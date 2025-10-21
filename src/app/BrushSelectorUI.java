package app;

import javax.swing.*;
import java.awt.*;

public class BrushSelectorUI extends JComponent {
    private Graphics2D g2d;

    public BrushSelectorUI(){}

    @Override
    protected void paintComponent(Graphics g) {

        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);

        int size = 75;

        for(int i = 0; i < 6; i ++) {
            g2d.fillRect(12+(size + 8)*i, 10, size, size);
        }


    }
}
