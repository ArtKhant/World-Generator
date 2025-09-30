import javax.swing.*;
import java.awt.*;

public class MapsForceUI extends JComponent {

    private Graphics2D g2d;

    short[][] BaseNoise;
    short[][] SecondNoise;
    short[][] ThirdNoise;
    short[][] ForthNoise;

    short[][] result;

    short BaseNoiseForce;
    short MediumDetailsNoiseForce;
    short SmallDetailsNoiseForce;
    short BrushAdditionsForce;

    public MapsForceUI(){}

    @Override
    protected void paintComponent(Graphics g){
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Draw BaseNoise
        if(BaseNoise == null || BaseNoise.length == 0){

            g2d.setColor(Color.BLACK);
            g2d.fillRect(2, 2, (int) (90/1.5), (int) (140/1.5));

        }
        else{
            for(int x = 0; x < BaseNoise.length; x+=20){

                for (int y = 0; y < BaseNoise[x].length; y+=20){
                    int brightness = (int) ((BaseNoise[x][y] + 500)/1000f * 255);
                    Color pixelColor = new Color(brightness, brightness, brightness);
                    g2d.setColor(pixelColor);
                    g2d.fillRect(2+x/20, 2+y/20, 1,  1);
                }
            }
        }


    }

    public void setBaseNoise(short[][] baseNoise){
        BaseNoise = baseNoise;
    }

    public void setMediumDetailsNoise(short[][] secondNoise) {
        SecondNoise = secondNoise;
    }

    public void setSmallDetailsNoise(short[][] thirdNoise){
        ThirdNoise = thirdNoise;
    }

    public void setBrushAdditions( short[][] forthNoise){
        ForthNoise = forthNoise;
    }
}
