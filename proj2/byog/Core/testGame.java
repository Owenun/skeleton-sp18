package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.*;
public class testGame {

    Room r1 = new Room(10, 10, 40, 15);
    Room r2 = new Room(10, 10 , 55, 15);
    Room r3 = new Room(4, 4, 10 ,4 );
    Room r4 = new Room(4, 4, 8 ,10 );

    Game game = new Game();




    @Test
    public void testCreateRooms(){
        ArrayList<Room> roomSet = game.createRandomRomes();

        assertEquals(" ", 2, roomSet.size());

    }

    /* test whether buffer work well or not */
    @Test
    public void testAddRoomToBuffer() throws InterruptedException {

        ArrayList<Room> roomSet = game.createRandomRomes();

        game.addRomeToBuffer(roomSet);



        assertEquals(" ", "#", game.buffer[40][16]);
        assertEquals(" ", " ", game.buffer[5][7]);
        assertEquals(" ", "#", game.buffer[49][17]);
        assertEquals(" ", "#", game.buffer[45][15]);
    }

    @Test
    public void testBufferToFinalWorld() throws InterruptedException {

        ArrayList<Room> roomSet = game.createRandomRomes();


        game.addRomeToBuffer(roomSet);
        game.bufferToFinalWordFrame(game.finalWordFrame);

        assertEquals("", Tileset.NOTHING, game.finalWordFrame[2][2]);
        assertEquals("", Tileset.WALL, game.finalWordFrame[40][16]);
    }






}
