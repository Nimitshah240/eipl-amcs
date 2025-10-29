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
//            if (url != null && !url.equals(MainApp.class.getResource("view/Identity.fxml"))) {
//                MainApp.setUrlLoadAndSetpath(null);
//                MainApp.setUrlLoadPath(url);
//            }
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
//            if (url != null && !url.equals(Main.class.getResource("view/Identity.fxml"))) {
//                Main.urlLoadPath = null;
//                Main.urlLoadAndSetpath = url;
//                Main.objectController = loader.getController();
//            }
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
//            if (System.getProperty("os.name").toLowerCase().contains("win"))
            scene.getStylesheets().add(MainApp.class.getResource("view/styles.css").toExternalForm());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
//            stage.setX(Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2 - stage.getWidth() / 2);
//            stage.setY(Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2 - stage.getHeight() / 2);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
