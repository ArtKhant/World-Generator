package app;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NodeEditorUI extends JComponent {

    private ColorNode node;
    private Board board;
    private ColorRampUI rampUI;
    private Graphics2D g2d;

    private JSpinner redSpinner;
    private JSpinner greenSpinner;
    private JSpinner blueSpinner;

    public NodeEditorUI(){}

    public void setNode(ColorNode node){this.node = node; if(node != null){drawTextFields();}}

    public void setBoard(Board board){this.board = board;}

    public void setRampUI(ColorRampUI rampUI){this.rampUI = rampUI;}



    @Override
    protected void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(node == null){

            for (int i = 0; i < 50; i++){

                g2d.setColor(new Color(181+i, 166+i, 66+i));
                g2d.drawOval(20+i , 2+i, 100-i*2, 100-i*2);

            }

            for (int i = 0; i < 5; i++){

                g2d.setColor(new Color(181+i*3, 166+i*3, 66+i*3));


                g2d.drawRect(135+i*3 , 2, 1, 100);



            }





            for (int i = 0; i < 20; i++){

                g2d.setColor(new Color(181+i, 166+i, 66+i));

                //hidden color sliders
                g2d.drawRect(165 , 2+i, 100, 1);
                g2d.drawRect(165 , 42+i, 100, 1);
                g2d.drawRect(165 , 82+i, 100, 1);

                //hidden color values
                g2d.drawRect(275 , 2+i, 70, 1);
                g2d.drawRect(275 , 42+i, 70, 1);
                g2d.drawRect(275 , 82+i, 70, 1);

                g2d.drawOval(350+i/2 , 2+i/2, 20-i, 20-i);
                g2d.drawOval(350+i/2 , 42+i/2, 20-i, 20-i);
                g2d.drawOval(350+i/2 , 82+i/2, 20-i, 20-i);

            }



        }

        else {

            //Color wheel
            int selectedColorX = 70;
            int selectedColorY = 52;

            int MinRedApprox = 5;
            int MinGreenApprox = 5;
            int MinBlueApprox = 5;

            float brightness = Math.max(node.getColor().getRed(), Math.max(node.getColor().getBlue(), node.getColor().getGreen()))/255f;

            for(int angle = 0; angle < 1000; angle++){

                for (int distance = 0; distance < 1000; distance++){

                    Color pixelColor = new Color(Color.HSBtoRGB(angle/1000f, distance/1000f, brightness));
                    g2d.setColor(pixelColor);

                    g2d.fillRect((int) (70 + Math.cos(Math.toRadians(360*angle/1000f)) * distance/1000f*50), (int) (52 + Math.sin(Math.toRadians( 360*angle/1000f)) * distance/1000f*50), 1, 1);

                    int redApprox = Math.abs(node.getColor().getRed()-pixelColor.getRed());
                    int greenApprox = Math.abs(node.getColor().getGreen()-pixelColor.getGreen());
                    int blueApprox = Math.abs(node.getColor().getBlue()-pixelColor.getBlue());

                    if(redApprox <= MinRedApprox && blueApprox <= MinBlueApprox && greenApprox <= MinGreenApprox){

                        MinRedApprox = redApprox;
                        MinBlueApprox = blueApprox;
                        MinGreenApprox = greenApprox;
                        selectedColorX = (int) (70 + Math.cos(Math.toRadians(360*angle/1000f)) * distance/1000f*50);
                        selectedColorY = (int) (52 + Math.sin(Math.toRadians(360*angle/1000f)) * distance/1000f*50);

                    }

                }
            }

            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval( selectedColorX-6, selectedColorY-6, 12, 12);
            g2d.setColor(node.getColor());
            g2d.fillOval( selectedColorX-5, selectedColorY-5, 10, 10);

            //brightness gradient column
            for (int i = 0; i < 50; i++){

                int gradColor = (int) (255 * (2*i) / 100f);
                g2d.setColor(new Color(gradColor, gradColor, gradColor));

                if(Math.abs(brightness - 2*i/100f) < 0.01){
                    selectedColorY = 2*i;
                }
                g2d.fillRect(135 , 2+i*2, 15, 2);

            }

            g2d.setColor(Color.WHITE);
            g2d.fillRect(135-3 , 2+selectedColorY-3, 21, 7);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(135-2 , 2+selectedColorY-2, 19, 5);
            g2d.setColor(new Color(brightness, brightness, brightness));
            g2d.fillRect(135-1 , 2+selectedColorY-1, 17, 3);



            //Color columns
            for (int i = 0; i < 50; i++){

                int colorVal = (int) (255*i/100f*2);

                //color sliders
                g2d.setColor(new Color(colorVal,0 , 0));
                g2d.fillRect(165+i*2 , 2, 2, 20);

                g2d.setColor(new Color(0,colorVal , 0));
                g2d.fillRect(165+i*2 , 42, 2, 20);

                g2d.setColor(new Color(0,0 , colorVal));
                g2d.fillRect(165+i*2 , 82, 2, 20);



            }

            g2d.setColor(Color.red);
            g2d.fillOval(350 , 2, 20, 20);
            g2d.setColor(Color.green);
            g2d.fillOval(350 , 42, 20, 20);
            g2d.setColor(Color.blue);
            g2d.fillOval(350 , 82, 20, 20);

            //draw sliders
            g2d.setColor(Color.BLACK);
            g2d.fillRect(165+(int) (node.getColor().getRed()/255f*100)-6,1,12,22);
            g2d.setColor(new Color(node.getColor().getRed(),0,0));
            g2d.fillRect(165+(int) (node.getColor().getRed()/255f*100)-5,2,10,20);

            g2d.setColor(Color.BLACK);
            g2d.fillRect(165+(int) (node.getColor().getGreen()/255f*100)-6,41,12,22);
            g2d.setColor(new Color(0,node.getColor().getGreen(),0));
            g2d.fillRect(165+(int) (node.getColor().getGreen()/255f*100)-5,42,10,20);

            g2d.setColor(Color.BLACK);
            g2d.fillRect(165+(int) (node.getColor().getBlue()/255f*100)-6,81,12,22);
            g2d.setColor(new Color(0,0,node.getColor().getBlue()));
            g2d.fillRect(165+(int) (node.getColor().getBlue()/255f*100)-5,82,10,20);



        }

    }

    private void drawTextFields(){

        this.removeAll();

        JPanel colorValues = new JPanel();
        colorValues.setLayout(new GridLayout(3, 1, 3, 5));

        redSpinner = new JSpinner(new SpinnerNumberModel(node.getColor().getRed()/255f, 0, 1, 0.01));
        greenSpinner = new JSpinner(new SpinnerNumberModel(node.getColor().getGreen()/255f, 0, 1, 0.01));
        blueSpinner = new JSpinner(new SpinnerNumberModel(node.getColor().getBlue()/255f, 0, 1, 0.01));

        ChangeListener colorUpdated =e->{
            int r = (int) ((double) redSpinner.getValue() * 255);
            int g = (int) ((double) greenSpinner.getValue() * 255);
            int b = (int) ((double) blueSpinner.getValue() * 255);


            node.setColor( new Color(r, g, b));

            updateScreen();
        };


        redSpinner.addChangeListener(colorUpdated);
        greenSpinner.addChangeListener(colorUpdated);
        blueSpinner.addChangeListener(colorUpdated);

        redSpinner.setPreferredSize(new Dimension(60, 20));
        greenSpinner.setSize(100, 30);
        blueSpinner.setSize(100, 30);

        colorValues.add(redSpinner);
        colorValues.add(greenSpinner);
        colorValues.add(blueSpinner);

        colorValues.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 90));
        colorValues.setSize(100, 100);

        colorValues.setOpaque(false);

        setLayout(new BorderLayout());
        add(colorValues, BorderLayout.EAST);

        createSlidersHandeler();
    }

    private void createSlidersHandeler(){
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (node == null) return;

                int x = e.getX();
                int y = e.getY();

                if (x >= 165 && x <= 265) {

                    int r = node.getColor().getRed();
                    int g = node.getColor().getGreen();
                    int b = node.getColor().getBlue();

                    int newColor = (int) ((x - 165) / 100f * 255);

                    if (y > 1 && y < 23) {

                        node.setColor(new Color(newColor, g, b));
                        redSpinner.setValue(newColor / 255d);

                    }
                    else if (y > 41 && y < 63) {

                        node.setColor(new Color(r, newColor, b));
                        greenSpinner.setValue(newColor / 255d);

                    }
                    else if (y > 81 && y < 103) {

                        node.setColor(new Color(r, g, newColor));
                        blueSpinner.setValue(newColor / 255d);

                    }

                    updateScreen();

                } //colors

                else if(x>134 && x < 151 && y > 2 && y < 102){

                    float newBrightness = y/100f;
                    float currentBrightness = Math.max(node.getColor().getRed(), Math.max(node.getColor().getBlue(), node.getColor().getGreen()))/255f;

                    int r = (int) (node.getColor().getRed()*newBrightness/currentBrightness);
                    int g = (int) (node.getColor().getGreen()*newBrightness/currentBrightness);
                    int b = (int) (node.getColor().getBlue()*newBrightness/currentBrightness);

                    node.setColor(new Color(r,g,b));

                    redSpinner.setValue(r / 255d);
                    greenSpinner.setValue(g/ 255d);
                    blueSpinner.setValue(b / 255d);

                    updateScreen();
                } //brightness

                else if((x-70) * (x-70) + (y-52) * (y-52) < 2500){

                    double angle;
                    if(x-70>0) {
                        angle = Math.atan((y - 52) / (x - 70d)) / (2 * Math.PI);
                    }
                    else {
                        angle = Math.atan((y - 52) / (x - 70d)) / (2 * Math.PI)+0.5;
                    }
                    double distance = Math.sqrt((x-70) * (x-70) + (y-52) * (y-52))/50d;
                    float currentBrightness = Math.max(node.getColor().getRed(), Math.max(node.getColor().getBlue(), node.getColor().getGreen()))/255f;

                    Color newColor = new Color(Color.HSBtoRGB((float) angle, (float) distance, currentBrightness));
                    node.setColor(newColor);
                    updateScreen();

                }
            }
        });
    }

    private void updateScreen(){

        revalidate();
        repaint();

        rampUI.getRamp().bakeRamp();

        board.setColorRamp(rampUI.getRamp());

        board.revalidate();
        board.repaint();

        rampUI.revalidate();
        rampUI.repaint();




    }

}
