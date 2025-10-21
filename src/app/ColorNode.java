package app;

import java.awt.*;

public class ColorNode {
    private Color color;
    private int index;

    public ColorNode(Color color, int index){
        this.color = color;
        this.index = index;
    }

    public void setIndex(int newIndex){
        index = newIndex;
    }

    public void setColor(Color NewColor) {
        color = NewColor;
    }

    public Color getColor() {
        return color;
    }

    public int getIndex() {
        return index;
    }
}
