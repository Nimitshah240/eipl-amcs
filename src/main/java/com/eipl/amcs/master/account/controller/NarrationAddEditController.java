package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.E_TextFieldLocal;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.converter.NarrationTypeConvertor;
import com.eipl.amcs.master.account.model.Narration;
import com.eipl.amcs.master.account.model.NarrationType;
import com.eipl.amcs.master.account.task.NarrationNumberLoadTask;
import com.eipl.amcs.master.account.task.NarrationSaveTask;
import com.eipl.amcs.master.account.task.NarrationTypeLoadTask;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class NarrationAddEditController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private E_ComboBox<NarrationType> cboxNarrationType;
    @FXML
    private E_TextField txtNarrationName;
    @FXML
    private E_TextFieldLocal txtNarrationNameLocal;
    @FXML
    private Button btnSaveUpdate, btnClose;
    private String code;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private Narration narration;

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadNarrationType();
        cboxNarrationType.setConverter(new NarrationTypeConvertor(cboxNarrationType));
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        if (btnClose != null) {
            btnClose.setOnAction(e -> {
                if (this.callback != null) {
                    this.callback.reloadData(true);
                }
                this.stage.close();
            });
        }
    }

    public void setNarration(Narration dto) {
        if (dto != null) {
            this.narration = dto;
            this.code = narration.getNarrationCode();
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            loadNarrationNextCode();
        }
    }

    public void loadControls() { //Edit
        txtNarrationName.setText(narration.getNarration());
        txtNarrationNameLocal.setText(narration.getNarrationLocal());
        cboxNarrationType.getSelectionModel().select(narration.getNarrationType());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("narrationname"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.narration != null) {
                narration = setValuesInObject();
                updateData();
            }
        } else {
            narration = new Narration();
            narration = setValuesInObject();
            saveData();
        }
    }

    private Narration setValuesInObject() {
        narration.setNarrationCode(code);
        narration.setNarration(txtNarrationName.getText());
        narration.setNarrationLocal(txtNarrationNameLocal.getText());
        narration.setNarrationType(cboxNarrationType.getSelectionModel().getSelectedItem());
        return narration;
    }

    private boolean validate() {
        if (cboxNarrationType.getValue() == null)
            errorMsg.append(resourceBundle.getString("narrationtypenullerror") + "\n");
        if (txtNarrationName.getText() == null || txtNarrationName.getText().isBlank())
            errorMsg.append(resourceBundle.getString("narrationnullerror") + "\n");
        return errorMsg.length() == 0;
    }

    private void loadNarrationType() {
        var task = new NarrationTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<NarrationType> list = task.get();
                if (list != null) {
                    cboxNarrationType.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadNarrationNextCode() {
        var task = new NarrationNumberLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                this.code = task.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
    }

    @Override
    public void saveData() {
        var task = new NarrationSaveTask(narration, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("narrationlocal"),
                        resourceBundle.getString("narration.insert.successful"));
                alert.createAlert();
                if (this.callback != null) {
                    this.callback.reloadData(true);
                }
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("narrationlocal"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new NarrationSaveTask(narration, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("narrationlocal"),
                        resourceBundle.getString("narration.update.successful"));
                alert.createAlert();
                if (this.callback != null) {
                    this.callback.reloadData(true);
                }
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("narrationname"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }
}