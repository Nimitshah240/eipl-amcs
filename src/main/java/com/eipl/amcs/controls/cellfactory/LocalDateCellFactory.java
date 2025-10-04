package com.eipl.amcs.controls.cellfactory;

import com.eipl.amcs.utils.AppConstant;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDate;

public class LocalDateCellFactory<S, T> implements Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> {
    @Override
    public TableCell<S, LocalDate> call(TableColumn<S, LocalDate> param) {
        return new TableCell<S, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else {
                    setText(item.format(AppConstant.DATE_FORMATTER));
                }
            }
        };
    }
}