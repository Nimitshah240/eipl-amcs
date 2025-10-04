package com.eipl.amcs.master.inventory.convertor;

import com.eipl.amcs.master.inventory.model.ProductGroup;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class ProductGroupConvertor extends StringConverter<ProductGroup> {

    private final ComboBox<ProductGroup> cbox;

    public ProductGroupConvertor(ComboBox<ProductGroup> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(ProductGroup productGroup) {
        if (productGroup == null)
            return null;
        return productGroup.toString();
    }

    @Override
    public ProductGroup fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
