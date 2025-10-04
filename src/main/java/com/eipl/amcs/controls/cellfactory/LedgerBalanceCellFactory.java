package com.eipl.amcs.controls.cellfactory;

import com.eipl.amcs.utils.NumberUtil;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
@SuppressWarnings("hiding")
public class LedgerBalanceCellFactory<S> implements Callback<TableColumn<S, Number>, TableCell<S, Number>> {
    @Override
    public TableCell<S, Number> call(TableColumn<S, Number> param) {
        return new TableCell<S, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    double it = ((java.lang.Number) item).doubleValue();
                    if (it < 0) {
                        setText(String.valueOf(NumberUtil.twoDecimal(Math.abs(it))));
                        setStyle("-fx-text-fill: -fx-accent-color; -fx-alignment: center-right");
                    } else {
                        setText(String.valueOf(NumberUtil.twoDecimal(it)));
                        setStyle("-fx-text-fill: -fx-green-color; -fx-alignment: center-right");
                    }
                }
            }
        };
    }
}
