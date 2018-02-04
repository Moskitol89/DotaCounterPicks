
package com.moskitol.view;

import com.moskitol.Champion;
import com.moskitol.ChampionsHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class View extends JFrame {

    private JLabel labelGoodVs;
    private JLabel labelBadVs;
    private JTextField textField;
    private JButton button;
    private List<Champion> championArrayList;
    public View() {

        super("Dota counter pick");
        //инициализируем коллекцию чампионов.
        championArrayList = new ChampionsHelper().initChampCollection();
        JPanel jPanel = new JPanel();
        //создаем из ресурсов и изменяем иконку.
        BufferedImage image = null;
        try {
            image = ImageIO.read(View.class.getResource("/dota2.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIconImage(image);

        Toolkit kit = this.getToolkit();

        Dimension d = kit.getScreenSize();
        //задаем центровку.
        setLocation(d.width / 2 - 150,d.height / 2 -150);
        jPanel.setLayout(null);
        //Запрещаем изменение размеров окна.
        setResizable(false);
        //создаем лэйблэ для результатов поиска.
        labelGoodVs = new JLabel();
        labelBadVs = new JLabel();
        //на крестик закрываем приложение.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //изменяем размеры окна.
        setSize(350, 300);
        //задаем расположение лэйблов.
        labelGoodVs.setBounds(40, 90, 150,170);
        labelBadVs.setBounds(200, 90,150,170);

        //создаем дополнительные лэйблы, поле текста, кнопку и задаем их расположение.
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
        //добаляем листенер к кнопке и текстовому полю.
        ButtonListener buttonListener = new ButtonListener();
        button.addActionListener(buttonListener);
        textField.addActionListener(buttonListener);

        //добавляем все на панель.
        jPanel.add(helpGood);
        jPanel.add(helpBad);
        jPanel.add(helpTxt);
        jPanel.add(textField);
        jPanel.add(button);
        jPanel.add(labelGoodVs);
        jPanel.add(labelBadVs);
        //добавляем саму панель
        getContentPane().add(jPanel);
        //отображаем
        setVisible(true);

    }
    //создаем листенер для кнопки.
    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //получаем имя героя из текстовой строки.
            String championName = textField.getText();

            List<String> goodVsArray = null;
            List<String> badVsArray = null;

            //создаем стрингбилдеры для лэйблов.
            StringBuilder goodVsString = new StringBuilder("<html>");
            StringBuilder badVsString = new StringBuilder("<html>");

            //заполняем коллекции против кого герой слаб/силен используя линейный поиск.
            for (Champion champion: championArrayList) {
                if(champion.getName().equalsIgnoreCase(championName)
                        || champion.getRuName().equalsIgnoreCase(championName)) {
                    goodVsArray = champion.getGoodVs();
                    //при совпадении изменяем текст в текстовом поле на альтернативное русское имя героя
                    //для удобства в дальнейшем поиске.
                   textField.setText(champion.getRuName());
                    badVsArray = champion.getBadVs();
                    break;
                }
                else {
                    //если совпадений не найдено.
                    labelGoodVs.setText("Герой не найден");
                }
            }
            //конкатенация строк в формат html для использования в лэйблах.
            if(goodVsArray != null) {
                for(String s : goodVsArray) {
                    goodVsString.append(s).append("<br>");
                }
                goodVsString.append("<html>");
                //изменяем текст в лэйбле на конечный результат.
                labelGoodVs.setText(goodVsString.toString());
            }

            if(badVsArray != null) {
                for(String s : badVsArray) {
                    badVsString.append(s).append("<br>");
                }
                badVsString.append("<html>");
                //изменяем текст в лэйбле на конечный результат.
                labelBadVs.setText(badVsString.toString());
            }

        }
    }
}
