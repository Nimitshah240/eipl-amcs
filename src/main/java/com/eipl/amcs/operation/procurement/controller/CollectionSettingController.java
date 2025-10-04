package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.operation.procurement.dto.HardwareSetting;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class CollectionSettingController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private CheckBox chkAutoQuality, chkAutoWeight, chkAutoTare;
    @FXML
    private Button btnSave, btnClose;

    private Stage stage;
    private PopupCallback callback;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        btnClose.setOnAction(e -> this.stage.close());
        btnSave.setOnAction(e -> saveData());
        root.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    this.stage.close();
                    break;
            }
        });
    }

    @Override
    public void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("resources/collection/setting.ser"))) {
            HardwareSetting setting = (HardwareSetting) in.readObject();
            if (setting != null) {
                chkAutoWeight.setSelected(setting.isAutoQuantity());
                chkAutoQuality.setSelected(setting.isAutoQuality());
                chkAutoTare.setSelected(setting.isAutoTare());
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveData() {
        File f = new File("resources/collection");
        if (!f.exists())
            f.mkdir();

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("resources/collection/setting.ser"))) {
            HardwareSetting setting = new HardwareSetting();
            setting.setAutoQuality(chkAutoQuality.isSelected());
            setting.setAutoQuantity(chkAutoWeight.isSelected());
            setting.setAutoTare(chkAutoTare.isSelected());
            out.writeObject(setting);
            out.flush();

            this.callback.reloadHardwareSetting(true);
            this.stage.close();
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
