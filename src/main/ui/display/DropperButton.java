package ui.display;

import javax.swing.*;
import java.awt.*;


// a button used to place a piece. It's text it the number of the column it corresponds with
public class DropperButton extends JButton {

    public DropperButton(String column) {
        super(column);
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(50, 50);
    }
}
