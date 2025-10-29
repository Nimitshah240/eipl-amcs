package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.eipl.amcs.master.account.model.StaffSalaryMapping;
import com.eipl.amcs.operation.administartion.converter.StaffMemberConvertor;
import com.eipl.amcs.operation.administartion.task.StaffMembersLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffSalaryHeadLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffSalaryMappingLoadTask;
import com.eipl.amcs.operation.administartion.task.StaffSalaryMappingSaveTask;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class StaffSalaryMappingController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<StaffSalaryHead> tableStaffSalaryMapping;
    @FXML
    TableColumn<StaffSalaryHead, String> colSalaryHead;
    @FXML
    TableColumn<StaffSalaryHead, Integer> colHeadType;
    @FXML
    TableColumn<StaffSalaryHead, String> colAmount;
    @FXML
    Button btnClose, btnSave;
    @FXML
    DatePicker dpDate;
    @FXML
    ComboBox<StaffMember> cboxStaff;
    private List<StaffMember> listStaffMembers;
    private StaffSalaryMapping dto;
    private List<StaffSalaryHead> listStaffSalaryHead;
    private List<StaffSalaryMapping> staffSalaryMappingList;
    @FXML
    private GridPane gridMaster;
    private final StringBuilder errorMsg = null;
    private ResourceBundle resourceBundle;


    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        staffSalaryMappingList = new ArrayList<>();
        loadStaffMember();
        loadData();
        setupTable();
        setupComboBox();
        dpDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnSave.setOnAction(e -> {
            saveData();
        });
    }


    private void loadStaffMember() {
        var task = new StaffMembersLoadTask();
        task.setOnSucceeded(e -> {
            try {
                listStaffMembers = task.get();
                cboxStaff.getItems().addAll(listStaffMembers);

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadStaffSalaryMapping() {
        var task = new StaffSalaryMappingLoadTask();
        task.setOnSucceeded(e -> {
            try {
                staffSalaryMappingList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void loadData() {
        var task = new StaffSalaryHeadLoadTask();
        task.setOnSucceeded(e -> {
            try {
                listStaffSalaryHead = task.get();
                tableStaffSalaryMapping.setItems(FXCollections.observableList(listStaffSalaryHead));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        try {
            colSalaryHead.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colHeadType.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getType() == 0 ? "DEDUCTION" : "ADDITION"));
//        colHeadType.setCellValueFactory(data->new SimpleObjectProperty(data.getValue().getType()));
            colAmount.setCellValueFactory(data -> new SimpleStringProperty(""));
            colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
            colAmount.setOnEditCommit(e -> {
                StaffSalaryHead w = e.getRowValue();
                StaffSalaryMapping staffSalaryMapping = new StaffSalaryMapping();
                staffSalaryMapping.setAmount(new BigDecimal(e.getNewValue()));
                staffSalaryMapping.setStaffSalaryHead(w);
                staffSalaryMapping.setStaffMember(cboxStaff.getValue());
                staffSalaryMapping.setSociety(MainApp.identityDto.getSociety());
                staffSalaryMapping.setUnionCode(MainApp.identityDto.getUnion().getCode());
                staffSalaryMapping.setWefDate(dpDate.getValue());
                staffSalaryMapping.setActive(true);
                staffSalaryMappingList.add(staffSalaryMapping);
            });
        } catch (Exception e) {
            System.out.println("StaffSalaryMapping setuptable Exception");
            e.printStackTrace();
        }
    }

    private boolean validate() {

//        if (txtMinuteOfMeeting.getText().trim() == null || txtMinuteOfMeeting.getText().trim().isEmpty())
//            errorMsg.append(resourceBundle.getString("minutesofmeetingnullerror") + "\n");

        return errorMsg.length() == 0;

    }


    @Override
    public void saveData() {
        System.out.println(staffSalaryMappingList);
        var task = new StaffSalaryMappingSaveTask(staffSalaryMappingList, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("staffsalarymapping"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("staffsalarymapping"),
                        resourceBundle.getString("staffsalarymapping.insert.successful"));
                alert.createAlert();
                loadData();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxStaff.setConverter(new StaffMemberConvertor(cboxStaff));
    }


}
