import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColorRampUI extends JComponent {

    private colorRamp ramp;
    private Graphics2D g2d;
    private JPanel parameters;
    private JButton addNode;
    private JButton removeNode;
    private ColorNode selectedNode = null;
    private Board board;
    private NodeEditorUI nodeEditorUI;

    public ColorRampUI(){    }

    public void drawUI(){
        setLayout(new BorderLayout());


        parameters = new JPanel();
        parameters.setBackground(new Color(176,141,87));
        parameters.setPreferredSize(new Dimension(20, 40));
        parameters.setLayout(new GridLayout(4, 1, 0, 0));

        addNode = new JButton("+");
        addNode.setFont(new Font("Serif", Font.BOLD, 10));
        addNode.setMargin(new Insets(0, 0, 0, 0));
        addNode.addActionListener(e -> {
            int r = 150;
            int g = 150;
            int b = 150;
            int distance = 500;

            if(selectedNode != null) {
                int minDistance = 1000;
                ColorNode nearestNode = new ColorNode(Color.WHITE, 500);

                for (ColorNode node : ramp.getNodes()) {
                    if (node != selectedNode && Math.abs(node.getIndex() - selectedNode.getIndex()) < minDistance) {
                        minDistance = Math.abs(node.getIndex() - selectedNode.getIndex());
                        nearestNode = node;
                    }
                }

                r = (selectedNode.getColor().getRed() + nearestNode.getColor().getRed())/2;
                g = (selectedNode.getColor().getGreen() + nearestNode.getColor().getGreen())/2;
                b = (selectedNode.getColor().getBlue() + nearestNode.getColor().getBlue())/2;

                distance = (selectedNode.getIndex() + nearestNode.getIndex())/2;




            }

            ColorNode newNode = new ColorNode(new Color(r,g,b), distance);
            selectedNode=newNode;

            ramp.addNewNode(newNode);
            ramp.bakeRamp();
            revalidate();
            repaint();

            board.setColorRamp(ramp);
            board.revalidate();
            board.repaint();

            nodeEditorUI.setNode(selectedNode);
            nodeEditorUI.revalidate();
            nodeEditorUI.repaint();
        });

        removeNode = new JButton("-");
        removeNode.setFont(new Font("Serif", Font.BOLD, 10));
        removeNode.setMargin(new Insets(0, 0, 0, 0));

        removeNode.addActionListener(e -> {

            ramp.removeNode(selectedNode);
            selectedNode=null;
            ramp.bakeRamp();
            revalidate();
            repaint();

            board.setColorRamp(ramp);
            board.revalidate();
            board.repaint();

            nodeEditorUI.setNode(selectedNode);
            nodeEditorUI.revalidate();
            nodeEditorUI.repaint();

        });



        parameters.add(addNode);
        parameters.add(removeNode);

        this.add(parameters, BorderLayout.EAST);

        //logic to select and drag color nodes
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int mousePosX = getMousePosition().x;
                int mousePosY = getMousePosition().y;

                for(ColorNode node : ramp.getNodes()){

                    if(mousePosX < node.getIndex()*2/5 + 20 && mousePosX > node.getIndex()*2/5 - 20 && mousePosY < 80 ){

                        selectedNode = node;
                        revalidate();
                        repaint();
                    }
                }

                nodeEditorUI.setNode(selectedNode);
                nodeEditorUI.repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (selectedNode != null) {

                    if(getMousePosition().x/2*5 < 1000){
                        selectedNode.setIndex(getMousePosition().x/2*5);

                        ramp.bakeRamp();
                        revalidate();
                        repaint();

                        board.setColorRamp(ramp);
                        board.revalidate();
                        board.repaint();
                    }
                }
            }

        });
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public void setNodeEditorUI(NodeEditorUI nodeEditorUI){
        this.nodeEditorUI = nodeEditorUI;
    }

    public void setRamp(colorRamp ramp) {
        this.ramp = ramp;
    }

    public ColorNode getSelectedNode(){return selectedNode;}

    @Override
    protected void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(ramp == null){
            return;
        }

        for(int i = 0; i < 200; i++){

            g2d.setColor(ramp.getColor(i*5));
            g2d.fillRect(i*2, 0, 2, 50);
        }

        for (ColorNode node : ramp.getNodes()){

            //cross lines
            for(int i = 0; i < 10; i++){

                if(i%2==0){
                    g2d.setColor(Color.BLACK);
                }
                else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(node.getIndex()*2/5, i*5, 2, 5);
            }


            if(node == selectedNode){
                g2d.setColor(Color.WHITE);
            }
            else {
                g2d.setColor(Color.GRAY);
            }

            //cool looking node
            for (int i = 0; i < 9; i++){
                g2d.fillRect(node.getIndex()*2/5-i+1,i+50,2*i-2,1);
            }

            if(node == selectedNode){
                g2d.setColor(Color.BLACK);
            }

            g2d.fillOval(node.getIndex()*2/5 - 8, 55, 16,16);
            g2d.setColor(node.getColor());
            g2d.fillOval(node.getIndex()*2/5 - 7, 56, 14,14);



        }

    }


    public colorRamp getRamp() {return ramp;}
}
