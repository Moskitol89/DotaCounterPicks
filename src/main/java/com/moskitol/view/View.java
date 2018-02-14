
package com.moskitol.view;

import com.moskitol.model.Champion;
import com.moskitol.controller.ChampionsHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class View extends JFrame {

    private JLabel labelGoodVs;
    private JLabel labelBadVs;
    private JLabel link;
    private JTextField textField;
    private JButton button;
    private static List<Champion> championArrayList;
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
        //создаем лэйбл для ссылки.
        link = new JLabel();
        //на крестик закрываем приложение.
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //изменяем размеры окна.
        setSize(350, 300);
        //задаем расположение лэйблов.
        labelGoodVs.setBounds(40, 90, 150,170);
        labelBadVs.setBounds(200, 90,150,170);
        link.setBounds(230, 5, 150,20);
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
        jPanel.add(link);
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

            List<String> goodVsArray;
            List<String> badVsArray;

            //заполняем коллекции против кого герой слаб/силен используя линейный поиск.
            for (Champion champion: championArrayList) {
                if(champion.getName().equalsIgnoreCase(championName)
                        || champion.getRuName().equalsIgnoreCase(championName)) {
                    goodVsArray = champion.getGoodVs();
                    badVsArray = champion.getBadVs();
                    //при совпадении изменяем текст в текстовом поле на альтернативное русское имя героя
                    //для удобства в дальнейшем поиске.
                   textField.setText(champion.getRuName());
                   goWebsite(link,champion.getLink());
                   //изменяем текст в лейблах именами подходящих героев.
                   labelGoodVs.setText(txtForLabels(goodVsArray));
                   labelBadVs.setText(txtForLabels(badVsArray));
                   //прерываем цикл, герой уже найден.
                   break;
                }
                else {
                    //если совпадений не найдено изменяем текст 1 лебла, второй оставляем пустым.
                    labelGoodVs.setText("Герой не найден");
                    labelBadVs.setText("");
                }
            }
        }
        //конкатенация строк в формат html для использования в лэйблах.
        private String txtForLabels(List<String> list) {
            if(list != null) {
                StringBuilder stringBuilder = new StringBuilder("<html>");
                for(String s : list) {
                    stringBuilder.append(s).append("<br>");
                }
                stringBuilder.append("</html>");
                //изменяем текст в лэйбле на конечный результат.
                return stringBuilder.toString();
            }
            return null;
        }
        //метод перехода на страницу героя.
        private void goWebsite(JLabel website, final String url) {
            //создаем ссылку в формате html.
            website.setText("<html><a href=\"\">go to hero page</a></html>");
            website.setCursor(new Cursor(Cursor.HAND_CURSOR));
            website.removeAll();
            //добавляем лейблу листенер.
            website.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        //открываем страницу браузера по умолчанию.
                        Desktop.getDesktop().browse(new URI(url));
                        //удаляем листенер, что бы они не накапливались.
                        website.removeMouseListener(this);
                    } catch (URISyntaxException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
    }
}
