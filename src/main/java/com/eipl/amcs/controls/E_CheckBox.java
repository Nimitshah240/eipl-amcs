package com.eipl.amcs.controls;

import javafx.scene.control.CheckBox;

public class E_CheckBox extends CheckBox {

    public E_CheckBox() {
        setOnKeyPressed(new FocusHandler());
    }
}
