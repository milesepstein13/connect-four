package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;

// Represents a board for a game of connect 4. It's a 7X7 grid that you can add red or yellow
// pieces to. When you add a piece it falls to the bottom of the column.
public class GameBoard implements Writable {

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;
    public static final int RED = 0;
    public static final int YELLOW = 1;

    private int redWins;
    private int yellowWins;
    private int ties;
    private ArrayList<ArrayList<GamePiece>> board;

    // MODIFIES: this
    // EFFECTS: creates an empty board of width BOARD_WIDTH and height BOARD_HEIGHT
    public GameBoard() {
        board = new ArrayList<>();
        for (int i = 0; i < BOARD_WIDTH; i++) {
            ArrayList<GamePiece> emptyColumn = new ArrayList<>();
            board.add(emptyColumn);
        }
        redWins = 0;
        yellowWins = 0;
    }

    public int getRedWins() {
        return redWins;
    }

    public int getYellowWins() {
        return yellowWins;
    }

    public int getTies() {
        return ties;
    }

    public void setRedWins(int redWins) {
        this.redWins = redWins;
    }

    public void setYellowWins(int yellowWins) {
        this.yellowWins = yellowWins;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    // REQUIRES: there is a piece at the given location
    public GamePiece getGamePiece(int column, int height) {
        return board.get(column - 1).get(height - 1);
    }

    public ArrayList<GamePiece> getColumn(int column) {
        return board.get(column - 1);
    }

    // REQUIRES: column is between 1 and BOARD_WIDTH
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

    // EFFECTS: returns true if there are no pieces on the board and false otherwise
    public boolean isClear() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (!board.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: returns true if the board is full and false otherwise
    public boolean isFull() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (board.get(i).size() != BOARD_HEIGHT) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: returns true if there are 4 in a row of the
    // given (0 for red and 1 for yellow) color and false otherwise
    public boolean checkWin(int color) {
        return checkHorizontalWin(color) | checkVerticalWin(color) | checkDiagonalWin(color);
    }

    // EFFECTS: returns true if there are 4 of the given color on a diagonal and false otherwise
    private boolean checkDiagonalWin(int color) {
        for (int i = 0; i < BOARD_WIDTH - 3; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board.get(i).size() > j) { // for each piece on the board (except in the last 3 columns)
                    if (checkWinNextFourRightDiagonal(i, j, color, 1)) {
                        return true;
                    }
                    if (checkWinNextFourRightDiagonal(i, j, color, -1)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    // REQUIRES: direction = -1 or 1
    // EFFECTS: return true if the next four pieces above and to the right (if direction is 1) or below and
    // to the right (if direction is -1) of piece in given column and height are the given color
    private boolean checkWinNextFourRightDiagonal(int column, int height, int color, int direction) {
        try {
            int consecutive = 0;
            for (int i = 0; i < 4; i++) {
                if (board.get(column + i).get(height + (direction * i)).getColor() == color) {
                    consecutive++;
                    if (consecutive == 4) {
                        return true; // if there have been 4 pieces in a row of the given color
                    }
                } else {
                    //return false; //if you run into a piece of the wrong color.
                    // Commenting this line out doesn't change the functionality.
                    // Commenting it out makes the program less efficient
                    // (because it keep checking even after running into the wrong piece) but ensures
                    // complete code coverage. If this line is uncommented, the next return statement
                    // is never used, so code coverage is less than 100%, even though that line is needed
                    // for the code to compile.
                }
            }
            return false;
        } catch (Exception e) { // if the four to the right include an empty spot (would give an array out of bounds)
            return false;
        }
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
        for (int i = 0; i < BOARD_WIDTH - 3; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board.get(i).size() > j) { // for each piece on the board (except in the last 3 columns)
                    if (checkWinNextFourRight(i, j, color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // EFFECTS: true if the next four pieces to the right, starting with piece in given column and height, are the
    // given color
    private boolean checkWinNextFourRight(int column, int height, int color) {
        try {
            int consecutive = 0;
            for (int i = 0; i < 4; i++) {
                if (board.get(column + i).get(height).getColor() == color) {
                    consecutive++;
                    if (consecutive == 4) {
                        return true; // if there have been 4 pieces in a row of the given color
                    }
                } else {
                    //return false; //if you run into a piece of the wrong color.
                    // Commenting this line out doesn't change the functionality.
                    // Commenting it out makes the program less efficient
                    // (because it keep checking even after running into the wrong piece) but ensures
                    // complete code coverage. If this line is uncommented, the next return statement
                    // is never used, so code coverage is less than 100%, even though that line is needed
                    // for the code to compile.
                }
            }
            return false;
        } catch (Exception e) { // if the four to the right include an empty spot (would give an array out of bounds)
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: places given piece in a random not full column
    public void aiMove(GamePiece gp) {
        boolean done = false;
        while (!done) {
            int rand = (int) (Math.random() * 7 + 1);
            if (addPiece(rand, gp)) {
                done = true;
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: if a color has won or there is a tie (full board), adds one to their win total and
    // returns true. Else returns false
    public boolean checkGameOver() {
        if (checkWin(YELLOW)) {
            yellowWins++;
            return true;
        }

        if (checkWin(RED)) {
            redWins++;
            return true;
        }

        if (isFull()) {
            ties++;
            return true;
        }

        return false;
    }


    @Override
    // Source: Json Serialization Demo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("redWins", redWins);
        json.put("yellowWins", yellowWins);
        json.put("ties", ties);
        json.put("board", boardToJson());
        return json;
    }

    // EFFECTS: returns JSONArray of JSONArrays of pieces (columns)
    // Source: Json Serialization Demo
    private JSONArray boardToJson() {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < BOARD_WIDTH; i++) {
            jsonArray.put(columnToJson(i));
        }
        return jsonArray;
    }

    // EFFECTS: returns column i as JSONArray of pieces
    private JSONArray columnToJson(int i) {
        JSONArray jsonArray = new JSONArray();

        for (GamePiece gp : board.get(i)) {
            jsonArray.put(gp.toJson());
        }

        return jsonArray;
    }
}
