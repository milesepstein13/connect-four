package ui.display;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

// a panel in the frame that shows a colored circle on a blue background, creating a visual for a game piece
public class Spot extends JPanel {

    public static final int RED = 0;
    public static final int YELLOW = 1;
    public static final int BLANK = -1;

    Color color;

    public Spot() {
        setSize(30, 30);
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: paints a circle of color inside a blue square in the panel
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 100, 100);
        g.setColor(color);
        g.fillOval(10, 10, 60, 60);
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