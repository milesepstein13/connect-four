package ui.display;

import javax.swing.*;

//change from JLabel to something that makes a circle
public class Spot extends JLabel {

    public static final int RED = 0;
    public static final int YELLOW = 1;

    public Spot() {
        super("empty");
    }

    public void update(int color) {
        if (color == RED) {
            setText("RED!");
        }
        if (color == YELLOW) {
            setText("YELLOW!");
        }
    }
}
