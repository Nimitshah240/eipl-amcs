package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Narration;
import com.eipl.amcs.master.account.task.NarrationDeleteTask;
import com.eipl.amcs.master.account.task.NarrationLoadTask;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class NarrationController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Narration> propNarrationDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<Narration> tableNarration;
    @FXML
    TableColumn<Narration, String> colNarration, colNarrationLocal, colNarrationType;
    @FXML
    Button btnAdd, btnEdit, btnDelete, btnClose;
    private ResourceBundle resourceBundle;


    public NarrationController() {
        propNarrationDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propNarrationDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        setupTable();
        loadData();
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "NarrationAddEdit", null, this, resourceBundle.getString("narrationname"));
        });

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            Narration dto = propNarrationDto.get();
            if (dto != null)
                editNarration(dto);
        });

        tableNarration.setRowFactory(tv -> {
            TableRow<Narration> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Narration data = row.getItem();
                    editNarration(data);
                }
            });
            return row;
        });

        tableNarration.setOnKeyPressed(event -> {
            Narration dto = tableNarration.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propNarrationDto.get();
                    if (dto != null)
                        deleteData();
                    break;
                case ENTER:
                    editNarration(dto);
                    break;
            }
        });
    }

    private void editNarration(Narration narration) {
        if (narration != null)
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "NarrationAddEdit", narration, this, resourceBundle.getString("narrationname"));
    }

    @Override
    public void setupTable() {
        try {
            colNarration.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNarration()));
            colNarrationLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNarrationLocal()));
            colNarrationType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNarrationType().toString()));
            propNarrationDto.bind(tableNarration.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableNarration);
        } catch (Exception e) {
            System.out.println("Narration setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        NarrationLoadTask task = new NarrationLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Narration> list = task.get();
                if (list != null) {
                    tableNarration.setItems(FXCollections.observableList(list));
                } else {
                    tableNarration.setItems(FXCollections.observableArrayList());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("narrationname"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Narration dto = propNarrationDto.get();
            if (dto != null) {
                var task = new NarrationDeleteTask(dto.getNarrationCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("narrationname"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        reloadData(true);
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}