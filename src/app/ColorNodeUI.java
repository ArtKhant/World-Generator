package app;

import javax.swing.*;
import java.awt.*;

public class ColorNodeUI extends JComponent {

    private Graphics2D g2d;

    private int x;
    private int y;
    boolean selected = false;
    private Board board;

    public ColorNodeUI(int x, int y){this.x = x; this.y = y;}

    public void setBoard(Board board){
        this.board = board;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(selected){
            g2d.setColor(Color.WHITE);
        }
        else {
            g2d.setColor(Color.GRAY);
        }

        for (int i = 0; i < 10; i++){
            g2d.fillRect(x-i*2,y,4*i,1);
        }

    }


}
