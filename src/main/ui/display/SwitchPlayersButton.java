package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwitchPlayersButton extends JButton {


    // the button that switches the number of players
    public SwitchPlayersButton() {
        super("Switch Number of Players");
        setFont(new Font("Arial", Font.BOLD, 30));
        setSize(100, 100);

    }


}
