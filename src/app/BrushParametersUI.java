package app;

import javax.swing.*;
import java.awt.*;

public class BrushParametersUI extends JComponent {
    private Graphics2D g2d;

    public BrushParametersUI(){}

    @Override
    protected void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);


        g2d.fillRect(10, 15, 300, 15);
        g2d.fillRect(10, 55, 300, 15);

        g2d.fillRect(330, 0, 80, 80);


    }
}
