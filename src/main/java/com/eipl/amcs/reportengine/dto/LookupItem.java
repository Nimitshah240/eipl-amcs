package com.eipl.amcs.reportengine.dto;

public class LookupItem {

    private Object value;

    private String display;

    public LookupItem() {}

    public LookupItem(Object value, String display) {
        this.value = value;
        this.display = display;
    }

    // getters setters

    @Override
    public String toString() {
        return display;
    }
}