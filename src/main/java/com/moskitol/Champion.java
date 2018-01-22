package com.moskitol;

import java.util.ArrayList;

public class Champion {

    private String name;
    private String ruName;
    private ArrayList<Champion> goodVs = new ArrayList<Champion>();
    private ArrayList<Champion> badVs = new ArrayList<Champion>();

    public Champion(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Champion> getGoodVs() {
        return goodVs;
    }

    public ArrayList<Champion> getBadVs() {
        return badVs;
    }

    public void addGoodVs(Champion champion) {
        goodVs.add(champion);
    }

    public void addBadVs(Champion champion) {
        badVs.add(champion);
    }

    public void removeFromGoodVs(Champion champion) {
        if(goodVs.contains(champion)) {
            goodVs.remove(champion);
        }
    }

    public void removeFromBadVs(Champion champion) {
        if(badVs.contains(champion)) {
            badVs.remove(champion);
        }
    }

    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }
}
