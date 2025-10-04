package com.eipl.amcs.master.inventory.convertor;

import com.eipl.amcs.master.inventory.model.Product;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class ProductConvertor extends StringConverter<Product> {

    private final ComboBox<Product> cbox;

    public ProductConvertor(ComboBox<Product> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Product Product) {
        if (Product == null)
            return null;
        return Product.toString();
    }

    @Override
    public Product fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
