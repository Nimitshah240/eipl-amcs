package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.utils.CommonUtils;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class LedgerCellFactory implements Callback<ListView<Ledger>, ListCell<Ledger>> {
    @Override
    public ListCell<Ledger> call(ListView<Ledger> param) {

        return new ListCell<Ledger>() {
            @Override
            protected void updateItem(Ledger item, boolean empty) {

                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(CommonUtils.getLocalString(item.getName(), item.getNameLocal()));
                }
            }
        };
    }
}