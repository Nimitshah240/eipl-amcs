package com.eipl.amcs.master.procurement.task;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDate;

public class LocalDateLoadTask<S, T> implements Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> {
    @Override
    public TableCell<S, LocalDate> call(TableColumn<S, LocalDate> param) {
        return new TableCell<S, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else {
                    setText(String.valueOf(item));
                }
            }
        };
    }
}

