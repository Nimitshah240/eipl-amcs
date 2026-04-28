package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Committee;
import com.eipl.amcs.operation.administartion.task.CommitteeDeleteTask;
import com.eipl.amcs.operation.administartion.task.CommitteeLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CommitteeController implements MyInitialization, PopupCallback {
    private final ObjectProperty<Committee> propCommitteeDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<Committee> tableCommittee;
    @FXML
    TableColumn<Committee, String> colName, colNameLocal, colElectionDate, colFormationDate, colCommitteeMembmers;
    @FXML
    DatePicker dpDate;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnRegister;
    private ResourceBundle resourceBundle;


    public CommitteeController() {
        propCommitteeDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
//        dpDate.setValue(LocalDate.now());
        propCommitteeDto.addListener((observable, oldValue, newValue) -> {
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
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CommitteeAddEdit", null, this, "Committee");
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            Committee dto = propCommitteeDto.get();
            if (dto != null)
                editCommittee(dto);
        });

        tableCommittee.setRowFactory(tv -> {
            TableRow<Committee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Committee data = row.getItem();
                    editCommittee(data);
                }
            });
            return row;
        });

        tableCommittee.setOnKeyPressed(event -> {
            Committee dto = tableCommittee.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propCommitteeDto.get();
                    if (dto != null)
                        deleteData();
                    break;
                case ENTER:
                    editCommittee(dto);
                    break;
            }
        });
//
//        btnRegister.setOnAction(e -> {
//            validateAndGenerateReport();
//        });
    }

    private void editCommittee(Committee committee) {
        if (committee != null)
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CommitteeAddEdit", committee, this, "Committee");
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_date", dpDate.getValue());
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.COMMITTEE_REGISTER, params);
        JasperViewer.viewReport(print, false);
    }

    @Override
    public void setupTable() {
        try {
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colNameLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colElectionDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getElectionDate().toString()));
            colFormationDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormationDate().toString()));
            colCommitteeMembmers.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getMembers().size())));
            propCommitteeDto.bind(tableCommittee.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("CommiteMembers setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        CommitteeLoadTask task = new CommitteeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Committee> list = task.get();

                tableCommittee.setItems(FXCollections.observableList(list));

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("committee"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Committee dto = propCommitteeDto.get();
            if (dto != null) {
                var task = new CommitteeDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committee"),
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
