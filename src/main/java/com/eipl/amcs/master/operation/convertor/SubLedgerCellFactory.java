package com.eipl.amcs.master.operation.convertor;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.SubLedger;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SubLedgerCellFactory implements Callback<ListView<SubLedger>, ListCell<SubLedger>> {
    @Override
    public ListCell<SubLedger> call(ListView<SubLedger> param) {

        return new ListCell<SubLedger>() {
            @Override
            protected void updateItem(SubLedger item, boolean empty) {

                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getCode()+" "+item.getName());
                }
            }
        };
    }
}