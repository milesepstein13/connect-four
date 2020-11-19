package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;

public class DropperButton extends JButton {


    // a button used to place a piece. It's text it the number of the column it corresponds with
    public DropperButton(String column) {
        super(column);
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(50, 50);
    }
}
