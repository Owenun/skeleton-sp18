package byog.Core;

import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static junit.framework.TestCase.*;

public class testRoom {

    Room r1 = new Room(4,4, 4, 4);
    Room r2 = new Room(4, 4, 10 ,4 ); // on the same x line with r1
    Room r3 = new Room(4, 4, 10 ,4 ); // on the same y line with r1
    Room r4 = new Room(4, 4, 8 ,10 ); // on the same x line crashed
    Room r5 = new Room(4, 4, 4 ,1 ); // on the same y line but crashed
    Room r6 = new Room(4 ,4, 10, 6);
    @Test
    public void testCheckedCashed() {

//        boolean test1 = Room.checkRomeCrashed(r1, r2);
//        boolean test2 = Room.checkRomeCrashed(r1, r3);
//        boolean test3 = Room.checkRomeCrashed(r1, r4);
//        boolean test4 = Room.checkRomeCrashed(r1, r5);

        double relativeAngle1 = r1.calAngelWithOtherRoom(r3);
        double relativeAngle2 = r1.calAngelWithOtherRoom(r4);
        double relativeAngle3 = r1.calAngelWithOtherRoom(r5);


//        assertTrue("room1 crash room2", test1);
//        assertTrue("room1 crash room2", test2);
//        assertFalse("room1 crash room2", test3);
//        assertFalse("room1 crash room2", test4);

        r1.calLinkPointBySide(0, r2);
        r1.calLinkPointBySide(0, r6);
        r1.calLinkPointBySide(1, r4);
        r1.calLinkPointBySide(3, r5);

//        assertEquals("", 0.0, relativeAngle1);
//        assertEquals("", 90.0, relativeAngle2);
//        assertEquals("", 270.0, relativeAngle3);

        assertEquals("", 0, r1.checkWitchSide(relativeAngle1));
        assertEquals("", 1, r1.checkWitchSide(relativeAngle2));
        assertEquals("", 3, r1.checkWitchSide(relativeAngle3));


    }

}
