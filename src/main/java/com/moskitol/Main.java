package com.moskitol;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Main {

    private static View view;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> view = new View());
        ArrayList<Champion> championArrayList;
        SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected ArrayList<Champion> doInBackground() throws Exception {
                ArrayList<Champion> championsHelper = new ChampionsHelper().initChampCollection();
                System.out.println(championsHelper.size());
                return championsHelper;
            }
        };
        swingWorker.execute();
        try {
            championArrayList = (ArrayList<Champion>) swingWorker.get();
            System.out.println(championArrayList.size());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
//


    }
}
