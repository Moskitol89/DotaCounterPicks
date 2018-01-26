
package com.moskitol.view;

import com.moskitol.Champion;
import com.moskitol.ChampionsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View extends JFrame {

    private JLabel labelGoodVs;
    private JLabel labelBadVs;
    private JTextField textField;
    private JButton button;
    private ArrayList<Champion> championArrayList;
    public View() {

        super("Dota counter pick");

        championArrayList = new ChampionsHelper().initChampCollection();
        JPanel jPanel = new JPanel();

        Toolkit kit = this.getToolkit();

        Dimension d = kit.getScreenSize();

        setLocation(d.width / 2 - 150,d.height / 2 -150);
        jPanel.setLayout(null);
        setResizable(false);

        labelGoodVs = new JLabel();
        labelBadVs = new JLabel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(350, 300);

        labelGoodVs.setBounds(40, 90, 150,170);
        labelBadVs.setBounds(200, 90,150,170);


        JLabel helpTxt = new JLabel("Введите имя героя:");
        helpTxt.setBounds(40,10, 150, 20);

        JLabel helpGood = new JLabel("Силён против: ");
        helpGood.setBounds(40, 50, 135, 30);

        JLabel helpBad = new JLabel("Слаб против: ");
        helpBad.setBounds(200,50,135,30);

        textField = new JTextField(10);
        textField.setBounds(40,30,150,20);

        button = new JButton("Поиск");
        button.setBounds(200, 30, 70, 20);
        button.addActionListener(new ButtonListener());

        jPanel.add(helpGood);
        jPanel.add(helpBad);
        jPanel.add(helpTxt);
        jPanel.add(textField);
        jPanel.add(button);
        jPanel.add(labelGoodVs);
        jPanel.add(labelBadVs);
        getContentPane().add(jPanel);
        setVisible(true);

    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String championName = textField.getText();

            ArrayList<String> goodVsArray = null;
            ArrayList<String> badVsArray = null;

            StringBuilder goodVsString = new StringBuilder("<html>");
            StringBuilder badVsString = new StringBuilder("<html>");


            for (Champion champion: championArrayList) {
                if(champion.getName().equalsIgnoreCase(championName)
                        || champion.getRuName().equalsIgnoreCase(championName)) {
                    goodVsArray = champion.getGoodVs();
                   textField.setText(champion.getRuName());
                    badVsArray = champion.getBadVs();
                    break;
                }
                else {
                    labelGoodVs.setText("Герой не найден");
                }
            }

            if(goodVsArray != null) {
                for(String s : goodVsArray) {
                    goodVsString.append(s).append("<br>");
                }
                goodVsString.append("<html>");
                labelGoodVs.setText(goodVsString.toString());
            }

            if(badVsArray != null) {
                for(String s : badVsArray) {
                    badVsString.append(s).append("<br>");
                }
                badVsString.append("<html>");
                labelBadVs.setText(badVsString.toString());
            }

        }
    }
}
