package ui.display;

import ui.GUI;

import javax.swing.*;

public class DropperButton extends JButton {

    private GUI gui;

    public DropperButton(String column, GUI gui) {
        super(column);
        this.gui = gui;
    }
}
