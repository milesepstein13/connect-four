package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;

public class LoadButton extends JButton {


    // the button that loads a board from a JSON file
    public LoadButton() {
        super("Load Board");
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(100, 100);
    }

}
