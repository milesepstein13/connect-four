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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

// Plays game of connect four in a popup. IIt is a 2 player head-to-head game, where players take turns
// adding game pieces to the board, with the goal of having four of your own pieces in a row
// along a horizontal, vertical, or diagonal
// Source: example code on project page, SimpleDrawingPlayer
public class GUI extends JFrame implements ActionListener {



    private GameBoard board;
    private GamePiece red;
    private GamePiece yellow;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    //Visible pieces
    private ArrayList<ArrayList<Spot>> spots;
    private ArrayList<DropperButton> droppers;
    private LoadButton loadButton;
    private SaveButton saveButton;
    private SwitchPlayersButton switchPlayersButton;
    private InfoDisplay infoDisplay;

    public static final String JSON_STORE = "./data/gameboard.json";
    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final boolean RED_TURN = true;
    public static final boolean YELLOW_TURN = false;
    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    public static final int BLANK = -1;

    // creates the game
    public GUI() {
        super("Connect Four");
        initializeFields();
        initializeGraphics();
    }


    // MODIFIES: this
    // EFFECTS: initializes graphics
    private void initializeGraphics() {
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        addVisuals();
        updateDisplay();
    }

    // MODIFIES: this
    // EFFECTS: adds all Jcomponenets to the display grid at appropriate positions
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

    // MODIFIES: this
    // EFFECTS: adds an individual Jcomponent to the gridbag display
    // at given coordinates in the grid
    public void addDisplay(JComponent component, int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = x;
        c.gridy = y;
        c.weightx = 1;
        c.weighty = 1;
        if (component == infoDisplay) {
            c.gridheight = 5;
        }
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

    // MODIFIES: this
    // EFFECTS: creates the appropriate number of spots (for pieces to go visually)
    // and adds them to an array list of array lists
    private void initializeSpots() {
        spots = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            ArrayList<Spot> column = new ArrayList<>();
            for (int j = 0; j < GameBoard.BOARD_HEIGHT; j++) {
                Spot s = new Spot(this);
                column.add(s);
            }
            spots.add(column);
        }
    }

    // MODIFIES: this
    // EFFECTS: iniitializes all buttons as fields
    private void initializeButtons() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            String columnString = Integer.toString(i + 1);
            DropperButton dropper = new DropperButton(columnString);
            dropper.setActionCommand(Integer.toString(i));
            dropper.addActionListener(this);
            dropper.setSize(50, 50);
            droppers.add(dropper);
        }
        loadButton = new LoadButton();
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);
        saveButton = new SaveButton();
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        switchPlayersButton = new SwitchPlayersButton();
        switchPlayersButton.setActionCommand("switch");
        switchPlayersButton.addActionListener(this);
    }

    public InfoDisplay getInfoDisplay() {
        return infoDisplay;
    }

    // MODIFIES: this
    // EFFECTS: visually updates the visual display (game pieces and text info)
    private void updateDisplay() {
        updateInfoDisplay();
        updateBoardDisplay();
    }

    // MODIFIES: this
    // EFFECTS: visually updates all spots to be red if the corresponding piece of board is red,
    // yellow if yellow, and grey if blank
    private void updateBoardDisplay() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (j < board.getColumn(i + 1).size()) {
                    // for each piece on the board, make it the color of the corresponding
                    // piece on board
                    if (board.getGamePiece(i + 1, j + 1).isRed()) {
                        spots.get(i).get(j).update(RED);
                    } else {
                        spots.get(i).get(j).update(YELLOW);
                    }
                } else {
                    spots.get(i).get(j).update(BLANK);
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates infoDisplay to state the number of wins, ties, number of players, and whose turn it is
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
    // MODIFIES: this
    // EFFECTS: triggered when a button is pressed. Loads board, saves board, switches the number of
    // players, or attempts to drop a piece in a column (and perform consequential behavior) when the
    // corresponding button is pressed
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("load")) {
            loadBoard();
        } else if (e.getActionCommand().equals("save")) {
            saveBoard();
        } else if (e.getActionCommand().equals("switch")) {
            switchNumPlayers();
        } else { //a column button was pressed
            if (playerMakeMove(e.getActionCommand())) {
                updateDisplay();
                process();
            } else { // beeps if you can't move there
                Toolkit.getDefaultToolkit().beep();
            }
        }
        updateDisplay();
    }

    // MODIFIES: this
    // EFFECTS: performs post-move behavior. If the game is over, update accordingly, pause,
    // beep, and reset the board. If it's the ai's turn to go, the ai goes and check for win again
    private void process() {
        if (board.checkGameOver()) {
            endGame();
        } else if (!board.getTurn() && board.getNumPlayers() == 1) {
            board.smartAIMove();
            board.switchTurn();
            if (board.checkGameOver()) {
                endGame();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: pauses, beeps, and resets the board
    private void endGame() {
        //beeps and pauses when you win
        Toolkit.getDefaultToolkit().beep();
        try {
            repaint();
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        board.clear();
        board.setTurn(RED_TURN);
    }

    // REQUIRES: actionCommand is a number 1-7
    // MODIFIES: this
    // EFFECTS: adds a piece of the color whose turn it currently is to the column
    // given by actionCommond and switches whose turn it is. If the column is full, does nothing
    private Boolean playerMakeMove(String actionCommand) {
        int column = Integer.parseInt(actionCommand) + 1;
        GamePiece gp;
        if (board.getTurn()) {
            gp = red;
        } else {
            gp = yellow;
        }
        if (board.addPiece(column, gp)) {
            board.switchTurn();
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: switches the number of players (1 to 2 or vice-versa).
    // If it switches to one player and it's yellow turn, the ai goes
    private void switchNumPlayers() {
        if (board.getNumPlayers() == 1) {
            board.setNumPlayers(2);
        } else {
            board.setNumPlayers(1);
            if (!board.getTurn() && board.getNumPlayers() == 1) {
                board.smartAIMove();
                board.switchTurn();
                if (board.checkGameOver()) {
                    board.clear();
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: loads a board from a json file
    private void loadBoard() {
        try {
            board = jsonReader.read();
            //System.out.println("Loaded board from " + JSON_STORE);
            //System.out.println("Board Loaded!" + board.getColumn(1).size());
        } catch (IOException ex) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Saves the current board to a json file
    private void saveBoard() {
        try {
            jsonWriter.open();
            jsonWriter.write(board);
            jsonWriter.close();
            //System.out.println("Saved board to " + JSON_STORE);
            //System.out.println("Board Saved!" + board.getColumn(1).size());
        } catch (FileNotFoundException exc) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}
