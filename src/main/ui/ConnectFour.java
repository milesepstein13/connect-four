package ui;

import model.GameBoard;
import model.GamePiece;
import persistance.JsonReader;
import persistance.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

// Plays game of connect four in the console. IIt is a 2 player head-to-head game, where players take turns
// adding game pieces to the board, with the goal of having four of your own pieces in a row
// along a horizontal, vertical, or diagonal
public class ConnectFour {

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


    // runs the application
    public ConnectFour() {
        connectFourGame();
    }

    // MODIFIES: this
    // EFFECTS: plays game
    private void connectFourGame() {
        init();
        while (playing) {
            while (!board.checkGameOver() && playing) {
                nextAction();
                sayWin();
            }
            board.clear();
            printWins();
            playing = true;
            board.setNumPlayers(0);
            board.setTurn(RED_TURN);
            askPlayAgain();

        }
        System.out.println("Thanks for playing!");
    }

    // EFFECTS: if a color has won, says so
    private void sayWin() {
        if (board.checkConsecutive(RED, 4) > 0) {
            System.out.println("Red Wins!");

        }
        if (board.checkConsecutive(YELLOW, 4) > 0) {
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
    // EFFECTS: gives player option to load board, save, quit, or make next move
    private void nextAction() {
        if (board.getNumPlayers() == 0) {
            askNumPlayers();
        }
        printBoard(board);
        boolean ready = false;
        while (!ready) {
            System.out.println("Options: \n - \"s\" to save the board\n - \"l\" to save load board");
            System.out.println(" - \"q\" to quit\n - \"m\" to make the next move");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            ready = true;
            if (answer.equals("s")) {
                saveGameBoard();
            } else if (answer.equals("l")) {
                loadGameBoard();
            } else if (answer.equals("q")) {
                playing = false;
            } else if (answer.equals("m")) {
                makeNextMove();
            } else {
                ready = false;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: makes the next move, either by a player or by the ai if it's a one player game and it's yellow's turn
    private void makeNextMove() {
        if (board.getTurn() == RED_TURN) {
            printBoard(board);
            makeNextMove(red);
        } else {
            if (board.getNumPlayers() == 2) {
                printBoard(board);
                makeNextMove(yellow);
            } else {
                board.smartAIMove();
                System.out.println("AI has made its move!");


            }
        }
        board.switchTurn();
    }

    // REQUIRES: color is RED or YELLOW
    // MODIFIES: this
    // EFFECTS: asks the user for a column number until they input a valid one
    // and puts a piece of given color in that column
    private void makeNextMove(GamePiece gp) {
        boolean done = false;
        while (!done) {
            String ask;
            if (board.getTurn() == RED_TURN) {
                ask = "Red, ";
            } else {
                ask = "Yellow, ";
            }
            System.out.println(ask + "enter the column number (1-7) where you would like to place your piece.");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            done = processAnswer(gp, answer);
        }
    }

    // MODIFIES: this
    // EFFECTS: saves or loads the board or places the game piece based on the user's answer
    private boolean processAnswer(GamePiece gp, String answer) {
        try {
            int column = Integer.parseInt(answer);
            if (column > 0 & column <= BOARD_WIDTH) {
                if (board.addPiece(column, gp)) {
                    return true;
                } else {
                    System.out.println("That column is full!");
                }
            } else {
                System.out.println("That column doesn't exist!");
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
            String numPlayers = scanner.nextLine();
            if (numPlayers.equals("1") | numPlayers.equals("2")) {
                ready = true;
                board.setNumPlayers(Integer.parseInt(numPlayers));
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
        board.setTurn(RED_TURN);
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

    // MODIFIES: boardString
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
            System.out.println("Board Saved!");
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
            System.out.println("Board Loaded! Here it is:");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
