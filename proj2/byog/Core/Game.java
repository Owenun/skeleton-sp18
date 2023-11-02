package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

import static java.lang.Thread.sleep;


public class Game {
    public TERenderer ter;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public String[][] buffer;
    public TETile[][] finalWordFrame;
   public HashMap<String, TETile> BufferCharMap;


   public Game(){

       ter = new TERenderer();
       ter.initialize(WIDTH, HEIGHT);
       finalWordFrame = new TETile[WIDTH][HEIGHT];
       BufferCharMap = new HashMap<>(10);
       buffer = new String[WIDTH][HEIGHT];

       for(int i = 0; i < WIDTH; i++) {
           for (int j = 0; j < HEIGHT; j++) {
               buffer[i][j] = " ";
           }
       }

       BufferCharMap.put(" ", Tileset.NOTHING);
       BufferCharMap.put("#", Tileset.WALL);

   }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    public TETile[][]  playNow() throws InterruptedException {

        ArrayList<Room> roomSet = createRomes();
        addRomeToBuffer(roomSet);
//        sortRomes(roomSet);
        createHallBetRomes(roomSet);
        bufferToFinalWordFrame(finalWordFrame);
        return finalWordFrame;
    }

    public void addRomeToBuffer(ArrayList<Room> roomSet) {
        for (Room room: roomSet){
            drawLine(room.boundary[0], room.boundary[1], room.boundary[2], "Y"); // Y means keep Y not change and increase X
            drawLine(room.boundary[0], room.boundary[1], room.boundary[3], "Y");
            drawLine(room.boundary[2], room.boundary[3], room.boundary[0], "X"); // X means keep X not change while increase Y
            drawLine(room.boundary[2], room.boundary[3], room.boundary[1], "X");
        }
    }
    public void drawLine(int start, int end, int keep, String tag) {
        if (tag.equals("X")) {
            while (start <= end) {
                buffer[keep][start] = "#";
                start++;
            }
        }
        if (tag.equals("Y")) {
            while (start <= end) {
                buffer[start][keep] = "#";
                start++;
            }
        }
    }

    public void bufferToFinalWordFrame(TETile[][] finalWordFrame) throws InterruptedException {

        /* test here*/
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                finalWordFrame[i][j] = Tileset.NOTHING;
            }
        }
        ter.renderFrame(finalWordFrame);
        /* test above */

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                TETile t = BufferCharMap.get(buffer[i][j]);
                finalWordFrame[i][j] = t;
                ter.renderFrame(finalWordFrame);
                sleep(5);
            }
        }
    }


    public ArrayList<Room> createRomes() {

        ArrayList<Room> roomSet = new ArrayList<>();

        Room r1 = new Room(10, 10, 30, 15);
        Room r2 = new Room(10, 10 , 50, 15);
//
        roomSet.add(r1);
        roomSet.add(r2);
        return roomSet;

    }

    public  void createHallBetRomes(ArrayList<Room> roomSet) {
        int i = 0;
        while(i + 1< roomSet.size()) {
            Room cur = roomSet.get(i);
            Room next = roomSet.get(i + 1);
            int side;
            double relativeDegree = cur.calAngelWithOtherRoom(next);
            side = cur.checkWitchSide(relativeDegree);
            Point2D.Double[] poses = cur.linkPosWithOtherRoom(next);
            linkPos(side, poses[0], poses[1]);
            i++;
        }
    }

    public void linkPos(int side, Point2D.Double pos1, Point2D.Double pos2) {
        if ( side == 0 || side == 2) {
            if (pos1.getY() == pos2.getY()) {
                linkStraightHallFat(pos1, pos2);
            } else {
                linkLHallFat(pos1, pos2);
            }
        } else {
            if (pos1.getX() == pos2.getX()) {
                linkStraightHallVet(pos1, pos2);
            } else {
                linkLHallVet(pos1, pos2);
            }
        }
    }

    public void linkStraightHallVet(Point2D.Double pos1, Point2D.Double pos2) {
        if (pos1.getY() < pos2.getY()) {
            linkStraightHallVetHelper(pos1, pos2);
            }
         else {
            linkStraightHallVetHelper(pos2, pos1);
            }
        }

    public void linkStraightHallFatHelper(Point2D.Double pos1, Point2D.Double pos2) {
        int curX = (int) pos1.getX();
        int targetX = (int) pos2.getX();
        int Y = (int) pos1.getY();
        buffer[curX][Y] = " ";
        buffer[targetX][Y] = " ";
        curX++;
        while (curX < targetX) {
            buffer[curX][Y - 1] = "#";
            buffer[curX][Y] = " ";
            buffer[curX][Y + 1] = "#";
            curX++;
        }
    }

    public void linkStraightHallFat(Point2D.Double pos1, Point2D.Double pos2) {
        if (pos1.getX() < pos2.getX()) {
            linkStraightHallFatHelper(pos1, pos2);

        } else {
            linkStraightHallFatHelper(pos2, pos1);
        }
    }

    public void linkStraightHallVetHelper(Point2D.Double pos1, Point2D.Double pos2){
        int curY = (int) pos1.getY();
        int targetY = (int) pos2.getY();
        int X = (int) pos1.getX();
        buffer[X][curY] = " ";
        buffer[X][targetY] = " ";
        curY++;
        while (curY < targetY) {
            buffer[X - 1][curY] = "#";
            buffer[X][curY] = " ";
            buffer[X + 1][curY] = "#";
            curY++;
        }
    }

    public void linkLHallFat(Point2D.Double pos1, Point2D.Double pos2) {

        int middleX = (int) (pos1.getX() + pos2.getX()) / 2;
        Point2D.Double midPos1 = new  Point2D.Double(middleX, pos1.getY());
        Point2D.Double midPos2 = new  Point2D.Double(middleX, pos2.getY());
        wallAroundPos(midPos1);
        wallAroundPos(midPos2);
        linkStraightHallVet(midPos1, midPos2);
        linkStraightHallFat(pos1, midPos1);
        linkStraightHallFat(midPos2, pos2);
    }

    public void linkLHallVet(Point2D.Double pos1, Point2D.Double pos2) {
        int middleY = (int) (pos1.getY() + pos2.getY()) / 2;
        Point2D.Double midPos1 = new  Point2D.Double(pos1.getX(), middleY);
        Point2D.Double midPos2 = new  Point2D.Double(pos2.getX(), middleY);
        wallAroundPos(midPos1);
        wallAroundPos(midPos2);
        linkStraightHallFat(midPos1, midPos2);
        linkStraightHallVet(pos1, midPos1);
        linkStraightHallVet(midPos2, pos2);
    }

    public void wallAroundPos(Point2D.Double pos) {
        int[] posSet = {-1, 0, 1};
        for (int i: posSet) {
            for (int j: posSet) {
                int x = (int) pos.getX() + i;
                int y = (int) pos.getY() + j;
                if (buffer[x][y].equals( " ")) {
                    buffer[x][y] = "#";
                }
            }
        }
        buffer[(int)pos.getX()][(int) pos.getY()] = " ";
    }
}


