package ui.display;

import ui.GUI;

import javax.swing.*;

public class LoadButton extends JButton {

    private GUI gui;

    public LoadButton(GUI gui) {
        super("Load Board");
        this.gui = gui;
    }

}
