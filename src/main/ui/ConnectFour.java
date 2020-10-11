package ui;

import model.GameBoard;
import model.GamePiece;

import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class ConnectFour {

    private GameBoard board;
    private GamePiece red;
    private GamePiece yellow;
    private boolean playing;
    String numPlayers;
    int nextTurn;

    public static final int RED = 0;
    public static final int YELLOW = 1;

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;

    private int players;

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
                if (board.checkWin(RED)) {
                    System.out.println("Red Wins!");
                }
                if (board.checkWin(YELLOW)) {
                    System.out.println("YELLOW Wins!");
                }
            }
            board.clear();
            askPlayAgain();

        }
        System.out.println("Thanks for playing!");
    }

    //
    private void makeNextMove() {
        if (nextTurn == RED) {
            makeNextMove(red);
            nextTurn = YELLOW;
        } else {
            if (numPlayers.equals("2")) {
                makeNextMove(yellow);
            } else {
                board.aiMove(yellow);
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
            System.out.println("Enter the column number (1-7) where you would like to place your piece");
            Scanner scanner = new Scanner(System.in);
            int column = parseInt(scanner.nextLine());
            if (column > 0 & column <= BOARD_WIDTH) {
                if (board.addPiece(column, gp)) {
                    done = true;
                }
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: asks the user to play again and sets playing to false if they don't want to
    private void askPlayAgain() {
        System.out.println("Red has " + Integer.toString(board.getRedWins()) + " wins!");
        System.out.println("Yellow has " + Integer.toString(board.getYellowWins()) + " wins!");
        System.out.println("There have been " + Integer.toString(board.getTies()) + " ties!");
        System.out.println("Good Game! Enter \"yes\" to play again and anything else to quit.");
        Scanner scanner = new Scanner(System.in);
        String playAgain = scanner.nextLine();
        if (!playAgain.equals("yes")) {
            playing = false;
        }
    }

    // MODIFIES: this
    // EFFECTS: gets the number of players from the user
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
    }

    // EFFECTS: print game board to console
    private void printBoard(GameBoard gb) {
        String boardString = "";
        boardString += "_________\n";
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            boardString += "|";
            for (int j = 0; j < BOARD_WIDTH; j++) {
                boardString = addPieceToString(boardString, gb, i, j);
            }
            boardString += "|\n";

        }
        boardString += "---------";
        System.out.println(boardString);
    }

    // REQUIRES: i and j are between 0 and board height/width respectively
    // EFFECTS: adds a "Y" to board string if the piece in row i (from top) and column j is yellow,
    // "R" if red, and a space if it doesn't exist
    private String addPieceToString(String boardString, GameBoard gb, int i, int j) {
        if (gb.getColumn(j + 1).size() > BOARD_HEIGHT - i - 1) {
            if (gb.getColumn(j + 1).get(BOARD_HEIGHT - i - 1).isRed()) {
                boardString += "R";
            } else {
                boardString += "Y";
            }
        } else {
            boardString += " ";
        }
        return boardString;
    }
}
