package com.eipl.amcs.controls;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Ref: https://thickclient.blog/tag/javafx/
 */
public class FocusHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            var control = (Control) event.getSource();
            var kids = control.getParent().getChildrenUnmodifiable();
            var nextFocus = kids.indexOf(control) + 1;
            while (nextFocus < kids.size()) {
                if (!kids.get(nextFocus).isDisable() && kids.get(nextFocus).isFocusTraversable())
                    break;
                nextFocus++;
            }
//            nextFocus = kids.get(nextFocus).isDisable() ? nextFocus + 1 : nextFocus;
            if (nextFocus < kids.size()) {
                kids.get(nextFocus).requestFocus();
                event.consume();
            }
        }
    }
}