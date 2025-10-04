package com.eipl.amcs.controls;

import javafx.scene.control.Button;

public class E_Button extends Button {
    public E_Button() {
        defaultButtonProperty().bind(focusedProperty());
    }
}
