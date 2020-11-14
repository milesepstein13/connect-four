package ui.display;

import ui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwitchPlayersButton extends JButton implements ActionListener {

    private GUI gui;

    public SwitchPlayersButton(GUI gui) {
        super("Switch Number of Players");
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("aaaa");
        gui.getInfoDisplay().setText("Hopefully this worked!");
    }
}
