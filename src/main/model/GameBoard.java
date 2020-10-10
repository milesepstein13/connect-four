package model;

import java.util.ArrayList;

public class GameBoard {

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;
    public static final int RED = 0;
    public static final int YELLOW = 1;

    private ArrayList<ArrayList<GamePiece>> board;

    // MODIFIES: this
    // EFFECTS: creates an empty board of width BOARD_WIDTH and height BOARD_HEIGHT
    public GameBoard() {
        board = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            ArrayList<GamePiece> emptyColumn = new ArrayList<>();
            board.add(emptyColumn);
        }
    }

    // MODIFIES: this
    // EFFECTS: if the given column (between one and width) is not full,
    // adds given game piece to given column and returns true. Else returns false
    public boolean addPiece(int column, GamePiece gp) {
        if (board.get(column - 1).size() == BOARD_HEIGHT) {
            return false;
        } else {
            board.get(column - 1).add(gp);
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: removes all pieces from the board
    public void clear() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            board.get(i).clear();
        }
    }

    // EFFECTS: returns true if there are 4 in a row of the
    // given (0 for red and 1 for yellow) color and false otherwise
    public boolean checkWin(int color) {
        return checkHorizontalWin(color) | checkVerticalWin(color) | checkDiagonalWin(color);
    }

    // EFFECTS: returns true if there are 4 of the given color on a diagonal and false otherwise
    private boolean checkDiagonalWin(int color) {
        return true; //stub
    }

    // EFFECTS: returns true if there are 4 of the given color on top of each other
    private boolean checkVerticalWin(int color) {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (checkWinColumn(board.get(i), color)) {
                return true;
            }
        }
        return false;

    }

    // EFFECTS: returns true the list of pieces has 4 in a row of the given color
    private boolean checkWinColumn(ArrayList<GamePiece> column, int color) {
        int consecutive = 0; // number of consecutive pieces seen so far
        for (int i = 0; i < column.size(); i++) {
            if (column.get(i).getColor() == color) { // the next piece is the right color
                consecutive++;
                if (consecutive == 4) { // the last 4 pieces have been the right color
                    return true;
                }
            } else { // the next piece is the wrong color
                consecutive = 0;
            }
        }
        return false;
    }

    // EFFECTS: returns true if there are 4 of the given color next to each other
    private boolean checkHorizontalWin(int color) {
        return true; //stub
    }


}
