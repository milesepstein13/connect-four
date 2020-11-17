package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;


// a panel in the frame that shows a colored circle on a blue background,
// creating a visual for a game piece. Has refers to the gui so that it can be the
// appropriate size
public class Spot extends JPanel {

    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final int BLANK = -1;
    public GUI gui;

    Color color;

    public Spot(GUI gui) {
        setSize(30, 30);
        repaint();
        this.gui = gui;
    }

    // MODIFIES: this
    // EFFECTS: paints a circle of color inside a blue square in the panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, (gui.getWidth() - 400) / 6,gui.getHeight() / 8);
        g.setColor(color);
        g.fillOval(10, 10, (gui.getWidth() - 400) / 9, (gui.getWidth() - 400) / 9);
    }

    // MODIFIES: this
    // EFFECTS: updates color to be the given color
    public void update(int color) {
        if (color == RED) {
            this.color = Color.RED;
        }
        if (color == YELLOW) {
            this.color = Color.YELLOW;
        }
        if (color == BLANK) {
            this.color = Color.GRAY;
        }
        repaint();

    }
}
