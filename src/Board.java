import javax.swing.*;
import java.awt.*;
import javax.swing.*;

public class Board extends JComponent {

    private Graphics2D g2d;

    private short[][] FinalNoise;
    private short[][] vectors;


    private int force = 10;


    private int x = 1400;
    private int y = 900;
    private int bevel = 900;

    private colorRamp ramp = null;
    private String seed = "";
    private int step = 450;
    private int detailsLvl = 10;
    private double detailsStrenght = 4.9;
    private PerlinNoiseGenerator PNG;


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(224, 201, 166));
        g2d.fillRect(20, 20, 900, 900);



        for(int i = 20; i < x + 20; i++){

            for(int j = 20; j < y + 20; j++ ){

                short h = FinalNoise[i-20][j-20];
                h += 500;

                g2d.setColor(ramp.getColor(h));

                g2d.fillRect(i*1, j*1, 1, 1);
            }
        }

    }


    public Board(){
        if(ramp == null){
            ramp = new colorRamp();

            ramp.addNewNode(new Color(0, 0, 0), 0);
            ramp.addNewNode(new Color(10, 50, 90), 500);
            ramp.addNewNode(new Color(30, 110, 160), 600);
            ramp.addNewNode(new Color(170, 160, 20), 620);
            ramp.addNewNode(new Color(100, 150, 20), 625);
            ramp.addNewNode(new Color(80, 120, 30), 725);
            ramp.addNewNode(new Color(50, 70, 40), 800);
            ramp.addNewNode(new Color(40, 40, 60), 810);
            ramp.addNewNode(new Color(60, 60, 90), 880);
            ramp.addNewNode(new Color(255, 255, 255), 1000);
        }
    }

    private short[][] GenerateFractalMap(short[][] map, int force, int depth){



        for(int i = step/2, amountOfLayers = 0; i > 5 && amountOfLayers < depth; i--){

            if(step % i == 0) {

                map = PNG.addMap(map, PNG.generateNoiseMap(x, y, i, PNG.generateVectorMap(x, y, i)), (double) (1/(step/i / 1f) / 5 * force));
                amountOfLayers ++;

            }

        }

        return map;
    }

    public void generateTerrain(){
        PNG = new PerlinNoiseGenerator(seed);

        vectors = PNG.generateVectorMap(x, y, step);
        FinalNoise = PNG.generateNoiseMap(x, y, step, vectors);
        FinalNoise = GenerateFractalMap(FinalNoise, force, detailsLvl);
        FinalNoise = PNG.fixOverLimits(FinalNoise);

        FinalNoise = PNG.islandficate(x, y, bevel, FinalNoise);
        FinalNoise = PNG.fixOverLimits(FinalNoise);

    }

    public void setNewSeed(String newSeed){
        seed = newSeed;
    }


    public void setDetailsLvl(int newDetailsLvl){detailsLvl = newDetailsLvl;}

    public void setDetailsStrenght(double newDetailsStrenght){detailsStrenght = newDetailsStrenght;}

    public void setColorRamp(colorRamp newRamp){
        ramp = newRamp;
    }

    public colorRamp getColorRamp() {
        return ramp;
    }

    public short[][] getFinalNoise() {
        return FinalNoise;
    }


}
