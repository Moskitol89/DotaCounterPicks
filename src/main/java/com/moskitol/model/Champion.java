package com.moskitol.model;

import java.util.List;

public class Champion {

    private String name;
    private String ruName;
    private String link;

    private List<String> goodVs;
    private List<String> badVs;

    public Champion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getGoodVs()
    {
        return goodVs;
    }

    public List<String> getBadVs() {
        return badVs;
    }

    public void setGoodVs(List<String> goodVs) {
        this.goodVs = goodVs;
    }

    public void setBadVs(List<String> badVs) {
        this.badVs = badVs;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRuName() {
        return ruName;
    }

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }
}
