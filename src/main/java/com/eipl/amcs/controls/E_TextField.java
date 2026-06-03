package com.eipl.amcs.controls;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class E_TextField extends TextField {

    public E_TextField() {
        setOnKeyReleased(new FocusHandler());

        addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case RIGHT:
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
                case LEFT:
                    if (event.isControlDown()) {

                        this.fireEvent(new javafx.event.ActionEvent(this, null));
                        Platform.runLater(() -> {
                            this.fireEvent(new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "",
                                    KeyCode.TAB, true, false, false, false
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
