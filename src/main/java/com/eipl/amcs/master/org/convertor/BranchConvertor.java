package com.eipl.amcs.master.org.convertor;

import com.eipl.amcs.master.org.model.Branch;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class BranchConvertor extends StringConverter<Branch> {

    private final ComboBox<Branch> cbox;

    public BranchConvertor(ComboBox<Branch> cbox) {
        this.cbox = cbox;
    }

    @Override
    public String toString(Branch branch) {
        if (branch == null)
            return null;
        return branch.toString();
    }

    @Override
    public Branch fromString(String s) {
        if (s == null || s.isEmpty())
            return null;
        return cbox.getItems().stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
    }
}
