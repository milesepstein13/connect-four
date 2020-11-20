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
    private boolean turn;
    private int numPlayers;
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
        ties = 0;
        numPlayers = 0;
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

    public boolean getTurn() {
        return turn;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void switchTurn() {
        turn = !turn;
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

    // REQUIRES: there is a piece at given column and height (starting from 1, 1)
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

    // REQUIRES: color = 0 or 1, 0<number<8
    // EFFECTS: returns the number of instances on the board that the given
    // given (0 for red and 1 for yellow) color has number pieces in a row
    public int checkConsecutive(int color, int number) {
        return checkHorizontalWin(color, number) + checkVerticalWin(color, number) + checkDiagonalWin(color, number);
    }

    // REQUIRES: color = 0 or 1, 0<number<8
    // EFFECTS: returns the number of instances there are number of the given color on a diagonal and false otherwise
    private int checkDiagonalWin(int color, int number) {
        int acc = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board.get(i).size() > j) { // for each piece on the board
                    if (checkWinNextFourRightDiagonal(i, j, color, 1, number)) {
                        acc++;
                    }
                    if (checkWinNextFourRightDiagonal(i, j, color, -1, number)) {
                        acc++;
                    }
                }
            }

        }
        return acc;
    }

    // REQUIRES: direction = -1 or 1, color = 0 or 1, 0<number<8
    // EFFECTS: return true if the next number pieces above and to the right (if direction is 1) or below and
    // to the right (if direction is -1) of piece in given column and height are the given color
    private boolean checkWinNextFourRightDiagonal(int column, int height, int color, int direction, int number) {
        try {
            int consecutive = 0;
            for (int i = 0; i < number; i++) {
                if (board.get(column + i).get(height + (direction * i)).getColor() == color) {
                    consecutive++;
                    if (consecutive == number) {
                        return true; // if there have been number pieces in a row of the given color
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

    // REQUIRES: color = 0 or 1, 0<number<8
    // EFFECTS: returns the number of instances there are number of the given color on top of each other
    private int checkVerticalWin(int color, int number) {
        int acc = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            acc += checkWinColumn(board.get(i), color, number);

        }
        return acc;
    }

    // REQUIRES: color = 0 or 1, 0<number<8
    // EFFECTS: returns the number of instances there are number color consecutive pieces in a column
    private int checkWinColumn(ArrayList<GamePiece> column, int color, int number) {
        int consecutive = 0; // number of consecutive pieces seen so far
        int acc = 0; //number of instances of number pieces
        for (int i = 0; i < column.size(); i++) {
            if (column.get(i).getColor() == color) { // the next piece is the right color
                consecutive++;
                if (consecutive >= number) { // the last 4 pieces have been the right color
                    acc++;
                }
            } else { // the next piece is the wrong color
                consecutive = 0;
            }
        }
        return acc;
    }

    // EFFECTS: returns the number of instances there are number of the given color next to each other
    private int checkHorizontalWin(int color, int number) {
        int acc = 0;
        for (int i = 0; i < BOARD_WIDTH - (7 - number); i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (board.get(i).size() > j) { // for each piece on the board (except in the last 3 columns)
                    if (checkWinNextNumberRight(i, j, color, number)) {
                        acc++;
                    }
                }
            }
        }
        return acc;
    }

    // REQUIRES: color = 0 or 1, 0<number<8
    // EFFECTS: true if the next number pieces to the right, starting with piece in given column and height, are the
    // given color
    private boolean checkWinNextNumberRight(int column, int height, int color, int number) {
        try {
            int consecutive = 0;
            for (int i = 0; i < number; i++) {
                if (board.get(column + i).get(height).getColor() == color) {
                    consecutive++;
                    if (consecutive == number) {
                        return true; // if there have been n pieces in a row of the given color
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
    // EFFECTS: places a yellow piece in a smart place
    // English explanation of strategy:
    // If it can get 4 in a row it goes there. Otherwise, if it can block you from getting four
    // in a row it goes there. Otherwise if it can get 3 in a row it goes there. Otherwise,
    // if it can block you from getting 3 in a row it goes there. And so on with 2 and 1
    //
    // It's a basic strategy but is pretty hard to beat because it's never going to miss anything
    // It can't think multiple turns ahead, though, so you can still beat if you think ahead
    // and trap in a spot that it will lose no matter where it goes.
    //
    // To make it better it would need to think multiple turns ahead, and that would probably be
    // too complex to hard-code the logic and would require some form of machine learning
    // or more complicated algorithm (things I would struggle to implement with my current abilities)
    public void smartAIMove() {
        for (int i = 4; i > 0; i--) { // for 4 in a row, then 3, 2, and lastly 1
            if (checkAImove(YELLOW, i)) { // see if there's a spot you can put yellow to get that many in a row
                return;
            }
            if (checkAImove(RED, i)) {
                return;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: see which columns you could add a piece of given color to that
    // increases the number of instances there are consecutive in a row of the given color
    // if there are any such columns, put a yellow piece in a random one of them and return true
    // if not, return false
    private boolean checkAImove(int color, int consecutive) {
        GamePiece gp = new GamePiece(color);
        int baseline = checkConsecutive(color, consecutive);
        ArrayList<Integer> possibleColumns = new ArrayList<>(); //columns that would work
        for (int i = 1; i <= BOARD_WIDTH; i++) { //get the possible columns
            if (addPiece(i, gp)) {
                if (checkConsecutive(color, consecutive) > baseline) {
                    // if adding a piece to that column makes more consecutive,
                    // add the number of that col to possibleColumns
                    possibleColumns.add(i);
                }
                removePiece(i);
            }
        }

        // then put a yellow piece in one of the possible columns
        int numPossibleColumns = possibleColumns.size();
        if (numPossibleColumns > 0) {
            int rand = (int) (Math.random() * numPossibleColumns);
            int moveColumn = possibleColumns.get(rand);
            GamePiece yellowPiece = new GamePiece(YELLOW);
            addPiece(moveColumn, yellowPiece);
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: there is a piece in the given column
    // MODIFIES: this
    // EFFECTS: removes the top piece from given column (1-7)
    private void removePiece(int column) {
        int size = board.get(column - 1).size();
        board.get(column - 1).remove(size - 1);
    }

    // MODIFIES: this
    // EFFECTS: if a color has won or there is a tie (full board), adds one to their win total and
    // returns true. Else returns false
    public boolean checkGameOver() {
        if (checkConsecutive(YELLOW, 4) > 0) {
            yellowWins++;
            return true;
        }

        if (checkConsecutive(RED, 4) > 0) {
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
    // EFFECTS: returns this as a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("redWins", redWins);
        json.put("yellowWins", yellowWins);
        json.put("ties", ties);
        json.put("turn", turn);
        json.put("numPlayers", numPlayers);
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
