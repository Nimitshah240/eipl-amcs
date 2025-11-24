package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.operation.procurement.model.BmcRunningHours;
import com.eipl.amcs.operation.procurement.task.BmcRunningHrsDeleteTask;
import com.eipl.amcs.operation.procurement.task.BmcRunningHrsLoadTask;
import com.eipl.amcs.operation.procurement.task.BmcRunningHrsSaveTask;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class BmcRunningHrsController implements MyInitialization {
    private final ObjectProperty<BmcRunningHours> bmcRunningHrsObjectProperty;
    private final StringBuilder errorMsg = null;
    public String invoice = "";
    @FXML
    TableView<BmcRunningHours> tableBMCRunningHrs;
    @FXML
    GridPane gridMaster;
    @FXML
    VBox vbox;
    @FXML
    private StackPane root;
    @FXML
    private E_TextField txtBmcRunningHrs, txtDgRunningHrs, txtAmount, txtSocietyCode, txtPowerGrid;
    @FXML
    private E_Button btnSave, btnEdit, btnDelete, btnCancel;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TableColumn<BmcRunningHours, String> colSocietyCode;
    @FXML
    private TableColumn<BmcRunningHours, Long> colCode;
    @FXML
    private TableColumn<BmcRunningHours, Integer> colBmcRunningHrs, colDgRunningHrs, colPowerGrid;
    @FXML
    private TableColumn<BmcRunningHours, BigDecimal> colAmount;
    private Stage stage;
    private ResourceBundle resourceBundle;
    private BmcRunningHours dto = null;
    private PopupCallback callback;
    private BmcRunningHours bmcRunningHrs;

    public BmcRunningHrsController() {
        bmcRunningHrsObjectProperty = new SimpleObjectProperty<>();
    }

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
        btnSave.setText(resourceBundle.getString("add"));
        vbox.getChildren().remove(gridMaster);
        this.resourceBundle = resourceBundle;
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        dpDate.setValue(LocalDate.now());
        this.resourceBundle = resourceBundle;
        txtSocietyCode.setText(MainApp.identityDto.getSociety().getCode());
        txtSocietyCode.setDisable(true);
        txtAmount.setDisable(true);
        bmcRunningHrsObjectProperty.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnEdit.setDisable(false);
        });

        btnSave.setOnAction(e -> {
            if (btnSave.getText().equalsIgnoreCase(resourceBundle.getString("add"))) {
                FocusUtils.requestFocus(txtBmcRunningHrs);
                vbox.getChildren().add(1, gridMaster);
                btnSave.setText(resourceBundle.getString("save"));
            } else {
                validateAndSave();
                btnSave.setText(resourceBundle.getString("add"));
                vbox.getChildren().remove(gridMaster);
            }
        });
        setupComboBox();
        setupTable();
        loadData();
        btnCancel.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            if (btnEdit.getText().equalsIgnoreCase(resourceBundle.getString("edit"))) {
                FocusUtils.requestFocus(txtBmcRunningHrs);
                if (bmcRunningHrsObjectProperty.get() != null) {
                    bmcRunningHrs = bmcRunningHrsObjectProperty.get();
                    vbox.getChildren().add(1, gridMaster);
                    setControls(bmcRunningHrsObjectProperty.get());
                    btnEdit.setText(resourceBundle.getString("update"));
                }
            } else {
                updateData();
                vbox.getChildren().remove(gridMaster);
                btnEdit.setText(resourceBundle.getString("edit"));
            }
        });
        FocusUtils.requestFocus(txtBmcRunningHrs);
        txtPowerGrid.setText("0");
        txtAmount.setText("0");
    }

    @Override
    public void setupComboBox() {
        dpDate.setConverter(new LocalDateConvertor());
    }

    private void setControls(BmcRunningHours bmcRunningHrs) {
        txtDgRunningHrs.setText(String.valueOf(bmcRunningHrs.getRunningHoursDg()));
        txtPowerGrid.setText(String.valueOf(bmcRunningHrs.getRunningHoursPower()));
        txtBmcRunningHrs.setText(String.valueOf(bmcRunningHrs.getTotalRunningHours()));
    }

    public void validateAndSave() {
        try {
            bmcRunningHrs = new BmcRunningHours();
            setValuesInObject();
            var task = new BmcRunningHrsSaveTask(bmcRunningHrs, (short) 0);
            task.setOnSucceeded(e -> {
                loadData();
                clearControls();
            });
            new Thread(task).start();

        } catch (RuntimeException e) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("dock"),
                    "ERROR");
            alert.createAlert();
            throw new RuntimeException(e);
        }
    }

    private void setValuesInObject() {
        bmcRunningHrs.setSocietyCode(MainApp.identityDto.getSociety().getCode());
        bmcRunningHrs.setUnionCode(MainApp.identityDto.getUnion().getCode());
        bmcRunningHrs.setAmount(BigDecimal.ZERO);
        bmcRunningHrs.setRunningHoursPower(Integer.valueOf(0));
        bmcRunningHrs.setRunningHoursDg(Integer.parseInt(txtDgRunningHrs.getText()));
        bmcRunningHrs.setTotalRunningHours(Integer.parseInt(txtBmcRunningHrs.getText()));
        bmcRunningHrs.setIsActive(true);
    }

    public void updateData() {
        setValuesInObject();
        var task = new BmcRunningHrsSaveTask(bmcRunningHrs, (short) 1);
        task.setOnSucceeded(e -> {
            loadData();
            clearControls();
        });
        new Thread(task).start();

    }

    @Override
    public void setupTable() {
        try {
            colDgRunningHrs.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRunningHoursDg()));
            colBmcRunningHrs.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotalRunningHours()));
            colPowerGrid.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRunningHoursPower()));
            colSocietyCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSocietyCode()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            colCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCode()));
            bmcRunningHrsObjectProperty.bind(tableBMCRunningHrs.getSelectionModel().selectedItemProperty());
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loadData() {
        tableBMCRunningHrs.setItems(null);
        BmcRunningHrsLoadTask task = new BmcRunningHrsLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<BmcRunningHours> list = task.get();
                if (list != null)
                    tableBMCRunningHrs.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void loadControls() {
        if (this.dto != null) {
            dpDate.setValue(LocalDate.now());
            txtBmcRunningHrs.setText(String.valueOf(dto.getTotalRunningHours()));
            txtDgRunningHrs.setText(String.valueOf(Integer.valueOf(dto.getRunningHoursDg())));
            txtPowerGrid.setText(String.valueOf(dto.getRunningHoursPower()));
            txtAmount.setText(dto.getAmount().toString());
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("BmcRunningHrs"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            BmcRunningHours dto = bmcRunningHrsObjectProperty.get();
            if (dto != null) {
                var task = new BmcRunningHrsDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("BmcRunningHrs"),
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
        txtAmount.setText("");
        txtPowerGrid.setText("");
        txtDgRunningHrs.setText("");
        txtBmcRunningHrs.setText("");
    }
}
