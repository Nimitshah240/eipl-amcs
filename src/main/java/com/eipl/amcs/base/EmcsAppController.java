package com.eipl.amcs.base;

import com.eipl.amcs.MainApp;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EmcsAppController implements MyInitialization {

    @FXML
    private BorderPane contentPane;
    @FXML
    private StackPane root, paneDrop;
    @FXML
    private Label lblLoaderMessage;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainApp.setContentPane(contentPane);
        MainApp.paneDrop = paneDrop;
        MainApp.lblMessage = lblLoaderMessage;
    }
}
