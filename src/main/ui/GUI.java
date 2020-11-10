package ui;


import model.GameBoard;
import model.GamePiece;
import persistance.JsonReader;
import persistance.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// Plays game of connect four in a popup. IIt is a 2 player head-to-head game, where players take turns
// adding game pieces to the board, with the goal of having four of your own pieces in a row
// along a horizontal, vertical, or diagonal
public class GUI extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private GameBoard board;
    private GamePiece red;
    private GamePiece yellow;
    private boolean playing;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final String JSON_STORE = "./data/gameboard.json";
    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final boolean RED_TURN = true;
    public static final boolean YELLOW_TURN = false;
    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;

    public GUI() {
        super("Connect Four");
        initializeFields();
        initializeGraphics();
        //initializeSound();
        initializeInteraction();
    }

    // MODIFIES: this
    // EFFECTS: creates mouse listener
    private void initializeInteraction() {

    }

    // MODIFIES: this
    // EFFECTS: initializes graphics
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields
    private void initializeFields() {
        board = new GameBoard();
        red = new GamePiece(RED);
        yellow = new GamePiece(YELLOW);
        playing = true;
        board.setTurn(RED_TURN);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

}
