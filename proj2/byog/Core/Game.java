package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Game implements Serializable{
    public TERenderer ter;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int AREA = WIDTH * HEIGHT;
    private static final long serialVersionUID = 15498234798734234L;
    public Long Seed;
    public Random random;
    public String[][] buffer;
    public TETile[][] finalWordFrame;
    public HashMap<String, TETile> BufferCharMap;
    public int[] heroPos;

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
       BufferCharMap.put("·", Tileset.FLOOR);
   }

   public static Game LoadGame(){
       File f = new File("./game.ser");
       try{
           FileInputStream fs = new FileInputStream(f);
           ObjectInputStream os = new ObjectInputStream(fs);
           Game oldgame = (Game) os.readObject();
           os.close();
           return oldgame;
       } catch (FileNotFoundException e) {
           System.out.println("file not found");
           System.exit(0);
       } catch (IOException e){
           System.exit(0);
       } catch (ClassNotFoundException e) {
           System.out.println("class not found");
           System.exit(0);
       }
       return null;
   }

   @SuppressWarnings("ResultOfMethodCallIgnored")
   public static void saveGame(Game g) {
        File f = new File("./game.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(g);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.exit(0);
        }
    }

   public void newGame(Long seed, boolean isNew) {
       boolean quit = false;

       if (isNew) {
           Seed = seed;
           random = new Random(seed);

           generateWord();
           heroPosInitial();
       }

       ter.renderFrame(finalWordFrame);
       String directionCh = "wsad";
       Character ch;
       while (!quit) {
           // main loop

           boolean mousePress = StdDraw.isMousePressed();
           boolean keyTyped = StdDraw.hasNextKeyTyped();

           if ( !(mousePress || keyTyped)) continue;

           if (keyTyped) {
               ch = StdDraw.nextKeyTyped();
               if (ch.equals(':')) {
                   if (listenToKeyboard().equals('q')) {
                       quit = true;
                       continue;
                   }
               }
               if (directionCh.contains(ch.toString())) {
                   boolean NotWall = flashHeroPos(ch);
                   if (NotWall) {
                       ter.renderFrame(finalWordFrame);
                       ter.drawPos(heroPos);
                   }
               }
           }
           if (mousePress) {
               int mosX = (int) StdDraw.mouseX();
               int mosY = (int) StdDraw.mouseY();
               // calculate mouse x y correspond to which tile
               TETile tile =  finalWordFrame[mosX][mosY];
               ter.renderFrame(finalWordFrame);
               ter.drawMouseTileInfo(tile);
               ter.drawPos(heroPos);
           }
       }
       Game.saveGame(this);
       System.exit(0);
   }

    public Character listenToKeyboard(){
        while(true) {
            if (!StdDraw.hasNextKeyTyped()) continue;
            return StdDraw.nextKeyTyped();
        }
    }

   public boolean flashHeroPos(Character ch) {

       HashMap<Character, int[]> moveMap = new HashMap<>();
       moveMap.put('w', new int[] {0, 1});
       moveMap.put('s', new int[] {0, -1});
       moveMap.put('a', new int[] {-1, 0});
       moveMap.put('d', new int[] {1, 0});

       int[] move = moveMap.get(ch);
       int[] newPos = {heroPos[0] + move[0], heroPos[1] + move[1]};
       if (!checkIfWall(newPos)) {
           heroPos = newPos;
           return true;
       }
       return false;
   }

   public boolean checkIfWall(int[] pos) {
       return  buffer[pos[0]][pos[1]].equals("#");
   }

    public void heroPosInitial() {
       heroPos = new int[2];
       while (true) {
       int x = random.nextInt(WIDTH);
       int y = random.nextInt(HEIGHT);
       if (buffer[x][y].equals( "·")) {
           heroPos[0] = x;
           heroPos[1] = y;
           break;
        }
       }
    }

    public void playWithKeyboard() {

        ter.drawBeginUI(false);
        Character beginCh =  listenToKeyboard();
        if (beginCh.equals('l')) {
            File f = new File("./game.ser");
            if (f.exists()) {
                loadGame();
            } else {
                System.out.println("Load game file not exit ");
                System.exit(0);
            }
        }
        if (beginCh.equals('n')) {
            ter.drawBeginUI(true);
            StringBuilder seed = new StringBuilder();
            Character ch = ' ';
            while (!ch.equals('s')) {

                if (! StdDraw.hasNextKeyTyped()) continue;
                ch = StdDraw.nextKeyTyped();
                seed.append(ch);
            }
            seed.deleteCharAt(seed.length() - 1);
            newGame(Long.parseLong(seed.toString()), true);
        }
    }

    public void loadGame() {
        Game oldGame = Game.LoadGame();
        this.ter = oldGame.ter;
        this.finalWordFrame = oldGame.finalWordFrame;
        this.random = oldGame.random;
        this.heroPos = oldGame.heroPos;
        this.buffer = oldGame.buffer;
        this.Seed = oldGame.Seed;
        newGame(Seed, false);
    }

    public TETile[][] playWithInputString(String input) throws RuntimeException {

        Pattern r = Pattern.compile("^([nNlL])(\\d+)([sS]|:[qQ])$");
        Matcher matcher = r.matcher(input);
        if ( !matcher.find())  throw new RuntimeException();
        String begin = matcher.group(1);
        long seed = Long.parseLong( matcher.group(2));
        String end = matcher.group(3);

        if (begin.equalsIgnoreCase("n")) {
            Seed = seed;
            random = new Random(Seed);
            generateWord();
        }
        if (begin.equalsIgnoreCase("l")) ;

        return finalWordFrame;
    }

    public void generateWord() {

        ArrayList<Room> roomSet = createRandomRomes();

        addRomeToBuffer(roomSet);

        ArrayList<Room[]> roomPairInOrder =  arrangeLinkOrderRomes(roomSet);
        createHallBetRomes(roomPairInOrder);
        bufferToFinalWordFrame(finalWordFrame);
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
        roomSet.sort((o1, o2) -> (int) (o1.centerX - o2.centerX));
        ArrayList<Room> originGroup = new ArrayList<>();
        int middleIndex = (roomSet.size() - 1) / 2;
        Room middleRoom = roomSet.get(middleIndex);
        originGroup.add(middleRoom);
        ArrayList<Room> leftRooms = new ArrayList<>();
        ArrayList<Room> rightRooms = new ArrayList<>();
        int i = 0;
        while ( i < middleIndex ) {
            leftRooms.add(roomSet.get(i));
            i++;
        }
        i++;
        while (i < roomSet.size()) {
            rightRooms.add(roomSet.get(i));
            i++;
        }
//        ArrayList<Room> rightRooms = (ArrayList<Room>) roomSet.subList(middleIndex + 1, roomSet.size());

        addRoomToGroup(leftRooms,originGroup, middleRoom, 1);

        addRoomToGroup( rightRooms,originGroup, middleRoom, 2);

        originGroup.sort((o1, o2) -> (int) (o1.centerY - o2.centerY));

        groupInnerLink(originGroup, arrangeRoomPairs);

        ArrayList<Room> group1 = new ArrayList<>(originGroup);
        ArrayList<Room> group2 = new ArrayList<>(originGroup);

        linkHalfSide(leftRooms, group1, arrangeRoomPairs, 1);

        linkHalfSide(rightRooms, group2, arrangeRoomPairs, 2);

        return arrangeRoomPairs;

    }
   public void linkHalfSide(ArrayList<Room> halfSideRooms, ArrayList<Room> middleGroup, ArrayList<Room[]> arrangeRoomPairs, int direction) {
       ArrayList<Room> groupR = middleGroup;
        while(halfSideRooms.size() != 0) {
            ArrayList<Room> newGroup = new ArrayList<>();
            Room picRoom;
            if (direction == 1) {
                picRoom = halfSideRooms.remove(halfSideRooms.size() - 1);
            } else {
                picRoom = halfSideRooms.remove(0);
            }
            newGroup.add(picRoom);
            addRoomToGroup(halfSideRooms, newGroup, picRoom, direction);
            newGroup.sort((o1, o2) -> (int) (o1.centerY - o2.centerY));
            groupInnerLink(newGroup, arrangeRoomPairs);
            linkBetTwoGroup(groupR, newGroup, arrangeRoomPairs);
            groupR = newGroup;
        }
    }

    public void addRoomToGroup(ArrayList<Room> originRooms, ArrayList<Room> newRooms, Room curRoom, int direction){
        if (direction == 1 ){ // 1 for left , 2 for right
            for ( Room room : originRooms) {
                if (room.boundary[1] >= curRoom.boundary[0]){
                    newRooms.add(room);
//                    originRooms.remove(room);
                }
            }
            for (Room room: newRooms) {
                originRooms.remove(room);
            }

        } else {
            for ( Room room : originRooms) {
                if (room.boundary[0] <= curRoom.boundary[1]){
                    newRooms.add(room);
//                    originRooms.remove(room);
                }
            }
            for (Room room: newRooms) {
                originRooms.remove(room);
            }
        }
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
            i++;
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
        while (curArea < ( AREA / 10) * 5  ){
            Room newRoom = createOneRoom();

            boolean roomInWord = Room.inWorld(newRoom);
            if (!roomInWord) continue;

            boolean notCrash = true;
            for (Room room: roomSet) {

                notCrash = room.checkRomeNotCrash(newRoom);
                notCrash = newRoom.checkRomeNotCrash(room) && notCrash;
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
            int a = random.nextInt(25);
            if (a > 4) {
                posElements[i] = a;
                i++;
            }
        }
        while (i < 2) {
            int a = random.nextInt(17);
            if (a > 4) {
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
            for (int i = room.boundary[0] + 1; i < room.boundary[1]; i++) {
                for(int j = room.boundary[2] + 1; j < room.boundary[3]; j++) {
                    buffer[i][j] = "·";
                }
            }
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

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                TETile t = BufferCharMap.get(buffer[i][j]);
                finalWordFrame[i][j] = t;
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
        buffer[curX][Y] = "·";
        buffer[targetX][Y] = "·";
        curX++;
        while (curX < targetX) {
            buffer[curX][Y - 1] = "#";
            buffer[curX][Y] = "·";
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
        buffer[X][curY] = "·";
        buffer[X][targetY] = "·";
        curY++;
        while (curY < targetY) {
            buffer[X - 1][curY] = "#";
            buffer[X][curY] = "·";
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
        buffer[(int)pos.getX()][(int) pos.getY()] = "·";
    }
}