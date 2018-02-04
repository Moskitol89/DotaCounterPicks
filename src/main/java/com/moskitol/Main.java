package com.moskitol;

import com.moskitol.view.ProgressDialog;
import com.moskitol.view.View;

import javax.swing.*;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {
        final ProgressDialog progress = new ProgressDialog();
        java.util.Timer timer = new java.util.Timer();
        final TimerTask task = new TimerTask() {
            public void run() {
                progress.showDialog();
            }
        };
        // Задаем время, через которое должен включиться progressBar,
        // Если задача еще не выполнена
        timer.schedule( task, 500 );

        Thread someThread = new Thread(() -> {
            // Тут идет обработка
            new View();

            SwingUtilities.invokeLater(() -> {
                task.cancel();
                progress.closeDialog();
            });
        });
        someThread.start();
    }
}
