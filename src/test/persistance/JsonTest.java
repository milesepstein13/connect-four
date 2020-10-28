package persistance;

import model.GamePiece;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Source: JSON Serialization Demo
public class JsonTest {
    protected void checkGamePiece(int color, GamePiece gp) {
        assertEquals(color, gp.getColor());
    }
}
