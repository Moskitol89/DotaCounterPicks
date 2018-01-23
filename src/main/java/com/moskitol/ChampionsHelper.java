package com.moskitol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class ChampionsHelper {

    private ClassLoader classLoader = this.getClass().getClassLoader();
    private String en = fromFileToString("champions_en");
    private String ru = fromFileToString("champions_ru");

    private String fromFileToString(String fileName) {
        String s = "";
        try{
            //обрезаем первые символы, c ними строка выглядит так:"file:/D:/projects/...."
            s = new String(Files.readAllBytes(Paths.get(String.valueOf(classLoader.getResource(fileName)).substring(6))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public ArrayList<Champion> initChampCollection() {
        System.out.println(en);

        System.out.println();

        System.out.println(ru);

        //сплитим строку полученную из файла ресурсов по - или переносу строки.
        String[] enArray = en.split("-|\r\n");
        String[] ruArray = ru.split("-|\r\n");

        System.out.println(Arrays.toString(enArray));

        ArrayList<Champion> championArrayList = new ArrayList<>();

        //создаем и добавляем в коллекцию всех героев с именами из файла ресурсов.
        for(String s : enArray) {
            championArrayList.add(new Champion(s));
        }
        //добавляем героям альтернативные названия на русском языке.
        for(int i = 0; i < enArray.length; i++) {
            championArrayList.get(i).setRuName(ruArray[i]);
        }

        for(Champion champion: championArrayList) {
            System.out.println(champion.getName() + "-" + champion.getRuName());
        }
        return championArrayList;
    }
}
