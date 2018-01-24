package com.moskitol;

import java.util.ArrayList;

public class Champion {

    private String name;
    private String ruName;

    private ArrayList<String> goodVs;
    private ArrayList<String> badVs;

    public Champion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getGoodVs()
    {
        return goodVs;
    }

    public ArrayList<String> getBadVs() {
        return badVs;
    }

    public void setGoodVs(ArrayList<String> goodVs) {
        this.goodVs = goodVs;
    }

    public void setBadVs(ArrayList<String> badVs) {
        this.badVs = badVs;
    }


    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }
}
