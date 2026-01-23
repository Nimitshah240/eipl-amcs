package com.eipl.amcs.utils;

import javafx.concurrent.Task;
import javafx.scene.control.TableView;

public class MyTableUtil {
    public static void initTableContextMenu(TableView<?> tableView, String keyName) {
        InitTask task = new InitTask();
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.setOnSucceeded(e -> {
            TableUtils.addCustomTableMenu(tableView, keyName);
        });
    }

    static class InitTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            Thread.sleep(1000);
            return null;
        }
    }
}
