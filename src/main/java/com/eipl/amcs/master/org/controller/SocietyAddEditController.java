package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.convertor.BranchConvertor;
import com.eipl.amcs.master.org.convertor.RouteConvertor;
import com.eipl.amcs.master.org.model.*;
import com.eipl.amcs.master.org.task.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SocietyAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private TabPane tabPane;
    @FXML
    private E_ComboBox<Bank> cboxBank;
    @FXML
    private E_ComboBox<Branch> cboxBranch;
    @FXML
    private E_ComboBox<Route> cboxRoute;
    @FXML
    private TableView<BmcChillerInfo> tableBmcChillerInfo;
    @FXML
    private TableColumn<BmcChillerInfo, String> colBmcFacilator, colBcuCapacity, colOwnerName, colAgreementPeriod, colAgreementFromDate, colAgreementToDate;
    @FXML
    private Tab tabSocietyDetail, tabContactDetail, tabOtherDetail;
    @FXML
    private TextField txtSocietyCode, txtSocietyName, txtShortName,
            txtSocietyNameLocal, txtShortNameLocal, txtFssaiCode, txtIfscCode,
            txtSapNo, txtBankAccNo, txtAdharCard, txtRegistrationCode,
            txtAddress, txtPhoneNo, txtEmail, txtChairmanMobileNo, txtChairmanName, txtGstNo, txtPan,
            txtSecretaryName, txtSecretaryMobileNo, txtBmcFacilator, txtBcuCapacity, txtOwnerName, txtAgreementPeriod;
    @FXML
    private DatePicker dpStartYear, dpFssaiExpiryDate, dpRegistrationDate, dpAgreementFromDate, dpAgreementToDate;
    @FXML
    private Button btnSave, btnClose;
    @FXML
    private E_Button btnAdd, btnDelete;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private User user;
    private StringBuilder errorMsg = null;

    private ContactDetails chairman;
    private ContactDetails secretary;
    List<ContactDetails> contactDetailsList = new ArrayList<>();
    private Society society;

    private BmcChillerInfo chillerInfo;
    private List<BmcChillerInfo> chillerInfoList = new ArrayList<>();

    private final ObjectProperty<BmcChillerInfo> propBmcChiller;


    public SocietyAddEditController() {
        propBmcChiller = new SimpleObjectProperty<>();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setSociety(Society society) {
        if (society != null) {
            this.society = society;
            loadControls();
            btnSave.setText(resourceBundle.getString("update"));
//            this.chillerInfo = society.getBmc().getChillerInfoList().get(0);
//            btnUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupComboBox();
        loadBank();
        loadRoute();
        loadBranch();
        setupTable();
        loadBmcChillerInfo();

        btnClose.setOnAction(e -> this.stage.close());
        btnSave.setOnAction(e -> validateAndSave());
        btnAdd.setOnAction(e -> addBmcChillerInfo());
        btnDelete.setOnAction(e -> {
            BmcChillerInfo dto = propBmcChiller.get();
            if (dto != null)
                deleteBmcChillerInfo(dto);
        });

        tableBmcChillerInfo.setOnKeyPressed(event -> {
            BmcChillerInfo dto = tableBmcChillerInfo.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propBmcChiller.get();
                    if (dto != null)
                        deleteBmcChillerInfo(dto);
                    break;
            }
        });
    }

    @Override
    public void setupComboBox() {
        cboxRoute.setConverter(new RouteConvertor(cboxRoute));
        cboxBank.setConverter(new BankConvertor(cboxBank));
        cboxBranch.setConverter(new BranchConvertor(cboxBranch));
    }

    @Override
    public void loadControls() {
        txtSocietyCode.setText(this.society.getCode());
        txtSocietyName.setText(this.society.getName());
        txtSocietyNameLocal.setText(this.society.getNameLocal());
        txtShortName.setText(this.society.getShortName());
        txtShortNameLocal.setText(this.society.getShortNameLocal());
        txtRegistrationCode.setText(this.society.getRegistrationCode());
        dpRegistrationDate.setValue(this.society.getRegistrationDate());
        txtSapNo.setText(this.society.getSapCenterCode());
        cboxRoute.getSelectionModel().select(this.society.getRoute());
        txtFssaiCode.setText(this.society.getFssaiCode());
        dpFssaiExpiryDate.setValue(this.society.getFssaiExpiryDate());
        cboxBank.getSelectionModel().select(this.society.getBank());
        cboxBranch.getSelectionModel().select(this.society.getBranch());
        txtBankAccNo.setText(this.society.getBankAccountNo());
        dpStartYear.setValue(this.society.getEffectiveDate());
        txtIfscCode.setText(this.society.getIfsc());

        txtAddress.setText(this.society.getAddress());
        txtPhoneNo.setText(this.society.getPhoneNo());
        txtEmail.setText(this.society.getEmail());
        txtGstNo.setText(this.society.getServiceTax());
        txtPan.setText(this.society.getPanNo());

        loadContactDetails();
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("user"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSave.getText().equals(resourceBundle.getString("update"))) {
            if (this.society != null) {
                society = setValuesInObject();
                saveData();
            }
        }
    }

    @Override
    public void setupTable() {
        colBcuCapacity.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getChillingCapacity())));
//        colAgreementPeriod.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAgreementFromDate().until(data.getValue().getAgreementToDate()))));
        colOwnerName.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getOwnerName())));
        colBmcFacilator.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getChillerName())));
        colAgreementFromDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAgreementFromDate())));
        colAgreementToDate.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getAgreementToDate())));
        propBmcChiller.bind(tableBmcChillerInfo.getSelectionModel().selectedItemProperty());

    }

    private void loadBank() {
        var task = new BankLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Bank> list = task.get();
                if (list != null) {
                    cboxBank.setItems(FXCollections.observableList(list));
                    if (society != null && society.getBank() != null)
                        cboxBank.getSelectionModel().select(society.getBank());

                    new AutoCompleteComboBoxListener<>(cboxBank);

                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadRoute() {
        var task = new RouteLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Route> list = task.get();
                if (list != null)
                    cboxRoute.setItems(FXCollections.observableList(list));
                if (society != null && society.getRoute() != null)
                    cboxRoute.getSelectionModel().select(society.getRoute());
                new AutoCompleteComboBoxListener<>(cboxRoute);

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadBranch() {
        var task = new BranchLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Branch> list = task.get();
                if (list != null)
                    cboxBranch.setItems(FXCollections.observableList(list));
                if (society != null && society.getBranch() != null)
                    cboxBranch.getSelectionModel().select(society.getBranch());
                new AutoCompleteComboBoxListener<>(cboxBranch);

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private Society setValuesInObject() {

        // Society Detail
        this.society.setCode(txtSocietyCode.getText());
        this.society.setName(txtSocietyName.getText());
        this.society.setNameLocal(txtSocietyNameLocal.getText());
        this.society.setShortName(txtShortName.getText());
        this.society.setShortNameLocal(txtShortNameLocal.getText());
        this.society.setRegistrationCode(txtRegistrationCode.getText());
        this.society.setRegistrationDate(dpRegistrationDate.getValue());
        this.society.setRoute(cboxRoute.getSelectionModel().getSelectedItem());
        this.society.setFssaiCode(txtFssaiCode.getText());
        this.society.setFssaiExpiryDate(dpFssaiExpiryDate.getValue());
        this.society.setSapCenterCode(txtSapNo.getText());
        this.society.setBank(cboxBank.getSelectionModel().getSelectedItem());
        this.society.setBranch(cboxBranch.getSelectionModel().getSelectedItem());
        this.society.setBankAccountNo(txtBankAccNo.getText());
        this.society.setIfsc(txtIfscCode.getText());
        this.society.setEffectiveDate(dpStartYear.getValue());

//        Contact details
        this.society.setAddress(txtAddress.getText());
        this.society.setPhoneNo(txtPhoneNo.getText());
        this.society.setEmail(txtEmail.getText());
        this.society.setServiceTax(txtGstNo.getText());
        this.society.setPanNo(txtPan.getText());
        chairman.setContactPerson(txtChairmanName.getText());
        chairman.setMobileNo(txtChairmanMobileNo.getText());
        secretary.setContactPerson(txtChairmanName.getText());
        secretary.setMobileNo(txtChairmanMobileNo.getText());
        contactDetailsList.add(chairman);
        contactDetailsList.add(secretary);

        return this.society;
    }

    private boolean validate() {

//        if (dpStartYear.getValue() == null)
//            errorMsg.append(resourceBundle.getString("mobile.cannot.be.empty.") + "\n");
//        if (dpRegistrationDate.getValue() == null)
//            errorMsg.append(resourceBundle.getString("mobile.cannot.be.empty.") + "\n");
//        if (txtRegistrationCode.getText() == null )
//            errorMsg.append(resourceBundle.getString("mobile.cannot.be.empty.") + "\n");
//
////
//        String newMobile = txtMobile.getText();
//        if (newMobile == null || newMobile.trim().isEmpty()) {
//            errorMsg.append(resourceBundle.getString("mobile.cannot.be.empty.") + "\n");
//        } else if (!newMobile.matches("\\d{10}")) {
//            errorMsg.append(resourceBundle.getString("Mobile.Number.must.be.10.digits.") + "\n");
//        }

//        String newPassword = txtPassword.getText();
//        if (newPassword == null || newPassword.trim().isEmpty()) {
//            errorMsg.append(resourceBundle.getString("Password.Cannot.Be.Empty.")+"\n");
//        }
//
        return errorMsg.length() == 0;
    }

    private void loadContactDetails() {
        var task = new ContactDetailLoadTask("Society", MainApp.identityDto.getIdentity().getSocietyRefCode(), "Chairman");
        task.setOnSucceeded(e -> {
            try {
                List<ContactDetails> list = task.get();
                if (list != null && !list.isEmpty()) {
                    chairman = list.get(0);
                    txtChairmanName.setText(chairman.getContactPerson());
                    txtChairmanMobileNo.setText(chairman.getMobileNo());
                } else {
                    chairman = new ContactDetails();
                    chairman.setModuleCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                    chairman.setModuleName("Society");
                    chairman.setDepartment("Chairman");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();


        var task1 = new ContactDetailLoadTask("Society", MainApp.identityDto.getIdentity().getSocietyRefCode(), "Secretary");
        task1.setOnSucceeded(e -> {
            try {
                List<ContactDetails> list = task1.get();
                if (list != null && !list.isEmpty()) {
                    secretary = list.get(0);
                    txtSecretaryName.setText(secretary.getContactPerson());
                    txtSecretaryMobileNo.setText(secretary.getMobileNo());
                } else {
                    secretary = new ContactDetails();
                    secretary.setModuleCode(MainApp.identityDto.getIdentity().getSocietyRefCode());
                    secretary.setModuleName("Society");
                    secretary.setDepartment("Secretary");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task1).start();

    }

    private void loadBmcChillerInfo() {
        var task = new BmcChillerInfoLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<BmcChillerInfo> list = task.get();
                if (list != null && !list.isEmpty()) {
                    chillerInfoList.addAll(list);
                    tableBmcChillerInfo.setItems(FXCollections.observableList(list));
                } else
                    tableBmcChillerInfo.setItems(FXCollections.observableList(chillerInfoList));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void addBmcChillerInfo() {
        try {

            BmcChillerInfo bmcChillerInfo = new BmcChillerInfo();
            bmcChillerInfo.setSociety(MainApp.identityDto.getSociety());
            bmcChillerInfo.setChillerName(txtBmcFacilator.getText());
            bmcChillerInfo.setChillingCapacity(Integer.valueOf(txtBcuCapacity.getText()));
            bmcChillerInfo.setOwnerName(txtOwnerName.getText());
            bmcChillerInfo.setAgreementFromDate(dpAgreementFromDate.getValue());
            bmcChillerInfo.setAgreementToDate(dpAgreementToDate.getValue());
//            bmcChillerInfo.setAgreementPeriod(Integer.valueOf(dpAgreementFromDate.getValue().until(dpAgreementToDate.getValue())));

            chillerInfoList.add(bmcChillerInfo);
            tableBmcChillerInfo.setItems(FXCollections.observableList(chillerInfoList));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveData() {
        SocietySaveTask task = new SocietySaveTask(society);
        task.setOnSucceeded(e -> {
            saveBmcChillerInfo();
            MyAlert alert = new InformationAlert(MainApp.stage, resourceBundle.getString("society"),
                    "Success");
            alert.createAlert();
            this.callback.reloadData(true);
            this.stage.close();
        });
        task.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("society"),
                    "ERROR");
            alert.createAlert();
        });
        new Thread(task).start();


        ContactDetailSaveTask task1 = new ContactDetailSaveTask(contactDetailsList);
        task1.setOnSucceeded(e -> {
        });
        task1.setOnFailed(e -> {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("society"),
                    "ERROR");
            alert.createAlert();
        });
        new Thread(task1).start();
    }

    private void saveBmcChillerInfo() {
        try {
            var task = new BmcChillerInfoSaveTask(chillerInfoList);
            task.setOnSucceeded(e -> {
                this.callback.reloadData(true);
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteBmcChillerInfo(BmcChillerInfo bmcChillerInfo) {
        try {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("society"),
                    resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {

                if (bmcChillerInfo.getChillerInfoCode() != null && bmcChillerInfo.getChillerInfoCode() != 0) {
                    var task = new BmcChillerInfoDeleteTask(bmcChillerInfo.getChillerInfoCode());
                    task.setOnSucceeded(e -> {
                        try {
                            Boolean respDelete = task.get();
                            if (respDelete == null || !respDelete.booleanValue()) {
                                MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("society"),
                                        resourceBundle.getString("error.occurred"));
                                alert1.createAlert();
                                return;
                            }
                            chillerInfoList.remove(bmcChillerInfo);
                            this.callback.reloadData(true);

                            tableBmcChillerInfo.setItems(FXCollections.observableList(chillerInfoList));
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    });
                    new Thread(task).start();
                } else {
                    chillerInfoList.remove(bmcChillerInfo);
                    tableBmcChillerInfo.setItems(FXCollections.observableList(chillerInfoList));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
