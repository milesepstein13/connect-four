package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Writable;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

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
    // color (0 for red and 1 for yellow) has number pieces out of four and isn't blocked
    public int checkConsecutive(int color, int number) {
        return checkVerticalWin(color, number) + checkDiagonalWin(color, number);
    }

    // REQUIRES: color = 0 or 1, 0<number<8
    // EFFECTS: returns the number of instances there are number of the given color on a diagonal and false otherwise
    private int checkDiagonalWin(int color, int number) {
        int acc = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) { // for each spot on the board
                if (checkWinNextFourRightDirection(i, j, color, 1, number)) {
                    acc++;
                }
                if (checkWinNextFourRightDirection(i, j, color, -1, number)) {
                    acc++;
                }
                if (checkWinNextFourRightDirection(i, j, color, 0, number)) {
                    acc++;
                }
            }

        }
        return acc;
    }

    // REQUIRES: direction = -1 or 1, color = 0 or 1, 0<number<8
    // EFFECTS: return true number of the next 4 pieces above and to the right (if direction is 1)
    // or below and to the right (if direction is -1) or to the right (if direction is 0)
    // of piece in given column and height are the given color with out the opposite color in the way
    private boolean checkWinNextFourRightDirection(int column, int height, int color, int direction, int number) {
        int consecutive = 0;
        for (int i = 0; i < 4; i++) {
            int piece; // the color of the piece if it exists, else -1
            int columnIndex = column + i; // the index of the spot if there's nothing in it
            int rowIndex = height + (direction * i);
            try {
                piece = board.get(column + i).get(height + (direction * i)).getColor();
            } catch (Exception e) {
                piece = -1;
            }

            if (piece == color) {
                consecutive++;
            } else if (piece == RED || piece == YELLOW) {
                return false;
            } else if (columnIndex >= 7 || rowIndex >= 7 || rowIndex < 0) {
                return false;
            }
        }
        if (consecutive == number) {
            return true; // if there have been number of the given color
        }
        return false;
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


    // MODIFIES: this
    // EFFECTS: places given piece in a random not-full column
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
    // in a row it goes there. Otherwise if it can get 3 out of 4 it goes there. Otherwise,
    // if it can block you from getting 3 out of 4 it goes there. And so on with 2 and 1
    //
    // It's a basic strategy but is pretty hard to beat because it's never going to miss anything
    // It can't think multiple turns ahead, though, so you can still beat if you think ahead
    // and trap in a spot that it will lose no matter where it goes. It still occasionally does something
    // stupid though
    //
    // To make it better it would need to think multiple turns ahead, and that would probably be
    // too complex to hard-code the logic and would require some form of machine learning
    // or more complicated algorithm (things I would struggle to implement with my current abilities)
    public void smartAIMove() {
        for (int i = 4; i > 0; i--) { // for 4, then 3, 2, and lastly 1 out of 4
            if (checkAIMove(YELLOW, i)) {
                // see if there's a spot you can put yellow to get that many
                return;
            }
            if (checkAIMove(RED, i)) {
                // same but for blocking red from getting that many
                return;
            }
        }
    } // code coverage says I'm missing this line. I think it's because the method
    // always returns before the end of the for loop but that's supposed to happen


    // MODIFIES: this
    // EFFECTS: see which columns you could add a piece of given color to that
    // increases the number of instances there are consecutive in a row of the given color
    // if there are any such columns, put a yellow piece in the middle one of them and return true
    // if not, return false
    private boolean checkAIMove(int color, int consecutive) {
        GamePiece gp = new GamePiece(color);
        int baseline = checkConsecutive(color, consecutive);
        ArrayList<Integer> possibleColumns = new ArrayList<>(); //columns that would work
        for (int i = 1; i <= BOARD_WIDTH; i++) { //get the possible columns
            if (addPiece(i, gp)) {
                if (checkConsecutive(color, consecutive) > baseline) {
                    // if adding a piece to that column makes more places with consecutive pieces,
                    // add the number of that col to possibleColumns
                    possibleColumns.add(i);
                }
                removePiece(i);
            }
        }

        // then put a yellow piece in the middle one of the possible columns
        int numPossibleColumns = possibleColumns.size();
        if (numPossibleColumns > 0) {
            int moveColumn = getAtMid(possibleColumns);
            GamePiece yellowPiece = new GamePiece(YELLOW);
            addPiece(moveColumn, yellowPiece);
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: If the list has an odd size, return the value at the middle index
    // If it has even size, return a random one of the integers at the middle two indices
    public int getAtMid(ArrayList<Integer> possibleColumns) {
        int index;
        if (possibleColumns.size() % 2 == 0) { // if it's even
            Random random = new Random();
            boolean rand = random.nextBoolean();
            if (rand) {
                index = possibleColumns.size() / 2 - 1;
            } else {
                index = possibleColumns.size() / 2;
            }
        } else { //if it's odd
            index = (possibleColumns.size() - 1) / 2;
        }
        return possibleColumns.get(index);
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
