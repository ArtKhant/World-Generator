package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Window {

    private JFrame frame;

    public Window(){

        frame = new JFrame();
        frame.setSize(1900, 1800);
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(53,62,67));
        frame.setTitle("World Map Generator");


        loadInterface();

    }

    private void loadInterface(){
        
        Color backgroundColor = new Color(176,141,87);
        Color textColor = new Color(20, 20, 50);

        Board board = new Board();
        board.generateTerrain();
        frame.add(board, BorderLayout.CENTER);

        JPanel parameters = new JPanel();

        parameters.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        parameters.setPreferredSize(new Dimension(430, 900));
        parameters.setBackground(backgroundColor);
        parameters.setLayout(new GridLayout(8, 1, 5, 15));


        frame.add(parameters, BorderLayout.EAST);

        JTextField seed = new JTextField("Seed");

        seed.setFont(new Font("Serif", Font.ITALIC, 28));
        seed.setHorizontalAlignment(SwingConstants.CENTER);
        seed.setForeground(textColor);
        seed.setBackground(backgroundColor);

        seed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.setNewSeed(seed.getText());
                board.generateTerrain();
                board.repaint();
            }
        });


        parameters.add(seed);



        JSlider DetailsStrengh = new JSlider();

        DetailsStrengh.setMinimum(-30);
        DetailsStrengh.setMaximum(30);
        DetailsStrengh.setValue(10);

        DetailsStrengh.addChangeListener(e -> {
            board.setDetailsStrengh(DetailsStrengh.getValue());
            board.prepareMap();
            board.repaint();
        });

        DetailsStrengh.addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            int currentValue = DetailsStrengh.getValue();
            int newValue = currentValue - notches; // Adjust slider value based on scroll direction

            // Ensure the new value stays within the slider's bounds
            if (newValue < DetailsStrengh.getMinimum()) {
                newValue = DetailsStrengh.getMinimum();
            } else if (newValue > DetailsStrengh.getMaximum()) {
                newValue = DetailsStrengh.getMaximum();
            }
            DetailsStrengh.setValue(newValue);

            board.setDetailsStrengh(newValue);

            board.prepareMap();
            board.repaint();

        });
        
        DetailsStrengh.setBackground(backgroundColor);

        parameters.add(DetailsStrengh);


//        MapsForceUI MPFUI = new MapsForceUI();
//
//        parameters.add(MPFUI);





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

        BrushSelectorUI brushSelection = new BrushSelectorUI();

        parameters.add(brushSelection);


        BrushParametersUI brushesUI = new BrushParametersUI();

        parameters.add(brushesUI);

        parameters.revalidate();
        parameters.repaint();

    }

}
