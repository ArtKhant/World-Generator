package generator;

import java.util.Random;
import java.util.Timer;
import java.util.ArrayList;
import java.util.List;

public class PerlinNoiseGenerator {

    private Random rnd;
    private StringToSeed STS = new StringToSeed();




    public PerlinNoiseGenerator(String StringSeed){
        rnd = new Random();
        rnd.setSeed(STS.convert(StringSeed));
    }

    public float[][][] generateVectorMap(int X, int Y, int step){

        int w = X/step + 2;
        int h = Y/step + 2;

        float[][][] vectors = new float[w][h][2];

        for(int i = 0; i < w; i++){

            for(int j = 0; j < h; j++){

                double x = rnd.nextDouble(-1.0, 1.0);
                double y = rnd.nextDouble(-1.0, 1.0);
                double len = Math.sqrt(x*x + y*y);

                if (len == 0) { // rare, but avoid div-by-zero
                    x = 1.0;
                    y = 0.0;
                    len = 1.0;
                }

                double inv = 1.0 / len;

                float sx = (float) (x * inv);
                float sy = (float) (y * inv);

                vectors[i][j][0] = sx;
                vectors[i][j][1] = sy;

            }
        }

        return vectors;
    }



    public short[][] generateNoiseMap(int x, int y, int step, float[][][] vectors){

        short[][] map = new short[x][y];

//        System.out.println("generating noise");
//        long before = System.nanoTime();

        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++) {

                int currentX = i%step;
                int currentY = j%step;


                float[] Angle00 = vectors[i/step][j/step];
                float[] Angle10 = vectors[i/step + 1][j/step];
                float[] Angle01 = vectors[i/step][j/step + 1];
                float[] Angle11 = vectors[i/step + 1][j/step + 1];

                float DX = (float)currentX/step;
                float DY = (float)currentY/step;

                float Dot00 = (-DX) * Angle00[0] + (-DY) * Angle00[1];
                float Dot10 = (1-DX) * Angle10[0] + (-DY) * Angle10[1];
                float Dot01 = (-DX) * Angle01[0] + ((1-DY)) * Angle01[1];
                float Dot11 = (1-DX) * Angle11[0] + ((1-DY)) * Angle11[1];



                if(step > x/10){
                    DX = fade(DX);
                    DY = fade(DY);
                }



                float Upper = (1-DX) * Dot01 + DX * Dot11;
                float Lower = (1-DX) * Dot00 + DX * Dot10;

                map[i][j] = (short) (((1-DY) * Lower + DY * Upper) * 500);
            }
        }

//        System.out.println("time spended: " + (System.nanoTime() - before)/1000000 + "ms");

        return map;
    }




    private float fade(float t){

        return  t * t * (3 - 2 * t);

    }


    public short[][] addMap (short[][] map1, short[][] map2, double ForceOFChanges) {

//        System.out.println("adding noise");
//        long before = System.nanoTime();

        if(map1 == null || map2 == null){

            throw new RuntimeException("map1 or/and map2 are equal to null");

        }

        if(map1.length != map2.length || map1[0].length != map2[0].length){

            throw new RuntimeException("map1 and map2 aren't equal");

        }

        for(int x = 0; x < map2.length; x++){
            for(int y = 0; y < map2[0].length; y++){

                map1[x][y] += (short) (map2[x][y] * ForceOFChanges);

            }
        }

//        System.out.println("time spended: " + (System.nanoTime() - before)/1000000 + "ms");

        map2 = null;
        return map1;
    }



    public short[][] fixOverLimits(short[][] map){

//        System.out.println("fixing over limits noise");
//        long before = System.nanoTime();

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

//        System.out.println("time spended: " + (System.nanoTime() - before)/1000000 + "ms");
        return map;
    }


    public short[][] islandficate (int x, int y, int bevel, short[][] highmap){

//        System.out.println("make in island shape");
//        long before = System.nanoTime();
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

//        System.out.println("time spended: " + (System.nanoTime() - before)/1000000 + "ms");
        return highmap;
    }




}

