package com.moskitol;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

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
        //сплитим строку полученную из файла ресурсов по - или переносу строки.
        String[] enArray = en.split("-|\r\n");
        String[] ruArray = ru.split("-|\r\n");


        ArrayList<Champion> championArrayList = new ArrayList<>();

        //создаем и добавляем в коллекцию всех героев с именами из файла ресурсов.
        for(String s : enArray) {
            championArrayList.add(new Champion(s));
        }
        //добавляем героям альтернативные названия на русском языке.
        for(int i = 0; i < enArray.length; i++) {
            championArrayList.get(i).setRuName(ruArray[i]);
        }
        //добавляем героям коллекции героев против которых они сильны и слабы на этой неделе.
        for(Champion champion: championArrayList) {
            ArrayList<ArrayList<String>> arrayLists = getBadAndGoodVsCollections(champion.getName());
            champion.setGoodVs(arrayLists.get(0));
            champion.setBadVs(arrayLists.get(1));
        }
        return championArrayList;
    }
    //получаем коллекцию с двумя коллекциями с страницы героя dotabuff.
    // Первая - против кого герой силен, вторая - против кого слаб на этой неделе.
    private ArrayList<ArrayList<String>> getBadAndGoodVsCollections(String championName) {
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
        Document document;
        try {
            //получаем dom страницы героя с dotabuff.
            //в имени героя заменяем пробелы на - и убираем ' для корретного обращения к странице героя.
            document = Jsoup.connect("https://ru.dotabuff.com/heroes/" + championName.toLowerCase()
                    .replace(" ","-")
                    .replace("'","")).get();
            //получаем секцию с "силен против".
            arrayLists.add((ArrayList<String>) document.select("div.col-8")
                    .select("section:eq(6)")
                    .select("a.link-type-hero")
                    .eachText());
            //получаем секцию с "слаб против".
            arrayLists.add((ArrayList<String>) document.select("div.col-8")
                    .select("section:eq(7)")
                    .select("a.link-type-hero")
                    .eachText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayLists;
    }
}
