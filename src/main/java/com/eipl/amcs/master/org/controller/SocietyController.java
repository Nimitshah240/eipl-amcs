package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.service.BankService;
import com.eipl.amcs.master.org.service.BranchService;
import com.eipl.amcs.master.org.service.SocietyService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class SocietyController implements MyInitialization {

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
    Button btnClose, btnSave;
    @FXML
    private StackPane root;

    private Society dto;

    private BankService bankService;
    private SocietyService societyService;
    private BranchService branchService;

    public SocietyController() {
        bankService = context.getBean(BankService.class);
        societyService = context.getBean(SocietyService.class);
        branchService = context.getBean(BranchService.class);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    private ObservableList<Bank> bankList = FXCollections.observableArrayList();
    private ObservableList<Branch> branchList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadData();
        loadBank();
        loadBranch();
        setupComboBox();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnSave.setOnAction(e -> {
            saveData();
        });
    }

    @Override
    public void setupTable() {
        try {
            tableSociety.setEditable(true);
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colCodeEx.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodeEx()));
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colName.setCellFactory(TextFieldTableCell.forTableColumn());
            colName.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setName(e.getNewValue());
            });
            colShortName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShortName()));
            colShortName.setCellFactory(TextFieldTableCell.forTableColumn());
            colShortName.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setShortName(e.getNewValue());
            });
            colShortNameLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getShortNameLocal()));
            colShortNameLocal.setCellFactory(TextFieldTableCell.forTableColumn());
            colShortNameLocal.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setShortNameLocal(e.getNewValue());
            });
            colRegistrationCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
            colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
            colEmail.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setEmail(e.getNewValue());
            });
            colPhoneNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));
            colPhoneNo.setCellFactory(TextFieldTableCell.forTableColumn());
            colPhoneNo.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setPhoneNo(e.getNewValue());
            });
            colContactPerson.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));
            colContactPerson.setCellFactory(TextFieldTableCell.forTableColumn());
            colContactPerson.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setContactPerson(e.getNewValue());
            });
            colContactPersonMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPersonMobileNo()));
            colContactPersonMobileNo.setCellFactory(TextFieldTableCell.forTableColumn());
            colContactPersonMobileNo.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setContactPersonMobileNo(e.getNewValue());
            });
            colPinCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPincode()));
            colPinCode.setCellFactory(TextFieldTableCell.forTableColumn());
            colPinCode.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setPincode(e.getNewValue());
            });
            colAcNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBankAccountNo()));
            colAcNo.setCellFactory(TextFieldTableCell.forTableColumn());
            colAcNo.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setBankAccountNo(e.getNewValue());
            });
            colIfsc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIfsc()));
            colIfsc.setCellFactory(TextFieldTableCell.forTableColumn());
            colIfsc.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setIfsc(e.getNewValue());
            });
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colLocalName.setCellFactory(TextFieldTableCell.forTableColumn());
            colLocalName.setOnEditCommit(e -> {
                Society s = e.getRowValue();
                if (e.getNewValue() != null && !e.getNewValue().equalsIgnoreCase(""))
                    s.setNameLocal(e.getNewValue());
            });
            colBank.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBank()));
            colBank.setCellFactory(ComboBoxTableCell.forTableColumn(bankConverter, bankList));
            colBank.setOnEditCommit(event -> {
                Society obj = event.getRowValue();
                obj.setBank(event.getNewValue());
            });
            colBranch.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBranch()));
            colBranch.setCellFactory(ComboBoxTableCell.forTableColumn(branchConverter, branchList));
            colBranch.setOnEditCommit(event -> {
                Society obj = event.getRowValue();
                obj.setBranch(event.getNewValue());
            });
            colRegistrationDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRegistrationDate()));
            colRegistrationDate.setCellFactory(new LocalDateCellFactory<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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


    public void loadBank() {
        List<Bank> list = bankService.findAll();
        if (list != null) {
            bankList.addAll(list);
        }
    }

    public void loadBranch() {
        try {
            List<Branch> list = branchService.findAll();
            if (list != null) {
                branchList.addAll(list);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadData() {
        try {
            List<Society> list = societyService.findAll();
            if (list != null) {
                tableSociety.setItems(FXCollections.observableList(list));
                dto = list.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void saveData() {
        try {
            societyService.save(tableSociety.getItems().get(0));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


