package com.eipl.amcs.controls;

import javafx.scene.control.PasswordField;

public class E_PasswordField extends PasswordField {

    public E_PasswordField() {
        setOnKeyReleased(new FocusHandler());
    }
}
