package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GamePieceTest {

    public static final int RED = 0;
    public static final int YELLOW = 1;

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;

    GamePiece gp1;
    GamePiece gp2;

    GameBoard board;

    @BeforeEach
    public void runBefore() {
        gp1 = new GamePiece(RED);
        gp2 = new GamePiece(YELLOW);
        board = new GameBoard();
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

    @Test
    public void testAddPieceBottomLeft(){
        assertTrue(board.addPiece(1, gp1));
        assertEquals(board.getGamePiece(1, 1), gp1);
    }

    @Test
    public void testAddPieceMiddleRight(){
        board.addPiece(1, gp1);
        board.addPiece(BOARD_WIDTH, gp1);
        board.addPiece(BOARD_WIDTH, gp1);
        assertTrue(board.addPiece(BOARD_WIDTH, gp2));
        assertEquals(board.getGamePiece(BOARD_WIDTH, 2), gp1);
        assertEquals(board.getGamePiece(BOARD_WIDTH, 3), gp2);
    }

    @Test
    public void testAddPieceFull(){
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            board.addPiece(3, gp1);
        }
        assertEquals(board.getColumn(3).size(), BOARD_HEIGHT);
        assertFalse(board.addPiece(3, gp1));
        assertEquals(board.getColumn(3).size(), BOARD_HEIGHT);
    }

    @Test
    public void testClear(){
        for (int i = 0; i < BOARD_WIDTH; i++) {
            board.addPiece(i+1, gp1);
        }
        assertFalse(board.isClear());
        board.clear();
        assertTrue(board.isClear());
    }

    @Test
    public void testIsClearClear(){
        assertTrue(board.isClear());
    }

    @Test
    public void testIsClearOne(){
        assertTrue(board.isClear());
        board.addPiece(1, gp1);
        assertFalse(board.isClear());
    }

    @Test
    public void testClearMany(){
        assertTrue(board.isClear());
        board.addPiece(1, gp1);
        board.addPiece(BOARD_WIDTH, gp1);
        board.addPiece(BOARD_WIDTH, gp2);
        assertFalse(board.isClear());
    }


    @Test
    public void testIsFullFull(){
        assertFalse(board.isFull());
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board.addPiece(i+1, gp1);
            }
        }
        assertTrue(board.isFull());
    }

    @Test
    public void testIsFullNotFull(){
        assertFalse(board.isFull());
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 1; j < BOARD_HEIGHT; j++) {
                board.addPiece(i+1, gp1);
            }
        }
        assertFalse(board.isFull());
    }

    @Test
    public void testAIMove(){
        board.aiMove(gp1);
        int pieces = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (board.getColumn(i+1).contains(gp1)){
                pieces++;
            }
        }
        assertEquals(pieces, 1);
    }

    @Test
    public void testCheckGameOverNot(){
        board.addPiece(1, gp1);
        board.addPiece(1, gp2);
        assertFalse(board.checkGameOver());
    }

    @Test
    public void testCheckGameOverRed(){
        board.addPiece(1, gp1);
        board.addPiece(1, gp1);
        board.addPiece(1, gp1);
        board.addPiece(1, gp1);
        assertEquals(board.getRedWins(), 0);
        assertTrue(board.checkGameOver());
        assertEquals(board.getRedWins(), 1);
        assertTrue(board.isClear());

    }

    @Test
    public void testCheckGameOverYellow(){
        board.addPiece(1, gp2);
        board.addPiece(2, gp2);
        board.addPiece(3, gp2);
        board.addPiece(4, gp2);
        assertEquals(board.getYellowWins(), 0);
        assertTrue(board.checkGameOver());
        assertEquals(board.getYellowWins(), 1);
        assertTrue(board.isClear());
    }

    // REQUIRES: board is 7X7
    // EFFECTS: creates a tie game (full board with no four in a row)
    public void createTieBoard(GameBoard board) {
        createTieBoardCol1(board, 1);
        createTieBoardCol1(board, 2);
        createTieBoardCol2(board, 3);
        createTieBoardCol3(board, 4);
        createTieBoardCol2(board, 5);
        createTieBoardCol1(board, 6);
        createTieBoardCol1(board, 7);
    }

    // REQUIRES: board is 7X7
    // EFFECTS: creates a column of tie game (full board with no four in a row)
    public void createTieBoardCol1(GameBoard board, int column) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (i == 3) {
                board.addPiece(column, gp1);
            }
            else {
                board.addPiece(column, gp2);
            }
        }
    }

    // REQUIRES: board is 7X7
    // EFFECTS: creates a column of tie game (full board with no four in a row)
    public void createTieBoardCol2(GameBoard board, int column) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (i >= 2 & i <= 4) {
                board.addPiece(column, gp1);
            }
            else {
                board.addPiece(column, gp2);
            }
        }
    }

    // REQUIRES: board is 7X7
    // EFFECTS: creates a column of tie game (full board with no four in a row)
    public void createTieBoardCol3(GameBoard board, int column) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            if (i != 3) {
                board.addPiece(column, gp1);
            }
            else {
                board.addPiece(column, gp2);
            }
        }
    }



    @Test
    public void testCheckGameOverFull(){
        createTieBoard(board);
        assertEquals(board.getTies(), 0);
        assertTrue(board.isFull());
        assertTrue(board.checkGameOver());
        assertEquals(board.getTies(), 1);
        assertTrue(board.isClear());
    }

    @Test
    public void testCheckWin(){
        
    }



}