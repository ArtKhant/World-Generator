import java.util.Random;

public class PerlinNoiseGenerator {

    private Random rnd;
    private StringToSeed STS = new StringToSeed();
    private int DetailsLvl = 25;
    private double DetailsStrenght = 4.9;

    public PerlinNoiseGenerator(String StringSeed){
        rnd = new Random();
        rnd.setSeed(STS.convert(StringSeed));
    }

    public short[][] generateVectorMap(int x, int y, int step){

        int w = x/step + 2;
        int h = y/step + 2;

        short[][] vectors = new short[w][h];

        for(int i = 0; i < w; i++){

            for(int j = 0; j < h; j++){

                vectors[i][j] = (short) rnd.nextInt(0, 360);

            }
        }

        return vectors;
    }

    public short[][] generateNoiseMap(int x, int y, int step, short[][] vectors){

        short[][] map = new short[x][y];

        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++) {

                int currentX = i%step;
                int currentY = j%step;


                short Angle00 = vectors[i/step][j/step];
                short Angle10 = vectors[i/step + 1][j/step];
                short Angle01 = vectors[i/step][j/step + 1];
                short Angle11 = vectors[i/step + 1][j/step + 1];

                float DX = (float)currentX/step;
                float DY = (float)currentY/step;

                float Dot00 = (float) ((-DX) * Math.cos(Math.toRadians(Angle00)) + (-DY) * Math.sin(Math.toRadians(Angle00)));
                float Dot10 = (float) ((1-DX) * Math.cos(Math.toRadians(Angle10)) + (-DY) * Math.sin(Math.toRadians(Angle10)));
                float Dot01 = (float) ((-DX) * Math.cos(Math.toRadians(Angle01)) + ((1-DY)) * Math.sin(Math.toRadians(Angle01)));
                float Dot11 = (float) ((1-DX) * Math.cos(Math.toRadians(Angle11)) + ((1-DY)) * Math.sin(Math.toRadians(Angle11)));


                DX = fade(DX);
                DY = fade(DY);


                float Upper = (1-DX) * Dot01 + DX * Dot11;
                float Lower = (1-DX) * Dot00 + DX * Dot10;

                map[i][j] = (short) (((1-DY) * Lower + DY * Upper) * 500);
            }
        }

        return map;
    }

    private float fade(float t){

        return t * t * t * (t * (t * 6 - 15) + 10);

    }

    public short[][] islandficate (int x, int y, int bevel, short[][] highmap){

        //calculation for quarter to save resources
        double R = Math.sqrt(x*x/4 + y*y/4);
        for(int i = 0; i < x/2; i++){
            for(int j = 0; j < y/2; j++) {
                int dX = x/2 - i;
                int dY = y/2 - j;
                int dR = (int) Math.sqrt(dX*dX + dY*dY);
                if(dR<bevel){
                    if(bevel<R){
                        R-=bevel;
                    }
                    float newHeigh = (float) (Math.cos(Math.PI * (R-dR)/R));

                    highmap[i][j] -= newHeigh*500;
                    highmap[x-i-1][j] -= newHeigh*500;
                    highmap[i][y-j-1] -= newHeigh*500;
                    highmap[x-i-1][y-j-1] -= newHeigh*500;
                }
                else{
                    highmap[i][j] -= 500;
                    highmap[x-i-1][j] -= 500;
                    highmap[i][y-j-1] -= 500;
                    highmap[x-i-1][y-j-1] -= 500;
                }
            }
        }
        return highmap;
    }

    public short[][] addMap (short[][] map1, short[][] map2, double map2Force) {
        if(map1.length != map2.length || map1[0].length != map2[0].length){
            throw new RuntimeException("map1 and map2 has different sizes");
        }

        for(int x = 0; x < map1.length; x++){
            for(int y = 0; y < map1[0].length; y++){
                map1[x][y] += (short) (map2[x][y] * map2Force);
            }
        }

        return map1;
    }

    public short[][] fixOverLimits(short[][] map){

        double max = 0;

        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[0].length; y++){

                if(max < Math.abs(map[x][y])){
                    max = Math.abs(map[x][y]);
                }

            }
        }

        for(int x = 0; x < map.length; x++){
            for(int y = 0; y < map[0].length; y++) {
                map[x][y] = (short)( (map[x][y] / max) * 500);
            }
        }

        return map;
    }


}
