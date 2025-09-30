import org.w3c.dom.Node;

import java.util.ArrayList;
import java.awt.Color;
import java.util.Collections;

public class colorRamp {

    private ArrayList<ColorNode> nodes = new ArrayList<>();
    private ArrayList<Color> ramp = new ArrayList<>();

    public colorRamp(){}

    public void addNewNode(ColorNode node){
        nodes.add(node);
        bakeRamp();
    }

    public void addNewNode(Color color, int position){
        nodes.add(new ColorNode(color, position));
        bakeRamp();
    }

    public void removeNode(ColorNode node){
        nodes.remove(node);
        bakeRamp();
    }

    public ArrayList<ColorNode> getNodes(){

        return nodes;
    }

    public Color getColor(int index){

        if (index >= ramp.size() || nodes.size() == 0) {

            return Color.WHITE; // or throw a more descriptive exception
        }
        return ramp.get(index);
    }

    public void bakeRamp(){

        if(nodes.size()==0){
            return;
        }

        ramp.clear();

        int currentIndex = 0;
        int previousIndex = -1;
        Color previousColor = null;

        Collections.sort(nodes, (n1, n2) -> Integer.compare(n1.getIndex(), n2.getIndex()));

        for(int i = 0; i < nodes.size()+1; i++){


            if (previousIndex + 1 < nodes.size()) {

                currentIndex = previousIndex + 1;

            } else {

                currentIndex = nodes.size() - 1; // Use the last node for the final stretch
            }
            if (previousIndex == -1){

                previousIndex = currentIndex;

            }



            int previusPosition = nodes.get(previousIndex).getIndex();
            int needeedPosition = nodes.get(currentIndex).getIndex();

            if(nodes.size() == 1){

                previousColor = nodes.get(currentIndex).getColor();
                previusPosition = 0;
                needeedPosition = 1000;

            }

            else if(previousColor == null){

                previousColor = nodes.get(currentIndex).getColor();
                previusPosition = 0;

            }
            else if (i == nodes.size()) {

                needeedPosition = 1000;

            }


            for (int c = previusPosition; c < needeedPosition; c++){

                double progress = (double)(c - previusPosition) / (needeedPosition - previusPosition);

                int R = (int)(previousColor.getRed() + progress * (nodes.get(currentIndex).getColor().getRed() - previousColor.getRed()));
                int G = (int)(previousColor.getGreen() + progress * (nodes.get(currentIndex).getColor().getGreen() - previousColor.getGreen()));
                int B = (int)(previousColor.getBlue() + progress * (nodes.get(currentIndex).getColor().getBlue() - previousColor.getBlue()));
                ramp.add(new Color(R, G, B));

            }

            previousColor = nodes.get(currentIndex).getColor();
            previousIndex = currentIndex;


        }
    }

}
