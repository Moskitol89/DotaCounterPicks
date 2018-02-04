package com.moskitol;

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
            //читам файл в строку из ресурсов
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
            championArrayList.add(new Champion(s));
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
        String sectionForGoodVs ="section:eq(6)";
        String sectionForBadVs ="section:eq(7)";
        //если герой - Techies, то нужно декрементировать номера секций из-за отсутствия 1 блока на его странице.
        if (championName.equalsIgnoreCase("Techies") ||
                championName.equalsIgnoreCase("Минер")) {
            sectionForGoodVs = "section:eq(5)";
            sectionForBadVs = "section:eq(6)";
        }
        Document document;
        try {
            //получаем dom страницы героя с dotabuff.
            //в имени героя заменяем пробелы на - и убираем ' для корретного обращения к странице героя.
            document = Jsoup.connect("https://dotabuff.com/heroes/" + championName.toLowerCase()
                    .replace(" ","-")
                    .replace("'","")).get();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayLists;
    }
}
