package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;

public class SaveButton extends JButton {


    // the button that saves the board to a Json File
    public SaveButton() {
        super("Save Board");
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(100, 80);


    }
}
