package ui.display;

import ui.GUI;

import javax.swing.*;

public class SaveButton extends JButton {

    private GUI gui;

    public SaveButton(GUI gui) {
        super("Save Board");
        this.gui = gui;
    }
}
