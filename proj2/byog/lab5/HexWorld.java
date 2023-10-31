package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;


import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {



    /* the top side length of single hexThing */
    public int scale = 4;

    /*random seed to get random */
    private final int seed =  2560;

    /* fill the word with a single hexThing according to its position x, y and what fill in is tile */
    public void fillAHex(TETile[][] word, int x, int y, TETile tile) {

        fillAHexHelper(word, x, y, tile);

    }

        /* this is a helper method which server the method above */
    private void fillAHexHelper(TETile[][] word, int x, int y, TETile tile) {
            for (int i = 0; i < scale; i++) {
                for (int j = 0; j < scale + i * 2; j++) {
                    word[x + j - i][y + i] = tile;
                }
            }
            for (int i = 0; i < scale; i++) {
                for (int j = 0; j < scale + i * 2; j++) {
                    word[x + j - i][y + 2 * scale - i - 1] = tile;
                }
            }

    }

    /* this is the core method which fill word with theThing as many as we want */
    public void Hexagons(TETile[][] word, int cornerX, int cornerY) {
        double centerX = cornerX + (scale - 1) ;
        double centerY = cornerY + (double) (scale * 2 - 1) / 2;
        double[][] centerPos = tileCenterPosition(centerX, centerY);
        ArrayList<double[]> centerPosition = tileCenterPositionMore(centerPos);

        Random RANDOM = new Random(seed);


        for (double[] cord: centerPosition) {
            int corneX = (int) (cord[0] - (scale - 1));
            int corneY = (int) (cord[1] - (double)(scale * 2 - 1 ) / 2);
            int tileNum = RANDOM.nextInt(3);
            TETile tile = Tileset.NOTHING;
            if (tileNum == 0) {
                 tile = Tileset.WALL;
            } else if (tileNum == 1) {
                tile = Tileset.FLOOR;
            } else {
                tile = Tileset.PLAYER;
            }
            fillAHex(word, corneX, corneY, tile);
        }


    }


    /* given the center pos and return the pos around them and it use the helper method */
    private ArrayList<double[]> tileCenterPositionMore(double[][] centerPositions) {
        ArrayList<double[]> cenPos = new ArrayList<>();
        for (double[] pos : centerPositions) {
            double[][] poss = tileCenterPosition(pos[0], pos[1]);
            for (double[] posses: poss) {
                if (!cenPos.contains(posses)) {
                    cenPos.add(posses);
                }
            }
        }
        return cenPos;
    }

    /*  this is the method that return center positions around the pos given x, y*/
    private  double[][] tileCenterPosition(double x, double y) {

        int[][]  absPositions = { {0, scale * 2}, {0, -(scale * 2)}, {scale * 2 - 1, scale}, {scale * 2 - 1, -scale},
                {-(scale * 2 - 1), -scale}, {-(scale * 2 - 1), scale}};
        double[][] positions = new double[absPositions.length][absPositions.length];
        for (int i = 0; i < absPositions.length; i++) {
            double[] addPos = {x + absPositions[i][0], y + absPositions[i][1]};
            positions[i] = addPos;
        }
        return positions;
    }


    // main method here
    public static void main(String[] args) {

        int width = 80;
        int height = 60;

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);

        TETile[][] word = new TETile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                word[x][y] = Tileset.FLOWER;
            }
        }

        HexWorld hexworld = new HexWorld();

        hexworld.Hexagons(word, width / 2, height / 2);


        ter.renderFrame(word);

    }


}
