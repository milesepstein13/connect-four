package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;

public class DropperButton extends JButton {

    private GUI gui;

    // a button used to place a piece. It's text it the number of the column it corresponds with
    public DropperButton(String column, GUI gui) {
        super(column);
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(50, 50);
        this.gui = gui;
    }
}
