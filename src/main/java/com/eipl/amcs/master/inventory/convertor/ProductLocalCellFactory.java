package com.eipl.amcs.master.inventory.convertor;

import com.eipl.amcs.master.inventory.model.Product;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ProductLocalCellFactory implements Callback<ListView<Product>, ListCell<Product>> {
    @Override
    public ListCell<Product> call(ListView<Product> param) {

        return new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getNameLocal());
                }
            }
        };
    }
}