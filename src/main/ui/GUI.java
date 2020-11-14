package ui;


import model.GameBoard;
import model.GamePiece;
import persistance.JsonReader;
import persistance.JsonWriter;
import ui.display.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Plays game of connect four in a popup. IIt is a 2 player head-to-head game, where players take turns
// adding game pieces to the board, with the goal of having four of your own pieces in a row
// along a horizontal, vertical, or diagonal
public class GUI extends JFrame implements ActionListener {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;

    private GameBoard board;
    private GamePiece red;
    private GamePiece yellow;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //Visisble pieces
    private ArrayList<ArrayList<Spot>> spots;
    private ArrayList<DropperButton> droppers;
    private LoadButton loadButton;
    private SaveButton saveButton;
    private SwitchPlayersButton switchPlayersButton;
    private InfoDisplay infoDisplay;

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
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        addVisuals();

    }

    private void addVisuals() {
        setLayout(new GridBagLayout());
        addDisplay(loadButton, 7, 0);
        addDisplay(saveButton, 7, 1);
        addDisplay(switchPlayersButton, 7, 2);
        addDisplay(infoDisplay, 7, 3);
        for (int i = 0; i < BOARD_WIDTH; i++) {
            addDisplay(droppers.get(i), i,0);
        }
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                addDisplay(spots.get(i).get(j), i, 7 - j);
            }
        }
    }

    public void addDisplay(JComponent component, int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = x;
        c.gridy = y;
        add(component, c);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields
    private void initializeFields() {
        board = new GameBoard();
        board.setNumPlayers(1);
        red = new GamePiece(RED);
        yellow = new GamePiece(YELLOW);
        board.setTurn(RED_TURN);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        droppers = new ArrayList<>();
        initializeSpots();
        initializeButtons();
        infoDisplay = new InfoDisplay();
    }

    private void initializeSpots() {
        spots = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            ArrayList<Spot> column = new ArrayList<>();
            for (int j = 0; j < GameBoard.BOARD_HEIGHT; j++) {
                String columnName = Integer.toString(i);
                String rowName = Integer.toString(j);
                Spot s = new Spot();
                column.add(s);
            }
            spots.add(column);
        }
    }

    private void initializeButtons() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            String columnString = Integer.toString(i + 1);
            DropperButton dropper = new DropperButton(columnString, this);
            dropper.setActionCommand("dropper " + Integer.toString(i));
            dropper.addActionListener(this);
            droppers.add(dropper);
        }
        loadButton = new LoadButton(this);
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);
        saveButton = new SaveButton(this);
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        switchPlayersButton = new SwitchPlayersButton(this);
        switchPlayersButton.setActionCommand("switch");
        switchPlayersButton.addActionListener(this);
    }

    public InfoDisplay getInfoDisplay() {
        return infoDisplay;
    }

    private void updateDisplay() {
        updateInfoDisplay();
        updateBoardDisplay();
    }

    private void updateBoardDisplay() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < board.getColumn(i + 1).size(); j++) {
                if (board.getGamePiece(i + 1, j + 1).isRed()) {
                    spots.get(i).get(j).setText("RED");
                } else {
                    spots.get(i).get(j).setText("YELLOW");
                }
            }
        }
    }

    private void updateInfoDisplay() {
        String displayString = "Red Wins: " + board.getRedWins() + "\nYellow Wins: " + board.getYellowWins();
        displayString = displayString + "\nTies: " + board.getTies() + "\nNumber of Players: " + board.getNumPlayers();
        if (board.getTurn()) {
            displayString = displayString + "\nTurn: Red";
        } else {
            displayString = displayString + "\nTurn: Yellow";
        }
        infoDisplay.setText(displayString);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("load")) {
            infoDisplay.setText("this is how event handling works!");
        } else {
            updateDisplay();
        }
    }
}
