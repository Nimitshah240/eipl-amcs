package com.eipl.amcs.controls;

import javafx.application.Platform;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class E_CheckBox extends CheckBox {

    public E_CheckBox() {
        addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case ENTER:
                    if (event.isControlDown()) {

                        this.fireEvent(new javafx.event.ActionEvent(this, null));
                        Platform.runLater(() -> {
                            this.fireEvent(new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "",
                                    KeyCode.TAB, false, false, false, false
                            ));
                        });
                        event.consume();
                    }
                    break;
                default:
                    break;
            }
        });

    }
}
