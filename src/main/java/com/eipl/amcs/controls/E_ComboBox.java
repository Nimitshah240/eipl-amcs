package com.eipl.amcs.controls;

import javafx.scene.control.ComboBox;

public class E_ComboBox<T> extends ComboBox<T> {

    public E_ComboBox() {
        setOnKeyReleased(new FocusHandler());
    }
}
