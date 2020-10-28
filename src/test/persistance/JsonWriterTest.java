package persistance;

import model.GameBoard;
import model.GamePiece;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Source: JSON Serialization Demo
public class JsonWriterTest extends JsonTest {

    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final boolean RED_TURN = true;
    public static final boolean YELLOW_TURN = false;
    GamePiece red = new GamePiece(RED);
    GamePiece yellow = new GamePiece(YELLOW);

    @Test
    void testWriterInvalidFile() {
        try {
            GameBoard gb = new GameBoard();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGameBoard() {
        try {
            GameBoard gb = new GameBoard();
            gb.setTurn(RED_TURN);
            gb.setNumPlayers(1);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyGameboardOnePlayer.json");
            writer.open();
            writer.write(gb);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyGameboardOnePlayer.json");
            gb = reader.read();
            assertTrue(gb.isClear());
            assertTrue(gb.getTurn());
            assertEquals(gb.getNumPlayers(), 1);
            assertEquals(gb.getRedWins(), 0);
            assertEquals(gb.getYellowWins(), 0);
            assertEquals(gb.getTies(), 0);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterExampleGameboard() {
        try {
            GameBoard gb = new GameBoard();
            gb.addPiece(1, yellow);
            gb.addPiece(4, red);
            gb.addPiece(4, red);
            gb.addPiece(5, yellow);
            gb.addPiece(5, red);
            gb.setTurn(YELLOW_TURN);
            gb.setNumPlayers(2);
            gb.setRedWins(1);

            JsonWriter writer = new JsonWriter("./data/testWriterExampleGameboard.json");
            writer.open();
            writer.write(gb);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterExampleGameboard.json");
            gb = reader.read();
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
            fail("Exception should not have been thrown");
        }
    }
}
