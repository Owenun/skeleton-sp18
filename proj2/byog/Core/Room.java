package byog.Core;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Room {

    public final int width;
    public final int height;
    public final int area;
    public final int leftX;
    public final int DownY;
    public final double centerX;
    public final double centerY;
    public final int[] boundary;
    public final int[][]corPoses;

    public Room(int width, int height, int leftX, int DownY) {
        this.width = width;
        this.height = height;
        this.area = width * height;
        this.leftX = leftX;
        this.DownY = DownY;
        this.boundary = new int[]{leftX, leftX + width -1, DownY, DownY + height - 1};
        this.corPoses =new int[][]{
                { boundary[0],  boundary[2]},
                { boundary[0],  boundary[3]},
                { boundary[1],  boundary[2]},
                { boundary[1], boundary[3]}
        };
        this.centerX = (double) (boundary[0] + boundary[1]) / 2;
        this.centerY = (double) (boundary[2] + boundary[3]) / 2;
    }


    /* static method to check whether two rooms crash or not
    * crash return false or return true */
    public static boolean checkRomeCrashed(Room room1, Room room2)  {
        boolean room1ToRoom2 = checkRomeCrashedHelper(room1, room2);
        if (!room1ToRoom2) {
            return false;
        }
        return checkRomeCrashedHelper(room2, room1);

    }

    private static boolean checkRomeCrashedHelper(Room room1, Room room2) {
        for (int[] cornerPos : room2.corPoses) {
            if (room1.boundary[0] <=  cornerPos[0] && cornerPos[0] <= room1.boundary[1] ) {
                if ( room1.boundary[2] <= cornerPos[1] && room1.boundary[3] >= cornerPos[1]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean inWorld(Room room) {
        if (room.boundary[1] >= Game.WIDTH || room.boundary[0] <= 0) {
            return false;
        }
        return room.boundary[2] > 0 && room.boundary[3] < Game.HEIGHT;
    }

    public  Point2D.Double[] linkPosWithOtherRoom( Room otherRoom) {
        /* step one find the relative position of two room*/

        double relativeAngle = calAngelWithOtherRoom(otherRoom);

        int side = checkWitchSide(relativeAngle);

        Point2D.Double[] posAB = calLinkPointBySide(side, otherRoom);

        return posAB;

    }

    public double calAngelWithOtherRoom(Room otherRoom) {
        Point2D.Double cur = new Point2D.Double(
                (double) (boundary[0] + boundary[1]) / 2 ,
                (double) (boundary[2] + boundary[3]) / 2
        );
        Point2D.Double other = new Point2D.Double(
                (double) (otherRoom.boundary[0] + otherRoom.boundary[1]) / 2,
                (double ) (otherRoom.boundary[2] + otherRoom.boundary[3]) / 2
        );
        double dx = other.getX() - cur.getX(); // 点B的x坐标减去点A的x坐标
        double dy = other.getY() - cur.getY(); // 点B的y坐标减去点A的y坐标
        double degree = Math.toDegrees(Math.atan2(dy, dx));
        return degree >= 0 ? degree : degree + 360;

    }

    public int checkWitchSide(double relativeAngle) {

        ArrayList<Double> anglesList = new ArrayList<>();
        for (int[] corPos : corPoses) {
            double dx = corPos[0] - centerX;
            double dy = corPos[1] - centerY;
            double angle = Math.toDegrees(Math.atan2(dy, dx));
            angle = angle > 0 ? angle : angle + 360;
            anglesList.add(angle);
            // the angle add in is not in order so hava to range again
        }
        ArrayList<Double> angles = new ArrayList<>();
        // sort the list to make angle in right order;
        angles.add(anglesList.get(3));
        angles.add(anglesList.get(1));
        angles.add(anglesList.get(0));
        angles.add(anglesList.get(2));

        int side;
        if (0 <= relativeAngle && relativeAngle <= angles.get(0)) {
            side = 0;
        } else if (angles.get(0) < relativeAngle && relativeAngle <= angles.get(1)) {
            side = 1;
        } else if (angles.get(1) < relativeAngle && relativeAngle <= angles.get(2)) {
            side = 2;
        } else if (angles.get(2) < relativeAngle && relativeAngle <= angles.get(3)) {
            side = 3;
        } else {
            side = 0;
        }
        return side;
    }

    public Point2D.Double[] calLinkPointBySide(int side, Room otherRoom) {
        double dirX = centerX;
        double dirY = centerY;
        double otherDirX = otherRoom.centerX;
        double otherDirY = otherRoom.centerY;

        Point2D.Double a;
        Point2D.Double b;

        if (side == 0) {
            int[] bouAs = {boundary[3], boundary[2]};
            int[] bouBs = {otherRoom.boundary[3],otherRoom.boundary[2]};
            int [] poses = calLinkPosBySideHelper(dirY, otherDirY, bouAs, bouBs, (int) dirY, (int) otherDirY);
            a = new Point2D.Double(boundary[1], poses[0]);
            b = new Point2D.Double(otherRoom.boundary[0], poses[1]);

        }
        else if (side == 1) {
            int[] bouAs = {boundary[1], boundary[0]};
            int[] bouBs = {otherRoom.boundary[1],otherRoom.boundary[0]};
            int [] poses = calLinkPosBySideHelper(dirX, otherDirX, bouAs, bouBs, (int) dirX, (int) otherDirX);
            a = new Point2D.Double(poses[0], boundary[3]);
            b = new Point2D.Double(poses[1], otherRoom.boundary[2]);
        } else if (side == 2) {
            int[] bouAs = {boundary[3], boundary[2]};
            int[] bouBs = {otherRoom.boundary[3],otherRoom.boundary[2]};
            int [] poses = calLinkPosBySideHelper(dirY, otherDirY, bouAs, bouBs, (int) dirY, (int) otherDirY);
            a = new Point2D.Double( boundary[0], poses[0]);
            b = new Point2D.Double( otherRoom.boundary[0], poses[1]);
        } else {
            int[] bouAs = {boundary[1], boundary[0]};
            int[] bouBs = {otherRoom.boundary[1],otherRoom.boundary[0]};
            int [] poses = calLinkPosBySideHelper(dirX, otherDirX, bouAs, bouBs, (int) dirX, (int) otherDirX);
            a = new Point2D.Double(poses[0], boundary[2]);
            b = new Point2D.Double( poses[1], otherRoom.boundary[3]);
        }


        return new Point2D.Double[]{a, b};

    }


    public int[] calLinkPosBySideHelper(double dirA, double dirB, int[] bouAs, int[] bouBs, int centA, int centB) {
        boolean dir = (dirB - dirA) >= 0;

        if (dir) {
            int bouA = bouAs[0];
            int bouB = bouBs[1];
            while(true) {
                if (centA >= centB) {
                    break;
                }
                if (centA < bouA - 1) {
                    centA++;
                }
                if (centA >= centB) {
                    break;
                }
                if (centB > bouB + 1){
                    centB--;
                }
                if (centB <= bouB + 1 && centA >= bouA - 1 ) {
                    break;
                }
            }
    }   else {
            int bouA = bouAs[1];
            int bouB = bouBs[0];
            while(true) {
                if (centA <= centB) {
                    break;
                }
                if (centA > bouA + 1) {
                    centA--;
                }
                if(centA <= centB) {
                    break;
                }
                if (centB > bouB - 1) {
                    centB++;
                }
                if (centA <= bouA + 1 && centB >= bouB - 1) {
                    break;
                }
            }
        }
        return new int[] {centA, centB};
        }
}
