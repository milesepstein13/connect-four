package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;

public class SaveButton extends JButton {

    private GUI gui;

    // the button that saves the board to a Json File
    public SaveButton(GUI gui) {
        super("Save Board");
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(100, 80);
        this.gui = gui;

    }
}
