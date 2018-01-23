package com.moskitol;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Champion> championArrayList = new ChampionsHelper().initChampCollection();
        Document document;
        try {
            //получаем dom страницы героя с dotabuff.
            document = Jsoup.connect("https://ru.dotabuff.com/heroes/shadow-fiend").get();
            //получаем секцию с "силен против на этой неделе"
            Elements elementGoodVs = document.select("div.col-8").select("section:eq(6)");
            System.out.println(elementGoodVs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
