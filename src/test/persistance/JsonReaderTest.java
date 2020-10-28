package persistance;

import model.GameBoard;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Source: JSON Serialization Demo
public class JsonReaderTest extends JsonTest {

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;
    public static final int RED = 0;
    public static final int YELLOW = 1;

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            GameBoard gb = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReaderEmptyGameboardOnePlayer() {
        JsonReader reader = new JsonReader("./data/testEmptyGameboardOnePlayer.json");
        try {
            GameBoard gb = reader.read();
            assertTrue(gb.isClear());
            assertTrue(gb.getTurn());
            assertEquals(gb.getNumPlayers(), 1);
            assertEquals(gb.getRedWins(), 0);
            assertEquals(gb.getYellowWins(), 0);
            assertEquals(gb.getTies(), 0);

        } catch (IOException e) {
            Assertions.fail("Couldn't read from file");
        }
    }

    @Test
    public void testReaderExampleGameboard() {
        JsonReader reader = new JsonReader("./data/testExampleGameboard.json");
        try {
            GameBoard gb = reader.read();
            assertFalse(gb.isClear());
            assertFalse(gb.getTurn());
            assertEquals(gb.getNumPlayers(), 2);
            assertEquals(gb.getRedWins(), 1);
            assertEquals(gb.getYellowWins(), 0);
            assertEquals(gb.getTies(), 0);
            checkGamePiece(YELLOW, gb.getGamePiece(1, 1));
            checkGamePiece(YELLOW, gb.getGamePiece(5, 1));
            checkGamePiece(RED, gb.getGamePiece(4, 1));
            checkGamePiece(RED, gb.getGamePiece(4, 2));
            checkGamePiece(RED, gb.getGamePiece(5, 2));


        } catch (IOException e) {
            Assertions.fail("Couldn't read from file");
        }
    }
}
