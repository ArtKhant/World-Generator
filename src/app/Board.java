package app;

import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import generator.PerlinNoiseGenerator;

public class Board extends JComponent {

    private Graphics2D g2d;

    private short[][] FinalNoise;


    private int force = 10;


    private int x = 1400;
    private int y = 900;
    private int bevel = 900;

    private colorRamp ramp = null;
    private String seed = "Seed";
    private int step = 450;
    private int detailsLvl = 15;


    private PerlinNoiseGenerator PNG;


    @Override
    protected void paintComponent(Graphics g) {

        long before = System.nanoTime();

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
            ramp.addNewNode(new Color(40, 35, 60), 810);
            ramp.addNewNode(new Color(60, 60, 90), 880);
            ramp.addNewNode(new Color(200, 200, 250), 1000);
        }
    }

    private short[][] GenerateFractalMap(short[][] map, int force, int depth){
        

        int totalLayers = detailsLvl;


        int[] matrixStep = new int[totalLayers];
        double[] matrixStrength = new double[totalLayers];

        List<float[][][]> vectorMaps = new ArrayList<>();

        long before = System.nanoTime();

        for(int i = step/2, amountOfLayers = 0; i > 2 && amountOfLayers < totalLayers; i--){

            if(step % i == 0) {

                matrixStep[amountOfLayers] = i;
                matrixStrength[amountOfLayers] = 1/(step/i / 1d) / 5 * force;
                vectorMaps.add(PNG.generateVectorMap(x, y, i));
                amountOfLayers ++;


            }

        }


        System.out.println("Time on vector maps milliseconds ~ " + (System.nanoTime() - before)/1000000f);

        before = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<short[][]>> futures = new ArrayList<>();

        for (int l = 0; l < totalLayers; l++) {

            final int layer = l;
            final int step = matrixStep[l];
            final float[][][] vectorMap =  vectorMaps.get(l);


            futures.add(executor.submit(() ->
                    PNG.generateNoiseMap(x, y, step, vectorMap)
            ));
        }

        List<short[][]> allLayers = new ArrayList<>();
        for (Future<short[][]> future : futures) {
            try {
                allLayers.add(future.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Time on noise maps milliseconds ~ " + (System.nanoTime() - before)/1000000f);

        before = System.nanoTime();

        short[][] currentMap = map; // Start with base map
        for (int l = 0; l < totalLayers; l++) {

            currentMap = PNG.addMap(currentMap, allLayers.get(l), matrixStrength[l]);
        }
        map = currentMap;
        System.out.println("Time on sum maps milliseconds ~ " + (System.nanoTime() - before)/1000000f);


//        for(int i = step/2, amountOfLayers = 0; i > 2 && amountOfLayers < depth; i--){
//
//            if(step % i == 0) {
//
//                map = PNG.addMap(map, PNG.generateNoiseMap(x, y, i, PNG.generateVectorMap(x, y, i)), (double) (1/(step/i / 1f) / 5 * force));
//                amountOfLayers ++;
//
//            }
//
//        }

        allLayers.clear();
        allLayers = null; // Remove reference
        vectorMaps.clear();
        vectorMaps = null;
        matrixStep = null;
        matrixStrength = null;

        Runtime.getRuntime().gc();

        return map;
    }

    public void generateTerrain(){
        PNG = new PerlinNoiseGenerator(seed);

        long before = System.nanoTime();

        FinalNoise = PNG.generateNoiseMap(x, y, step, PNG.generateVectorMap(x, y, step));
        FinalNoise = GenerateFractalMap(FinalNoise, force, detailsLvl);
        FinalNoise = PNG.fixOverLimits(FinalNoise);

        FinalNoise = PNG.islandficate(x, y, bevel, FinalNoise);
        FinalNoise = PNG.fixOverLimits(FinalNoise);

        float SecPerFrame = (System.nanoTime() - before)/1000000000f;
        System.out.println("Current FPS ~ " + (int) (1/SecPerFrame));

    }

    public void setNewSeed(String newSeed){
        seed = newSeed;
    }


    public void setDetailsStrengh(int newForce){force = newForce;}


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
