package ui.display;

import javax.swing.*;
import java.awt.*;


// the button that loads a board from a JSON file
public class LoadButton extends JButton {

    public LoadButton() {
        super("Load Board");
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(100, 100);
    }

}
