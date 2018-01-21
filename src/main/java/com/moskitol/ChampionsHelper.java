package com.moskitol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChampionsHelper {

    private String fileName;
    private ClassLoader classLoader = this.getClass().getClassLoader();

    public ChampionsHelper(String fileName) {
        this.fileName = fileName;
    }

    public String fromFileToString() {
        String s = "";
        try{
            System.out.println(String.valueOf(classLoader.getResource( fileName)).substring(6));
            //обрезаем первые символы, без них строка выглядит так:"file:/D:/projects/...."
            s = new String(Files.readAllBytes(Paths.get(String.valueOf(classLoader.getResource(fileName)).substring(6))));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
