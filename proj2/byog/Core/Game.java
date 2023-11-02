package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;



import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.*;


public class Game {
    public TERenderer ter;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int AREA = WIDTH * HEIGHT;
    public long seed;
    public Random random;
    public String[][] buffer;
    public TETile[][] finalWordFrame;
    public HashMap<String, TETile> BufferCharMap;


   public Game(){

       ter = new TERenderer();
       ter.initialize(WIDTH, HEIGHT);
       finalWordFrame = new TETile[WIDTH][HEIGHT];
       BufferCharMap = new HashMap<>(10);
       buffer = new String[WIDTH][HEIGHT];
       seed = Instant.now().toEpochMilli();
       random = new Random(seed);

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
     * of characters for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
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

        return null;
    }

    public TETile[][]  playNow()  {

        ArrayList<Room> roomSet = createRandomRomes();
        addRomeToBuffer(roomSet);
        ArrayList<Room[]> roomPairInOrder =  arrangeLinkOrderRomes(roomSet);
        createHallBetRomes(roomPairInOrder);
        bufferToFinalWordFrame(finalWordFrame);
        return finalWordFrame;
    }

    /** roomSet: the room group which need to give link order
     *  return:  ArrayList<Room[]>  the element in the Arraylist are two room which need to be linked. */
    public ArrayList<Room[]> arrangeLinkOrderRomes(ArrayList<Room> roomSet) {
        /*  method here is to give link order in room group
        *
        *   here are step to arrange room
        *   find the middle room R in group, and find room whose centX in the range RLeftX and RRightX
        *   make them a group and link them each other, in the range up to down
        *   turn left to find the nearest room T to current group
        *   find T group in the same way
        *   link group T
        *   link group T with last group, find nearest two room in two group
        *   repeat above until reach the leftest room
        *   repeat the above way to right
        *   check if any room forget linking with other
        *
        * @return ArrayList<Room[]> arrangeRomeList
        *   element in list is a Room[], and it contains two rooms which are arrange to link
        *  */

        ArrayList<Room[]> arrangeRoomPairs = new ArrayList<>();
        roomSet.sort((o1, o2) -> (int) (o1.centerX - o2.centerY));
        ArrayList<Room> originGroup = new ArrayList<>();
        int middleIndex = (roomSet.size() - 1) / 2;
        Room middleRoom = roomSet.get(middleIndex);
        originGroup.add(middleRoom);
        ArrayList<Room> leftRooms = (ArrayList<Room>) roomSet.subList(0, middleIndex);
        ArrayList<Room> rightRooms = (ArrayList<Room>) roomSet.subList(middleIndex + 1, roomSet.size());

        for (Room room : leftRooms) {
            if(room.boundary[1] >= middleRoom.boundary[0]) {
                originGroup.add(room);
                leftRooms.remove(room);

            }
        }
        for ( Room room : rightRooms) {
            if ( room.boundary[0] <= middleRoom.boundary[1]){
                originGroup.add(room);
                rightRooms.remove(room);
            }
        }

        originGroup.sort((o1, o2) -> (int) (o1.centerY - o2.centerY));

        groupInnerLink(originGroup, arrangeRoomPairs);

        ArrayList<Room> group1 = new ArrayList<>(originGroup);
        ArrayList<Room> group2 = new ArrayList<>(originGroup);

        while (leftRooms.size() != 0 ) {
            ArrayList<Room> newGroup = new ArrayList<>();
            Room pickRoom = leftRooms.remove(leftRooms.size() - 1);
            for (Room room : leftRooms) {
                if (room.boundary[1] >= pickRoom.boundary[0]) {
                    newGroup.add(room);
                    leftRooms.remove(room);
                }
            }
            groupInnerLink(newGroup, arrangeRoomPairs);
            linkBetTwoGroup(group1, newGroup, arrangeRoomPairs);
            group1 = newGroup;
        }

        while (rightRooms.size() != 0) {
            ArrayList<Room> newGroup = new ArrayList<>();
            Room pickRoom = leftRooms.remove(leftRooms.size() - 1);
            for (Room room : leftRooms) {
                if (room.boundary[0] >= pickRoom.boundary[1]) {
                    newGroup.add(room);
                    leftRooms.remove(room);
                }
            }
            groupInnerLink(newGroup, arrangeRoomPairs);
            linkBetTwoGroup(group2, newGroup, arrangeRoomPairs);
            group2 = newGroup;
        }

        return arrangeRoomPairs;

    }

    public void linkBetTwoGroup(ArrayList<Room> group1, ArrayList<Room> group2,ArrayList<Room[]> arrangeRoomPairs) {
        Room room1 = group1.get(0);
        Room room2 = group2.get(0);
        double minDistance = calDistanceBetPos(room1, room2);
        for (Room r1: group1){
            for(Room r2: group2) {
                double distance = calDistanceBetPos(r1, r2);
                if (distance < minDistance) {
                    minDistance = distance;
                    room1 = r1;
                    room2 = r2;
                }
            }
        }
        Room[] pairs = new Room[] {room1, room2};
        arrangeRoomPairs.add(pairs);
    }
    public double calDistanceBetPos(Room r1, Room r2) {
        Point2D.Double[] poses = r1.linkPosWithOtherRoom(r2);
        Point2D.Double p1 = poses[0];
        Point2D.Double p2 = poses[1];
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    public void groupInnerLink(ArrayList<Room> group, ArrayList<Room[]> arrangeRoomPairs){
        int i = 0;
        while( i + 1< group.size()) {
            Room[] roomPair = new Room[] {group.get(i), group.get(i + 1)};
            arrangeRoomPairs.add(roomPair);
        }
    }


    public ArrayList<Room> createRandomRomes() {

        /* there are a few limits for random rooms
        * Width and Height must bigger than 2 bur less than certain limit
        * one room can't crash  with another
        * rooms must in the word which means rooms can't over the boundary of the word
        * same random seed create same room group
        * */

        ArrayList<Room> roomSet = new ArrayList<>();
        int curArea = 0;
        while (curArea < AREA / 3 ){
            Room newRoom = createOneRoom();

            boolean roomInWord = Room.inWorld(newRoom);
            if (!roomInWord) continue;

            boolean notCrash = true;
            for (Room room: roomSet) {

                notCrash = room.checkRomeNotCrash(newRoom);
                if (!notCrash) break;
            }
            if (notCrash) {
                roomSet.add(newRoom);
                curArea += newRoom.area;
            }
        }
        return roomSet;
    }

    public Room createOneRoom() {
        int[] posElements = new int[4];
        int i = 0;
        while (i < 1) {
            int a = random.nextInt(30);
            if (a > 2) {
                posElements[i] = a;
                i++;
            }
        }
        while (i < 2) {
            int a = random.nextInt(20);
            if (a > 2) {
                posElements[i] = a;
                i++;
            }
        }
        while (i < 3) {
            int a = random.nextInt(WIDTH - posElements[0]);
            if (0 < a && a < WIDTH - posElements[0]) {
                posElements[i] = a;
                i++;
            }
        }
        while (i < 4) {
            int a = random.nextInt(HEIGHT - posElements[1]);
            if (0 < a && a < HEIGHT - posElements[1]) {
                posElements[i] = a;
                i++;
            }
        }
        int width = posElements[0];
        int height = posElements[1];
        int leftX = posElements[2];
        int downY = posElements[3];
        return new Room(width, height, leftX, downY);
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

    public void bufferToFinalWordFrame(TETile[][] finalWordFrame)  {

        /* test here*/
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < HEIGHT; j++) {
//                finalWordFrame[i][j] = Tileset.NOTHING;
//            }
//        }
//        ter.renderFrame(finalWordFrame);
        /* test above */

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                TETile t = BufferCharMap.get(buffer[i][j]);
                finalWordFrame[i][j] = t;
//                ter.renderFrame(finalWordFrame);
//                sleep(1);
            }
        }
    }


    public  void createHallBetRomes(ArrayList<Room[]> arrangeRoomPairs) {
        for (Room[] roomPair : arrangeRoomPairs){
            Room r1 = roomPair[0];
            Room r2 = roomPair[1];
            int side;
            double relativeDegree = r1.calAngelWithOtherRoom(r2);
            side = r1.checkWitchSide(relativeDegree);
            Point2D.Double[] poses = r1.linkPosWithOtherRoom(r2);
            linkPos(side, poses[0], poses[1]);
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