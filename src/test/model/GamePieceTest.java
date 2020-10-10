package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GamePieceTest {

    public static final int RED = 0;
    public static final int YELLOW = 1;

    GamePiece gp1;
    GamePiece gp2;

    @BeforeEach
    public void runBefore() {
        gp1 = new GamePiece(RED);
        gp2 = new GamePiece(YELLOW);
    }

    @Test
    public void testisRed() {
        assertTrue(gp1.isRed());
        assertFalse(gp2.isRed());
    }

    @Test
    public void testisYellow() {
        assertFalse(gp1.isYellow());
        assertTrue(gp2.isYellow());
    }

    @Test
    public void testGetColor() {
        assertEquals(gp1.getColor(), RED);
        assertEquals(gp2.getColor(), YELLOW);
    }



}