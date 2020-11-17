package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;

public class LoadButton extends JButton {

    private GUI gui;

    // the button that loads a board from a JSON file
    public LoadButton(GUI gui) {
        super("Load Board");
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(100, 100);
        this.gui = gui;
    }

}
