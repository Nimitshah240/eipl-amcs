package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.operation.procurement.model.BmcRecording;
import com.eipl.amcs.operation.procurement.task.BmcRecordingParameterDeleteTask;
import com.eipl.amcs.operation.procurement.task.BmcRecordingParameterLoadTask;
import com.eipl.amcs.operation.procurement.task.BmcRecordingParameterSaveTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class BmcRecordingParameterController implements MyInitialization, PopupCallback {

    private final ObjectProperty<BmcRecording> propRunningPara;
    @FXML
    StackPane root;
    @FXML
    TableView<BmcRecording> tableRecordingParameters;
    @FXML
    TableColumn<BmcRecording, String> colCode;
    @FXML
    TableColumn<BmcRecording, LocalDateTime> colDate, colTime;
    @FXML
    TableColumn<BmcRecording, BigDecimal> colWeight, colTemperature;
    @FXML
    GridPane gridMaster;
    @FXML
    VBox vbox;
    @FXML
    Button btnAdd, btnEdit, btnSave, btnDelete, btnCancel;
    @FXML
    private TextField txtTime, txtWeight, txtTemperature, txtSocietyCode;
    @FXML
    private DatePicker dpDate;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private BmcRecording recordingParameter;

    public BmcRecordingParameterController() {
        propRunningPara = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSave.setText(resourceBundle.getString("add"));
        dpDate.setValue(LocalDate.now());
        txtSocietyCode.setText(MainApp.identityDto.getSociety().getCode());
        txtSocietyCode.setDisable(true);
        txtTime.setText(String.valueOf(LocalTime.now()));
        vbox.getChildren().remove(gridMaster);
        this.resourceBundle = resourceBundle;
        propRunningPara.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnSave.setOnAction(e -> {
            if (btnSave.getText().equalsIgnoreCase(resourceBundle.getString("add"))) {
                vbox.getChildren().add(1, gridMaster);
//                FocusUtils.requestFocus(txtMemberCode);
                btnSave.setText(resourceBundle.getString("save"));
            } else {
                saveData();
                btnSave.setText(resourceBundle.getString("add"));
                vbox.getChildren().remove(gridMaster);
            }
        });
        setupTable();
        loadData();
        btnCancel.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

//        txtMemberCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
//            if (!newValue && txtMemberCode.getText().length() > 0) {
//                String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(txtMemberCode.getText()));
//                setMemberName(code);
//            }
//        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });

        btnEdit.setOnAction(e -> {
            if (btnEdit.getText().equalsIgnoreCase(resourceBundle.getString("edit"))) {
                if (propRunningPara.get() != null) {
                    recordingParameter = propRunningPara.get();
                    vbox.getChildren().add(1, gridMaster);
                    setControls(propRunningPara.get());
                    btnEdit.setText(resourceBundle.getString("update"));
                }
            } else {
                updateData();
                vbox.getChildren().remove(gridMaster);
                btnEdit.setText(resourceBundle.getString("edit"));
            }
        });
    }

    private void setControls(BmcRecording recordingParameter) {
        txtWeight.setText(String.valueOf(recordingParameter.getWeight()));
    }

//    private void checkAndSave() {
//        checkAndSaveData();
//    }


    @Override
    public void saveData() {
        recordingParameter = new BmcRecording();
        setValuesInObject();
        var task = new BmcRecordingParameterSaveTask(recordingParameter, (short) 0);
        task.setOnSucceeded(e -> {
            loadData();
            clearControls();
        });
        new Thread(task).start();

    }

    public void updateData() {
        setValuesInObject();
        var task = new BmcRecordingParameterSaveTask(recordingParameter, (short) 1);
        task.setOnSucceeded(e -> {
            loadData();
            clearControls();
        });
        new Thread(task).start();

    }


    private void setValuesInObject() {
        recordingParameter.setSocietyCode(MainApp.identityDto.getSociety().getCode());
        recordingParameter.setUnionCode(MainApp.identityDto.getUnion().getCode());
        recordingParameter.setRecordingDateTime(LocalDateTime.now());
        try {
            recordingParameter.setWeight(new BigDecimal(txtWeight.getText()));
        } catch (Exception e) {

        }
        recordingParameter.setChillerNo("1");
        recordingParameter.setIsActive(true);
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode().toString()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getRecordingDateTime()));
            colWeight.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getWeight()));
            colTemperature.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getTemperature()));
            colTime.setCellValueFactory(data -> new SimpleObjectProperty(LocalTime.now()));
            propRunningPara.bind(tableRecordingParameters.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void loadData() {
        tableRecordingParameters.setItems(null);
        BmcRecordingParameterLoadTask task = new BmcRecordingParameterLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<BmcRecording> list = task.get();
                if (list != null)
                    tableRecordingParameters.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("recordingparameter"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            BmcRecording dto = propRunningPara.get();
            if (dto != null) {
                var task = new BmcRecordingParameterDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("recordingparameter"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void clearControls() {
        txtTime.setText("");
        txtTemperature.setText("");
        txtWeight.setText("");

    }
}
