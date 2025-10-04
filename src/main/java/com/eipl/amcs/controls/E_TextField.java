package com.eipl.amcs.controls;

import javafx.scene.control.TextField;

public class E_TextField extends TextField {

    public E_TextField() {
        setOnKeyReleased(new FocusHandler());
    }
}
