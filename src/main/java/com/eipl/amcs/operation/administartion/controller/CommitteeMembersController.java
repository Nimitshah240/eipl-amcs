package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.operation.administartion.task.CommitteeMembersDeleteTask;
import com.eipl.amcs.operation.administartion.task.CommitteeMembersLoadTask;
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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CommitteeMembersController implements MyInitialization, PopupCallback {
    private final ObjectProperty<CommitteeMembers> propCommitteMembertDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<CommitteeMembers> tableCommitteeMembers;
    @FXML
    TableColumn<CommitteeMembers, String> colDesignation, colMembername;
    @FXML
    DatePicker dpDate;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnRegister;
    private ResourceBundle resourceBundle;


    public CommitteeMembersController() {
        propCommitteMembertDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpDate.setValue(LocalDate.now());
        propCommitteMembertDto.addListener((observable, oldValue, newValue) -> {
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
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CommitteeMembersAddEdit", null, this, "Committee Member");
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            CommitteeMembers dto = propCommitteMembertDto.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CommitteeMembersAddEdit", dto, this);
        });

        btnRegister.setOnAction(e -> {
            validateAndGenerateReport();
        });
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
            colMembername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMemberName()));
            colDesignation.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDesignation().getName()));
            propCommitteMembertDto.bind(tableCommitteeMembers.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("CommiteMembers setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        CommitteeMembersLoadTask task = new CommitteeMembersLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<CommitteeMembers> list = task.get();
                if (list != null)
                    tableCommitteeMembers.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            CommitteeMembers dto = propCommitteMembertDto.get();
            if (dto != null) {
                var task = new CommitteeMembersDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
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
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
