package model;

public class GamePiece {

    public static final int RED = 0;
    public static final int YELLOW = 1;

    private int color;

    // REQUIRES: color is "red" or "yellow"
    // EFFECTS: creates a game piece of given color
    public GamePiece(String color) {
        if (color.equals("red")) {
            this.color = RED;
        } else {
            this.color = YELLOW;
        }
    }

    // EFFECTS: returns true if the piece is red and false otherwise
    public boolean isRed() {
        if (color == RED) {
            return true;
        } else {
            return false;
        }
    }

    // EFFECTS: returns true if the piece is yellow and false otherwise
    public boolean isYellow() {
        if (color == YELLOW) {
            return true;
        } else {
            return false;
        }
    }

    //EFFECTS: returns 0 if the piece is red and 1 if the piece is yellow
    public int getColor() {
        return color;
    }
}
