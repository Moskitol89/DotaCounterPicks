
package com.moskitol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View extends JFrame {

    private JLabel label;
    private JTextField textField;
    private JButton button;
    ArrayList<Champion> championArrayList;
    public View() {
        super("Dota counter pick");

        label = new JLabel("test text");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 200);
        textField = new JTextField(10);
        button = new JButton("Search");
        button.addActionListener(new ButtonListener());
        add(textField);
        add(button);
        add(label);
        setLayout(new FlowLayout());
        setVisible(true);
//        championArrayList = new ChampionsHelper().initChampCollection();
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            label.setText(championArrayList.get(championArrayList.size() - 1).getName());
        }
    }
}
