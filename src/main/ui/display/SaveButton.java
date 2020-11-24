package ui.display;

import javax.swing.*;
import java.awt.*;


// the button that saves the board to a Json File
public class SaveButton extends JButton {

    public SaveButton() {
        super("Save Board");
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(100, 80);


    }
}
