package com.eipl.amcs.base;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.controller.MappingPopupController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    public void openMappingPopupStage(URL url, String forResource, Object object, PopupCallback callback, String... params) {
        FXMLLoader loader = new FXMLLoader(url);
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.initOwner(MainApp.getStage());
            if (params != null && params.length != 0)
                stage.setTitle(params[0]);
            else
                stage.setTitle("");
            Scene scene = new Scene(loader.load());
            stage.getIcons().add(new Image(MainApp.class.getResource("view/images/logo-small.png").toExternalForm()));

            MappingPopupController controller = loader.getController();
            controller.setStage(stage);
            controller.setObject(object);
            controller.setCallback(callback);
            controller.setForResource(forResource);
            scene.getStylesheets().add(MainApp.class.getResource("view/styles.css").toExternalForm());
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
