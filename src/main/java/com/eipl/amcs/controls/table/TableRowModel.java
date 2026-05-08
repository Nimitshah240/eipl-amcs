package com.eipl.amcs.controls.table;

import javafx.beans.property.*;
import java.util.HashMap;
import java.util.Map;

public abstract class TableRowModel {
    private final Map<String, StringProperty> columns = new HashMap<>();

    public StringProperty columnProperty(String columnName) {
        return columns.computeIfAbsent(columnName, k -> new SimpleStringProperty(""));
    }

    public String getColumnValue(String columnName) {
        return columnProperty(columnName).get();
    }

    public void setColumnValue(String columnName, String value) {
        columnProperty(columnName).set(value);
    }
}

