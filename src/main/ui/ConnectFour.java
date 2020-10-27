package ui;

import model.GameBoard;
import model.GamePiece;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// Plays game of connect four. It is a 2 player head-to-head game, where players take turns
// adding game pieces to the board, with the goal of having four of your own pieces in a row
// along a horizontal, vertical, or diagonal
public class ConnectFour {

    private GameBoard board;
    private GamePiece red;
    private GamePiece yellow;
    private boolean playing;
    private String numPlayers;
    int nextTurn;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final String JSON_STORE = "./data/gameboard.json";
    public static final int RED = 0;
    public static final int YELLOW = 1;

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;


    // runs the application
    public ConnectFour() {
        connectFourGame();
    }

    // MODIFIES: this
    // EFFECTS: plays game
    private void connectFourGame() {
        init();
        while (playing) {
            askNumPlayers();
            printBoard(board);
            while (!board.checkGameOver()) {
                makeNextMove();
                printBoard(board);
                sayWin();
            }
            board.clear();
            printWins();
            askPlayAgain();

        }
        System.out.println("Thanks for playing!");
    }

    // EFFECTS: if a color has won, says so
    private void sayWin() {
        if (board.checkWin(RED)) {
            System.out.println("Red Wins!");

        }
        if (board.checkWin(YELLOW)) {
            System.out.println("Yellow Wins!");

        }
    }

    // EFFECTS: Prints the number of wins and ties for each player
    private void printWins() {
        System.out.println("Red has " + Integer.toString(board.getRedWins()) + " wins!");
        System.out.println("Yellow has " + Integer.toString(board.getYellowWins()) + " wins!");
        System.out.println("There have been " + Integer.toString(board.getTies()) + " ties!");
    }

    // MODIFIES: this
    // EFFECTS: If it is red's turn, lets red make their move and sets next turn to YELLOW.
    // If it is yellow's turn and 1 player game, makes ai move and if 2 player game lets yellow make their tur
    // then sets nextTurn to RED
    private void makeNextMove() { //add load and save here
        if (nextTurn == RED) {
            makeNextMove(red);
            nextTurn = YELLOW;
        } else {
            if (numPlayers.equals("2")) {
                makeNextMove(yellow);
            } else {
                board.aiMove(yellow);
                System.out.println("AI has made its move!");
            }
            nextTurn = RED;
        }
    }

    // REQUIRES: color is RED or YELLOW
    // MODIFIES: this
    // EFFECTS: asks the user for a column number until they input a valid one
    // and puts a piece of given color in that column
    private void makeNextMove(GamePiece gp) {
        boolean done = false;
        while (!done) {
            String ask;
            if (nextTurn == RED) {
                ask = "Red, ";
            } else {
                ask = "Yellow, ";
            }
            System.out.println(ask + "enter the column number (1-7) where you would like to place your piece.");
            System.out.println("Enter \"s\" to save or \"l\" to load");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            done = processAnswer(gp, answer);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves or loads the board or places the game piece based on the user's answer
    private boolean processAnswer(GamePiece gp, String answer) {
        if (answer.equals("s")) {
            saveGameBoard();
            System.out.println("Board Saved!");
        }
        if (answer.equals("l")) {
            loadGameBoard();
            System.out.println("Board Loaded!");
        }
        try {
            int column = Integer.parseInt(answer);
            if (column > 0 & column <= BOARD_WIDTH) {
                if (board.addPiece(column, gp)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Asks the user to play again and sets playing to false if they don't want to
    private void askPlayAgain() {
        System.out.println("Good Game! Enter \"yes\" to play again and anything else to quit.");
        Scanner scanner = new Scanner(System.in);
        String playAgain = scanner.nextLine();
        if (!playAgain.equals("yes")) {
            playing = false;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets numPlayers to a string "1" or "2" as given by the user
    private void askNumPlayers() {
        System.out.println("Welcome to Connect Four! Enter the number of players (\"1\" or \"2\").");
        boolean ready = false;
        while (!ready) {
            Scanner scanner = new Scanner(System.in);
            numPlayers = scanner.nextLine();
            if (numPlayers.equals("1") | numPlayers.equals("2")) {
                ready = true;
            } else {
                System.out.println("Enter 1 or 2");
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: initializes board
    private void init() {
        board = new GameBoard();
        red = new GamePiece(RED);
        yellow = new GamePiece(YELLOW);
        playing = true;
        nextTurn = RED;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: print game board to console
    private void printBoard(GameBoard gb) {
        String boardString = "_________________\n| 1 2 3 4 5 6 7 |\n";
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            boardString += "|";
            for (int j = 0; j < BOARD_WIDTH; j++) {
                boardString = addPieceToString(boardString, gb, i, j);
            }
            boardString += " |\n";

        }
        boardString += "-----------------";
        System.out.println(boardString);
    }

    // REQUIRES: i and j are between 0 and board height/width respectively
    // EFFECTS: adds a "Y" to board string if the piece in row i (from top) and column j is yellow,
    // "R" if red, and a space if it doesn't exist
    private String addPieceToString(String boardString, GameBoard gb, int i, int j) {
        if (gb.getColumn(j + 1).size() > BOARD_HEIGHT - i - 1) {
            if (gb.getColumn(j + 1).get(BOARD_HEIGHT - i - 1).isRed()) {
                boardString += " R";
            } else {
                boardString += " Y";
            }
        } else {
            boardString += "  ";
        }
        return boardString;
    }

    // EFFECTS: saves the gameboard to file
    // Source: Json serialization demo
    private void saveGameBoard() {
        try {
            jsonWriter.open();
            jsonWriter.write(board);
            jsonWriter.close();
            System.out.println("Saved board to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads gameboard from file
    // Source: Json serialization demo
    private void loadGameBoard() {
        try {
            board = jsonReader.read();
            System.out.println("Loaded board from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
