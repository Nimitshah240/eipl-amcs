package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.operation.model.BillHead;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class BillHeadCellFactory implements Callback<ListView<BillHead>, ListCell<BillHead>> {
    @Override
    public ListCell<BillHead> call(ListView<BillHead> param) {

        return new ListCell<BillHead>() {
            @Override
            protected void updateItem(BillHead item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        };
    }
}