package ui.display;

import javax.swing.*;
import java.awt.*;

public class InfoDisplay extends JTextArea {

    // A text area that displays information about the state of the game
    public InfoDisplay() {
        super();
        setFont(new Font("Arial", Font.BOLD, 30));
        setEditable(false);
        setSize(100, 500);
    }
}
