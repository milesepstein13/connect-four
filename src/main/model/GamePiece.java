package model;

import org.json.JSONObject;
import persistance.Writable;

// A piece for a game of connect four. Is either red or yellow
public class GamePiece implements Writable {

    public static final int RED = 0;
    public static final int YELLOW = 1;

    private int color;

    // REQUIRES: color is 0 or 1;
    // EFFECTS: creates a game piece of given color
    public GamePiece(int color) {
        if (color == RED) {
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

    @Override
    // Source: Json Serialization Demo
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("color", color);
        return json;
    }
}
