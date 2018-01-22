package com.moskitol;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        ChampionsHelper championsHelperEn = new ChampionsHelper("champions_en");
        String en = championsHelperEn.fromFileToString();
        System.out.println(en);

        System.out.println();

        ChampionsHelper championsHelperRu = new ChampionsHelper("champions_ru");
        String ru = championsHelperRu.fromFileToString();
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
    }
}
