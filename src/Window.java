import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.*;

public class Window {

    private JFrame frame;

    public Window(){

        frame = new JFrame();
        frame.setSize(1900, 980);
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(53,62,67));
        frame.setTitle("World Map Generator");


        loadInterface();

    }

    private void loadInterface(){

        Board board = new Board();
        board.generateTerrain();
        frame.add(board, BorderLayout.CENTER);

        JPanel parameters = new JPanel();

        parameters.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        parameters.setPreferredSize(new Dimension(430, 900));
        parameters.setBackground(new Color(176,141,87));
        parameters.setLayout(new GridLayout(8, 1, 5, 15));


        frame.add(parameters, BorderLayout.EAST);

        JTextField seed = new JTextField("Seed");

        seed.setFont(new Font("Serif", Font.ITALIC, 28));
        seed.setHorizontalAlignment(SwingConstants.CENTER);
        seed.setForeground(new Color(20, 20, 50));
        seed.setBackground(new Color(176,141,87));

        seed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setNewSeed(seed.getText());
                board.generateTerrain();
                board.repaint();
            }
        });


        parameters.add(seed);



        JSlider DetailLvl = new JSlider();

        DetailLvl.setMinimum(5);
        DetailLvl.setMaximum(15);
        DetailLvl.setValue(10);

        DetailLvl.addChangeListener(e -> {
            board.setDetailsLvl(DetailLvl.getValue());
            board.generateTerrain();
            board.repaint();
        });

        DetailLvl.addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            int currentValue = DetailLvl.getValue();
            int newValue = currentValue - notches; // Adjust slider value based on scroll direction

            // Ensure the new value stays within the slider's bounds
            if (newValue < DetailLvl.getMinimum()) {
                newValue = DetailLvl.getMinimum();
            } else if (newValue > DetailLvl.getMaximum()) {
                newValue = DetailLvl.getMaximum();
            }
            DetailLvl.setValue(newValue);

            board.setDetailsLvl(newValue);

            board.generateTerrain();
            board.repaint();

        });

        parameters.add(DetailLvl);


        MapsForceUI MPFUI = new MapsForceUI();

        MPFUI.setBaseNoise(board.getFinalNoise());
        parameters.add(MPFUI);



        ColorRampUI rampUI = new ColorRampUI();
        rampUI.setRamp(board.getColorRamp());

        parameters.add(rampUI);
        rampUI.drawUI();
        rampUI.setBoard(board);

        NodeEditorUI nodeEditorUI = new NodeEditorUI();
        parameters.add(nodeEditorUI);

        rampUI.setNodeEditorUI(nodeEditorUI);
        nodeEditorUI.setRampUI(rampUI);
        nodeEditorUI.setBoard(board);


        parameters.revalidate();
        parameters.repaint();

    }

}
