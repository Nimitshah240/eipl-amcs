package com.eipl.amcs.base;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;


public class LaunchScreenController implements MyInitialization {

    @FXML
    Label lbl;
    @FXML
    StackPane root;

    private Timeline timeline;
    private int dotCount = 0;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0.5), event -> {
                        dotCount = (dotCount + 1) % 4;
                        StringBuilder text = new StringBuilder("Loading");
                        for (int i = 0; i < dotCount; i++) {
                            text.append(" .");
                        }
                        lbl.setText(text.toString());
                    })
            );
            timeline.setCycleCount(27);
            timeline.play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}