package com.eipl.amcs.base;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.controller.MappingPopupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;


public class FxmlLoaderUtil {
    public Node load(URL url) {
        FXMLLoader loader = new FXMLLoader(url);
        try {
            loader.setResources(MainApp.getBundle());
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object loadAndSet(URL url) {
        FXMLLoader loader = new FXMLLoader(url);
        try {
            loader.setResources(MainApp.getBundle());
            loader.load();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void openMappingPopupStage(URL url, String forResource, Object object, PopupCallback callback, double... dims) {
        FXMLLoader loader = new FXMLLoader(url);
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(MainApp.getStage());
            Scene scene = new Scene(loader.load());

            MappingPopupController controller = loader.getController();
            controller.setStage(stage);
            controller.setObject(object);
            controller.setCallback(callback);
            controller.setForResource(forResource);
            scene.getStylesheets().add(MainApp.class.getResource("view/styles.css").toExternalForm());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
