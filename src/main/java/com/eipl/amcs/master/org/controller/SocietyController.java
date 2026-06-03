package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.master.org.task.BranchLoadTask;
import com.eipl.amcs.master.org.task.SocietyLoadTask;
import com.eipl.amcs.master.org.task.SocietySaveTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SocietyController implements MyInitialization, PopupCallback {

    private final ObservableList<Bank> bankList = FXCollections.observableArrayList();
    private final StringConverter<Bank> bankConverter = new StringConverter<Bank>() {
        @Override
        public String toString(Bank bank) {
            if (bank == null)
                return null;
            return bank.toString();
        }

        @Override
        public Bank fromString(String s) {
            if (s == null || s.isEmpty())
                return null;
            return bankList.stream().filter(p -> s.equals(p.toString())).findAny().orElse(null);
        }

    };
    private final ObservableList<Branch> branchList = FXCollections.observableArrayList();
    private final StringConverter<Branch> branchConverter = new StringConverter<>() {
        @Override
        public String toString(Branch object) {
            if (object == null)
                return null;
            return object.toString();
        }

        @Override
        public Branch fromString(String string) {
            if (string == null || string.isEmpty())
                return null;
            return branchList.stream().filter(p -> p.toString().equalsIgnoreCase(string))
                    .findFirst().orElse(null);
        }
    };
    @FXML
    TableView<Society> tableSociety;
    @FXML
    TableColumn<Society, String> colCode, colName, colLocalName, colCodeEx, colShortName, colShortNameLocal,
            colRegistrationCode, colPhoneNo, colPinCode, colContactPerson, colEmail, colContactPersonMobileNo, colAcNo, colIfsc;
    @FXML
    TableColumn<Society, Branch> colBranch;
    @FXML
    TableColumn<Society, Bank> colBank;
    @FXML
    TableColumn<Society, LocalDate> colRegistrationDate;
    @FXML
    E_Button btnClose, btnEdit;
    @FXML
    private StackPane root;
    private Society dto;
    private ResourceBundle resourceBundle;
    private final ObjectProperty<Society> propSociety;

    @Override
    public Node getRoot() {
        return root;
    }

    public SocietyController() {
        propSociety = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadData();
        loadBank();
        loadBranch();
        setupComboBox();
        this.resourceBundle = resourceBundle;
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnEdit.setOnAction(e -> {
            Society dto = propSociety.get();
            editSociety(dto);
        });

        tableSociety.setRowFactory(tv -> {
            TableRow<Society> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Society data = row.getItem();
                    editSociety(data);
                }
            });
            return row;
        });

        tableSociety.setOnKeyPressed(event -> {
            Society dto = tableSociety.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case ENTER:
                    editSociety(dto);
                    break;
            }
        });

    }

    private void editSociety(Society society) {
        if (society != null)
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "SocietyAddEdit", dto, this, resourceBundle.getString("society"));
    }

    @Override
    public void setupComboBox() {

    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colCodeEx.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colShortName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShortName()));
            colShortNameLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShortNameLocal()));
            colRegistrationCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
            colPhoneNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));
            colContactPerson.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));
            colContactPersonMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPersonMobileNo()));
            colPinCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPincode()));
            colAcNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBankAccountNo()));
            colIfsc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIfsc()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colBank.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBank()));
            colBranch.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBranch()));
            colRegistrationDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRegistrationDate()));
            colRegistrationDate.setCellFactory(new LocalDateCellFactory<>());
            propSociety.bind(tableSociety.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("Society setuptable Exception");
            e.printStackTrace();
        }
    }

    public void loadBank() {
        var task = new BankLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Bank> list = task.get();
                if (list != null) {
                    bankList.addAll(list);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadBranch() {
        var task = new BranchLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Branch> list = task.get();
                if (list != null) {
                    branchList.addAll(list);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void loadData() {
        var task = new SocietyLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Society> list = task.get();
                if (list != null) {
                    tableSociety.setItems(FXCollections.observableList(list));
                    dto = list.get(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void saveData() {
        SocietySaveTask task = new SocietySaveTask(tableSociety.getItems().get(0));
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.stage, resourceBundle.getString("society"),
                    "Success");
            alert.createAlert();
        });
        task.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("society"),
                    "ERROR");
            alert.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}


