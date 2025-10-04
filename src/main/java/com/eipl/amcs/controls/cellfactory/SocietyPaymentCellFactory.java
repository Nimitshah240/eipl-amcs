package com.eipl.amcs.controls.cellfactory;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class SocietyPaymentCellFactory<S, T> implements Callback<TableColumn<S, SocietyPaymentCycle>, TableCell<S, SocietyPaymentCycle>> {
    @Override
    public TableCell<S, SocietyPaymentCycle> call(TableColumn<S, SocietyPaymentCycle> param) {
        return new TableCell<>() {
            @Override
            protected void updateItem(SocietyPaymentCycle item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setText(null);
                else {
                    setText(item.toDateShiftString());
                }
            }
        };
    }
}