package com.moskitol;

public class Main {

    public static void main(String[] args) {


        ChampionsHelper championsHelper = new ChampionsHelper("champions_en");
        String s = championsHelper.fromFileToString();
        System.out.println(s);

    }
}
