package com.moskitol.controller;

import com.moskitol.model.Champion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ChampionsHelper {

    private String en = fromFileToString("champions_en");
    private String ru = fromFileToString("champions_ru");

    public String fromFileToString(String fileName) {
        StringBuilder s = new StringBuilder();
        try{
            //читаем файл в строку из ресурсов.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    ChampionsHelper.class.getClass().getResource("/" +fileName).openStream(),"UTF-8"
            ));

            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                s.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public List<Champion> initChampCollection() {
        //сплитим строку полученную из файла ресурсов по - или переносу строки.
        String[] enArray = en.split("-|\n");
        String[] ruArray = ru.split("-|\n");

        List<Champion> championArrayList = Collections.synchronizedList(new ArrayList<Champion>());

        //создаем и добавляем в коллекцию всех героев с именами из файла ресурсов.
        for(String s : enArray) {
            Champion champion = new Champion(s);
            //добавляем чампиону ссылку на страницу dotabuff.
            champion.setLink("https://dotabuff.com/heroes/" + s.toLowerCase()
                    .replace(" ","-")
                    .replace("'",""));
            championArrayList.add(champion);
        }
        //добавляем героям альтернативные названия на русском языке.
        for(int i = 0; i < enArray.length; i++) {
            championArrayList.get(i).setRuName(ruArray[i]);
        }
        //Создаем экзекутор для парралельного парсинга.
        ExecutorService executorService = Executors.newCachedThreadPool();
        //добавляем героям коллекции героев против которых они сильны и слабы на этой неделе.
        for(Champion champion: championArrayList) {
            executorService.execute(() -> {
                List<List<String>> arrayLists = getBadAndGoodVsCollections(champion.getName());
                champion.setGoodVs(arrayLists.get(0));
                champion.setBadVs(arrayLists.get(1));
            });

        }
        executorService.shutdown();
        try {
            //добавляем ожидание завершения работы всех потоков с потолком в 60 секунд.
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return championArrayList;
    }
    //получаем коллекцию с двумя коллекциями с страницы героя dotabuff.
    // Первая - против кого герой силен, вторая - против кого слаб на этой неделе.
    private List<List<String>> getBadAndGoodVsCollections(String championName) {
        List<List<String>> arrayLists = new ArrayList<>();
        List<String> percentages;
        String sectionForGoodVs ="section:eq(6)";
        String sectionForBadVs ="section:eq(7)";
        Document document;
        try {
            //получаем dom страницы героя с dotabuff.
            //в имени героя заменяем пробелы на - и убираем ' для корретного обращения к странице героя.
            document = Jsoup.connect("https://dotabuff.com/heroes/" + championName.toLowerCase()
                    .replace(" ","-")
                    .replace("'","")).get();
            //у некоторых героев отсутствует блок с роликами на их страницах, у таких героев
            // нужно декрементировать номера секций.
            if(document.select("div.col-8").select("section:eq(7)").isEmpty()) {
                sectionForGoodVs = "section:eq(5)";
                sectionForBadVs = "section:eq(6)";
            }
            //получаем секцию с "силен против".
            arrayLists.add((document.select("div.col-8")
                    .select(sectionForGoodVs)
                    .select("a.link-type-hero")
                    .eachText()));

            //получаем секцию с "слаб против".
            arrayLists.add(document.select("div.col-8")
                    .select(sectionForBadVs)
                    .select("a.link-type-hero")
                    .eachText());

            //получаем проценты и конкатенируем их с именами героев.
            percentages = document.select("div.col-8")
                    .select(sectionForGoodVs)
                    .select("tr")
                    .select("td:eq(2").eachText();

            StringBuilder name;
            for(int i = 0; i < arrayLists.get(0).size(); i++) {
                name = new StringBuilder(arrayLists.get(0).get(i));
                name.append(" ").append(percentages.get(i));
                arrayLists.get(0).set(i,name.toString());
            }

            percentages = document.select("div.col-8")
                    .select(sectionForBadVs)
                    .select("tr")
                    .select("td:eq(2").eachText();

            for(int i = 0; i < arrayLists.get(1).size(); i++) {
                name = new StringBuilder(arrayLists.get(1).get(i));
                name.append(" ").append(percentages.get(i));
                arrayLists.get(1).set(i,name.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayLists;
    }
}
