package com.eipl.amcs.utils;

import javafx.application.Platform;
import javafx.scene.Node;

public class FocusUtils {

    public static void requestFocus(Node node) {
        Platform.runLater(() -> {
            node.requestFocus();
        });
    }
}
