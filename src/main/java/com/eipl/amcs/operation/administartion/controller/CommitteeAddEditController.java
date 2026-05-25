package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.*;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Committee;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.master.account.model.Designation;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.operation.administartion.converter.DesignationConvertor;
import com.eipl.amcs.operation.administartion.task.*;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class CommitteeAddEditController implements MyInitialization {


    private final ObjectProperty<CommitteeMembers> propCommitteMembertDto;

    @FXML
    private StackPane root;
    @FXML
    private E_Button btnClose, btnSave, btnAdd, btnDelete,btnExport;
    @FXML
    private E_ComboBox<Designation> cboxDesignation;
    @FXML
    private E_TextField txtCommMemberCode, txtCommitteeCode, txtName, txtNameLocal, txtMemberName, txtCode, txtMemberCode;
    @FXML
    private E_DatePicker dpElectionDate, dpFormation, dpJoiningDate, dpRegistrationdate;

    private Committee committee;
    private List<CommitteeMembers> members = new ArrayList<>();

    @FXML
    TableView<CommitteeMembers> tableCommitteeMembers;
    @FXML
    TableColumn<CommitteeMembers, String> colCode, colJoiningDate, colRegistrationDate, colDesignation, colMemberName;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private CommitteeMembers dto = null;

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

    public CommitteeAddEditController() {
        propCommitteMembertDto = new SimpleObjectProperty<>();
    }


    public void setCommittee(Committee dto) {
        if (dto != null) {
            this.committee = dto;
            btnSave.setText(resourceBundle.getString("update"));
            this.members = committee.getMembers();
            loadControls();
        } else {
            committeeNextCode();
        }
        loadDesignation();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadDesignation();
        setupComboBox();
        setupTable();
//        cboxDesignation.setConverter(new DesignationConvertor(cboxDesignation));
//        cboxDesignation.getSelectionModel().select(0);
        dpElectionDate.setValue(LocalDate.now());
        dpFormation.setValue(LocalDate.now());
        dpJoiningDate.setValue(LocalDate.now());
        dpRegistrationdate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> {
            this.callback.reloadData(true);
            this.stage.close();
        });
        btnSave.setOnAction(e -> validateAndSave());
        btnAdd.setOnAction(e -> addCommitteeMembers());
        btnExport.setOnAction(e -> {
            exportExcel(tableCommitteeMembers.getItems());
        });
        btnDelete.setOnAction(e -> {
            CommitteeMembers dto = propCommitteMembertDto.get();
            if (dto != null)
                deleteCommitteeMembers(dto);
        });

        tableCommitteeMembers.setOnKeyPressed(event -> {
            CommitteeMembers dto = tableCommitteeMembers.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propCommitteMembertDto.get();
                    if (dto != null)
                        deleteCommitteeMembers(dto);
                    break;
            }
        });
        txtMemberCode.setOnAction(event -> {
            if (txtMemberCode.getText().length() > 0) {
                String code = generateCode(txtMemberCode.getText().trim());
                getNameFromMemberCode(code);
            }
        });

        txtMemberCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtMemberCode.getText().length() > 0) {
                String code = generateCode(txtMemberCode.getText().trim());
                getNameFromMemberCode(code);
            }
        });

    }

    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMemberName()));
            colDesignation.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDesignation().getName()));
            colJoiningDate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getJoiningDate()));
            colRegistrationDate.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getRegistrationDate()));
            propCommitteMembertDto.bind(tableCommitteeMembers.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void loadControls() {
        tableCommitteeMembers.setItems(FXCollections.observableList(committee.getMembers()));
        txtCommitteeCode.setText(committee.getCode());
        dpElectionDate.setValue(committee.getElectionDate());
        dpFormation.setValue(committee.getFormationDate());
        txtName.setText(committee.getName());
        txtNameLocal.setText(committee.getNameLocal());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committee"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSave.getText().equals(resourceBundle.getString("update"))) {
            if (this.committee != null) {
                committee = setValuesInObject();
                saveData();
            }
        } else {
            committee = new Committee();
            committee = setValuesInObject();
            saveData();
        }
    }

    private Committee setValuesInObject() {
        committee.setCode(txtCommitteeCode.getText());
        committee.setElectionDate(dpElectionDate.getValue());
        committee.setFormationDate(dpFormation.getValue());
        committee.setName(txtName.getText());
        committee.setNameLocal(txtNameLocal.getText());
        committee.setMembers(members);

        return committee;
    }

    private void addCommitteeMembers() {
        try {
            errorMsg = new StringBuilder();
            if (!validateCommitteeMember()) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                        errorMsg.toString());
                alert.createAlert();
                return;
            }

            CommitteeMembers member = new CommitteeMembers();
            member.setMemberName(txtMemberName.getText());
            member.setMemberCode(txtCommMemberCode.getText());
            member.setDesignation(cboxDesignation.getSelectionModel().getSelectedItem());
            member.setRegistrationDate(dpRegistrationdate.getValue());
            member.setJoiningDate(dpJoiningDate.getValue());

            members.add(member);
            tableCommitteeMembers.setItems(FXCollections.observableList(members));

            txtCommMemberCode.setText("");
            txtMemberName.setText("");
            cboxDesignation.getSelectionModel().clearSelection();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCommitteeMembers(CommitteeMembers committeeMembers) {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {

                if (committeeMembers.getCode() != null && !committeeMembers.getCode().isBlank()) {
                    var task = new CommitteeMembersDeleteTask(committeeMembers.getCode());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                                        resourceBundle.getString("error.occurred"));
                                alert1.createAlert();
                                return;
                            }
                            members.remove(committeeMembers);
                            this.callback.reloadData(true);

                            tableCommitteeMembers.setItems(FXCollections.observableList(members));
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(task).start();
                } else {
                    members.remove(committeeMembers);
                    tableCommitteeMembers.setItems(FXCollections.observableList(members));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validate() {
        if (txtName.getText() == null || txtName.getText().isBlank())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
        if (dpElectionDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("electiondateullerror") + "\n");
        if (dpFormation.getValue() == null)
            errorMsg.append(resourceBundle.getString("formationdateullerror") + "\n");
        return errorMsg.length() == 0;
    }

    private boolean validateCommitteeMember() {
        if (txtMemberName.getText() == null || txtMemberName.getText().isBlank())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
        if (cboxDesignation.getValue() == null)
            errorMsg.append(resourceBundle.getString("designationnullerror") + "\n");
        if (dpJoiningDate.getValue() == null)
            errorMsg.append(resourceBundle.getString("joiningdateullerror") + "\n");

        return errorMsg.length() == 0;

    }

    @Override
    public void saveData() {

        var task = new CommitteeSaveTask(committee, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                        resourceBundle.getString("committeemembers.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new CommitteeMembersSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                        resourceBundle.getString("committeemembers.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        task.setOnFailed(event -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxDesignation.setConverter(new DesignationConvertor(cboxDesignation));
        cboxDesignation.getSelectionModel().select(0);
    }

    private void loadDesignation() {
        var task = new DesignationLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Designation> list = task.get();
                if (list != null) {

                    List<String> filter = Arrays.asList("President", "Vice President", "Committee Member", "Local Auditor", "Fat Machine Operator", "Milk Collector");
                    List<Designation> filteredList = list.stream()
                            .filter(designation -> filter.contains(designation.getName()))
                            .collect(Collectors.toList());
                    cboxDesignation.setItems(FXCollections.observableList(filteredList));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void committeeNextCode() {
        var task = new CommitteeGetNextCodeLoadTask(MainApp.identityDto.getSociety());
        task.setOnSucceeded(e -> {
            try {
                String code = task.get();
                txtCommitteeCode.setText(code);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void getNameFromMemberCode(String code) {
        var task = new MemberByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    Member member = task.get();
                    txtMemberName.setText(member.toMemberName());
                } else {
                    txtMemberName.clear();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committee"),
                            resourceBundle.getString("membernotfound"));
                    alert.createAlert();
                    txtMemberCode.setText("");
                    //FocusUtils.requestFocus(txtMemberCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private String generateCode(String code) {
        return MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(code));
    }

    private void exportExcel(List<CommitteeMembers> list) {
        if (list == null || list.isEmpty()) {
            new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committeemembers"), "No data available to export").createAlert();
            return;
        }

        boolean exported = true;
        boolean cancelled = false;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Committee Members");
            fileDialog.setInitialFileName("Committee_Members_List.xls");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.getStage());

            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Members");

                List<String> headers = Arrays.asList("Code", "Member Name", "Designation", "Joining Date", "Registration Date");

                // Create header row
                HSSFRow headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.size(); i++) {
                    HSSFCell cell = headerRow.createCell(i);
                    cell.setCellValue(headers.get(i));
                }

                // Fill data rows
                int rowCnt = 1;
                for (CommitteeMembers item : list) {
                    HSSFRow row = sheet.createRow(rowCnt++);
                    for (int i = 0; i < headers.size(); i++) {
                        HSSFCell cell = row.createCell(i);
                        switch (headers.get(i)) {
                            case "Code":
                                cell.setCellValue(item.getCode() != null ? item.getCode() : "");
                                break;
                            case "Member Name":
                                cell.setCellValue(item.getMemberName() != null ? item.getMemberName() : "");
                                break;
                            case "Designation":
                                cell.setCellValue(item.getDesignation() != null ? item.getDesignation().getName() : "");
                                break;
                            case "Joining Date":
                                cell.setCellValue(item.getJoiningDate() != null ? item.getJoiningDate().toString() : "");
                                break;
                            case "Registration Date":
                                cell.setCellValue(item.getRegistrationDate() != null ? item.getRegistrationDate().toString() : "");
                                break;
                        }
                    }
                }

                // Write to file
                try (FileOutputStream out = new FileOutputStream(file)) {
                    wb.write(out);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    exported = false;
                } finally {
                    try {
                        wb.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                cancelled = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exported = false;
        }

        showExportStatus(exported, cancelled);
    }

    private void showExportStatus(boolean exported, boolean cancelled) {
        MyAlert alert;
        String title = resourceBundle.getString("committeemembers");
        if (cancelled) {
            alert = new InformationAlert(MainApp.getStage(), title,
                    resourceBundle.containsKey("export.cancelled") ? resourceBundle.getString("export.cancelled") : "Export Cancelled");
        } else if (exported) {
            alert = new InformationAlert(MainApp.getStage(), title,
                    resourceBundle.containsKey("successful") ? resourceBundle.getString("successful") : "Export Successful");
        } else {
            alert = new ErrorAlert(MainApp.getStage(), title,
                    resourceBundle.containsKey("error.occurred") ? resourceBundle.getString("error.occurred") : "Error occurred during export");
        }
        alert.createAlert();
    }
}
