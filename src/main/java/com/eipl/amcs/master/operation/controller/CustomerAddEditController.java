package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.geo.converter.DistrictConvertor;
import com.eipl.amcs.master.geo.converter.StateConvertor;
import com.eipl.amcs.master.geo.converter.SubDistrictConvertor;
import com.eipl.amcs.master.geo.converter.VillageConvertor;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.task.DistrictLoadTask;
import com.eipl.amcs.master.geo.task.StateLoadTask;
import com.eipl.amcs.master.geo.task.SubDistrictLoadTask;
import com.eipl.amcs.master.geo.task.VillageLoadTask;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.task.CustomerCodeLoadTask;
import com.eipl.amcs.master.operation.task.CustomerDetailLoadTask;
import com.eipl.amcs.master.operation.task.CustomerSaveTask;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.convertor.BranchConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.master.org.task.BranchLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class CustomerAddEditController implements MyInitialization {

    @FXML
    private AnchorPane root;
    @FXML
    private VBox vbox;
    @FXML
    private DatePicker dpRegistrationDate;
    @FXML
    private TextArea txtAddress;
    @FXML
    private RadioButton rbtnCash, rbtnBank;
    @FXML
    private ToggleGroup paymentType;
    @FXML
    private ComboBox<Union> cboxUnion;
    @FXML
    private ComboBox<Society> cboxSociety;
    @FXML
    private ComboBox<State> cboxState;
    @FXML
    private ComboBox<District> cboxDistrict;
    @FXML
    private ComboBox<SubDistrict> cboxSubDistrict;
    @FXML
    private ComboBox<Village> cboxVillage;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxType;
    @FXML
    private ComboBox<Bank> cboxBankName;
    @FXML
    private ComboBox<Branch> cboxBranchName;
    @FXML
    private TextField txtCst, txtCode, txtMobileNo, txtPincode,
            txtName, txtLocalName,
            txtEmail, txtPanNo, txtAadharCardNo, txtAcNo, txtIfsc, txtRegistrationNo;
    @FXML
    private Button btnSaveUpdate, btnClose;
    @FXML
    private GridPane gridBankDetail;

    private Stage stage;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private CustomerDto dto = null;
    private Customer customer = null;
    private CustomerDetails customerDetail = null;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setCustomer(Customer customer) {
        if (customer != null) {
            this.customer = customer;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadCustomerDetail(customer.getCode());
            loadCustomerType();
        } else {
            getNextCustomerCode();
            loadData();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        gridBankDetail.setDisable(true);
//        cboxType.setItems(FXCollections.observableList(CommonUtils.getCustomerTypesForCustomerCreate()));

        setupComboBox();
        loadState();
        loadCustomerType();
        loadBank();

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/customer.fxml")));
        });
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        cboxState.setOnAction(e -> {
            if (cboxState.getValue() != null) {
                cboxDistrict.getItems().clear();
                cboxDistrict.valueProperty().set(null);
                cboxSubDistrict.getItems().clear();
                cboxSubDistrict.valueProperty().set(null);
                cboxVillage.getItems().clear();
                cboxVillage.valueProperty().set(null);
                loadDistrict(cboxState.getValue());
            }
        });
        cboxDistrict.setOnAction(event -> {
            if (cboxDistrict.getValue() != null) {
                cboxSubDistrict.getItems().clear();
                cboxSubDistrict.valueProperty().set(null);
                cboxVillage.getItems().clear();
                cboxVillage.valueProperty().set(null);
                loadSubDistrict(cboxDistrict.getSelectionModel().getSelectedItem());
            }
        });
        cboxSubDistrict.setOnAction(event -> {
            if (cboxSubDistrict.getValue() != null) {
                cboxVillage.getItems().clear();
                cboxVillage.valueProperty().set(null);
                loadVillage(cboxSubDistrict.getSelectionModel().getSelectedItem());
            }
        });
        rbtnCash.selectedProperty().addListener((observablevalue, oldvalue, newvalue) -> {
            if (newvalue) {
                gridBankDetail.setDisable(true);
                cboxBankName.valueProperty().set(null);
                cboxBranchName.valueProperty().set(null);
                txtAcNo.clear();
                txtIfsc.clear();
            } else {
                gridBankDetail.setDisable(false);
            }
        });
        cboxBankName.setOnAction(event -> {
            if (cboxBankName.getValue() != null) {
                cboxBranchName.getItems().clear();
                cboxBranchName.valueProperty().set(null);
                loadBranch(cboxBankName.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            setValuesInObject();
            updateData();
        } else {
            customer = new Customer();
            customerDetail = new CustomerDetails();
            setValuesInObject();
            saveData();
        }
    }

    public void setValuesInControls() {
        loadCustomerType();
        txtCode.setText(customer.getCode());
        dpRegistrationDate.setValue(customer.getRegistrationDate());
        txtMobileNo.setText(customer.getMobileNo());
        txtRegistrationNo.setText(customer.getRegistrationNo());
        txtName.setText(customer.getName());
        txtCst.setText(customerDetail.getCstNo());
        txtLocalName.setText(customer.getNameLocal());
        txtAddress.setText(customerDetail.getAddress());
        txtEmail.setText(customerDetail.getEmail());
        txtPincode.setText(customerDetail.getPincode());
        txtPanNo.setText(customerDetail.getPanNo());
        cboxState.getSelectionModel().select(customerDetail.getState());
        cboxDistrict.getSelectionModel().select(customerDetail.getDistrict());
        cboxSubDistrict.getSelectionModel().select(customerDetail.getSubDistrict());
        cboxVillage.getSelectionModel().select(customerDetail.getVillage());
        txtAadharCardNo.setText(customerDetail.getAadharCardNo());
        cboxBankName.getSelectionModel().select(customerDetail.getBank());
        cboxBranchName.getSelectionModel().select(customerDetail.getBranch());
        txtAcNo.setText(customerDetail.getAccountNo());
        txtIfsc.setText(customerDetail.getIfsc());
    }

    private void loadCustomerDetail(String code) {
        var task = new CustomerDetailLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                CustomerDetails md = task.get();
                if (md != null) {
                    this.customerDetail = md;
                    loadData();
                    setValuesInControls();
                } else {
                    loadData();
                    setValuesInControls();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void saveData() {
        var task = new CustomerSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                        resourceBundle.getString("customer.insert.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Customer.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new CustomerSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                        resourceBundle.getString("customer.update.successful"));
                alert.createAlert();
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Customer.fxml")));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNextCustomerCode() {
        var task = new CustomerCodeLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty())
                    return;
                txtCode.setText(nextCode);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void setValuesInObject() {
        customer.setCode(txtCode.getText());
        customerDetail.setCode(txtCode.getText());
        customer.setSociety(MainApp.identityDto.getSociety());
        customer.setUnion(MainApp.identityDto.getUnion());
//        customer.setType((cboxType.getSelectionModel().getSelectedIndex() == 0 ? 3 :
//                (cboxType.getSelectionModel().getSelectedIndex() == 1 ? 4 : 5)));
        customer.setType((int) cboxType.getValue().getKey());
        customer.setRegistrationDate(dpRegistrationDate.getValue());
        customer.setMobileNo(txtMobileNo.getText());
        customer.setPaymentMode((rbtnBank.isSelected() ? 1 : 0));
        customerDetail.setBank(cboxBankName.getValue());
        customerDetail.setBranch(cboxBranchName.getValue());
        customerDetail.setAccountNo(txtAcNo.getText());
        customerDetail.setIfsc(txtIfsc.getText());
        customerDetail.setAddress(txtAddress.getText());
        customerDetail.setState(cboxState.getValue());
        customerDetail.setSubDistrict(cboxSubDistrict.getValue());
        customerDetail.setDistrict(cboxDistrict.getValue());
        customerDetail.setVillage(cboxVillage.getValue());
        customerDetail.setEmail(txtEmail.getText());
        customerDetail.setPincode(txtPincode.getText());
        customerDetail.setPanNo(txtPanNo.getText());
        customer.setRegistrationNo(txtRegistrationNo.getText());
        customerDetail.setCstNo(txtCst.getText());
        customer.setName(txtName.getText());
        customer.setNameLocal(txtLocalName.getText());
        customerDetail.setAadharCardNo(txtAadharCardNo.getText());
        customerDetail.setCustomer(customer);
        customerDetail.setCode(customer.getCode());
        customer.setActive(true);
        dto = new CustomerDto(customer, customerDetail);
    }

    private boolean validate() {
        if (cboxType.getValue() == null)
            errorMsg.append(resourceBundle.getString("customertypenullerror") + "\n");
        if (txtCode.getText() == null || txtCode.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("codenullerror") + "\n");
//        if (dpRegistrationDate.getValue() == null)
//            errorMsg.append(resourceBundle.getString("regdatenullerror") + "\n");
//        if (txtRegistrationNo.getText() == null || txtRegistrationNo.getText().isEmpty())
//            errorMsg.append(resourceBundle.getString("regnonullerror") + "\n");
//        if (txtCst.getText() == null || txtCst.getText().isEmpty())
//            errorMsg.append(resourceBundle.getString("cstnullerror") + "\n");
//        if (txtAddress.getText() == null || txtAddress.getText().isEmpty())
//            errorMsg.append(resourceBundle.getString("addressnullerror") + "\n");
        if (txtName.getText() == null || txtName.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
//        if (cboxState.getValue() == null)
//            errorMsg.append(resourceBundle.getString("statenullerror") + "\n");
//        if (cboxDistrict.getValue() == null)
//            errorMsg.append(resourceBundle.getString("districtnullerror") + "\n");
//        if (cboxSubDistrict.getValue() == null)
//            errorMsg.append(resourceBundle.getString("subdistrictnullerror") + "\n");
//        if (cboxVillage.getValue() == null)
//            errorMsg.append(resourceBundle.getString("villagenullerror") + "\n");
//        if (txtPanNo.getText() == null || txtPanNo.getText().isEmpty())
//            errorMsg.append(resourceBundle.getString("pannonullerror") + "\n");
//        if (txtEmail.getText() == null || txtEmail.getText().isEmpty())
//            errorMsg.append(resourceBundle.getString("emailnullerror") + "\n");
//
//        if (txtMobileNo.getText() == null || txtMobileNo.getText().isEmpty() || Long.parseLong(txtMobileNo.getText()) >= 10000000000L || Long.parseLong(txtMobileNo.getText()) <= 999999999L)
//            errorMsg.append(resourceBundle.getString("mobilenonullerror") + "\n");
//
//        if (txtAadharCardNo.getText() == null || txtAadharCardNo.getText().isEmpty())
//            errorMsg.append(resourceBundle.getString("aadharcardnonullerror") + "\n");
//        if (txtPincode.getText() == null || txtAadharCardNo.getText().isEmpty())
//            errorMsg.append(resourceBundle.getString("pincodenullerror") + "\n");
//        if (rbtnBank.isSelected()) {
//            if (cboxBankName.getValue() == null) {
//                errorMsg.append(resourceBundle.getString("banknullerror") + "\n");
//            }
//            if (cboxBranchName.getValue() == null) {
//                errorMsg.append(resourceBundle.getString("branchnullerror") + "\n");
//            }
//            if (txtAcNo.getText() == null || txtAcNo.getText().trim().isEmpty()) {
//                errorMsg.append(resourceBundle.getString("acnonullerror") + "\n");
//            }
//            if (txtIfsc.getText() == null || txtIfsc.getText().trim().isEmpty()) {
//                errorMsg.append(resourceBundle.getString("ifscnonullerror") + "\n");
//            }
//        }
        return errorMsg.length() == 0;
    }

    @Override
    public void setupComboBox() {
        cboxState.setConverter(new StateConvertor(cboxState));
        cboxDistrict.setConverter(new DistrictConvertor(cboxDistrict));
        cboxSubDistrict.setConverter(new SubDistrictConvertor(cboxSubDistrict));
        cboxVillage.setConverter(new VillageConvertor(cboxVillage));
        cboxBankName.setConverter(new BankConvertor(cboxBankName));
        cboxBranchName.setConverter(new BranchConvertor(cboxBranchName));
        dpRegistrationDate.setConverter(new LocalDateConvertor());
        dpRegistrationDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpRegistrationDate.setValue(dpRegistrationDate.getConverter().fromString(dpRegistrationDate.getEditor().getText()));
            }
        });
        cboxType.setConverter(new CustomerTypeConvertor(cboxType));
    }


    private void loadState() {
        var task = new StateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<State> list = task.get();
                if (list != null) {
                    cboxState.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxState);

                    if (customerDetail != null)
                        cboxState.setValue(customerDetail.getState());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void loadCustomerType() {
        cboxType.setItems(FXCollections.observableList(CommonUtils.getCustomerTypesForCustomerCreate()));
        cboxType.getSelectionModel().select(0);
        if (customer != null) {
            Optional<CustomerTypeKeyValDto> dd = cboxType.getItems().stream()
                    .filter(p -> p.getKey() == customer.getType()).findFirst();
            if (dd.isPresent())
                cboxType.getSelectionModel().select(dd.get());
        }
    }

    private void loadDistrict(State state) {
        var task = new DistrictLoadTask(state);
        task.setOnSucceeded(e -> {
            try {
                List<District> list = task.get();
                if (list != null) {
                    cboxDistrict.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxDistrict);
                    if (customerDetail != null)
                        cboxDistrict.setValue(customerDetail.getDistrict());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadSubDistrict(District district) {
        var task = new SubDistrictLoadTask(district);
        task.setOnSucceeded(e -> {
            try {
                List<SubDistrict> list = task.get();
                if (list != null) {
                    cboxSubDistrict.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxSubDistrict);
                    if (customerDetail != null)
                        cboxSubDistrict.setValue(customerDetail.getSubDistrict());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadVillage(SubDistrict subDistrict) {
        var task = new VillageLoadTask(subDistrict);
        task.setOnSucceeded(e -> {
            try {
                List<Village> list = task.get();
                if (list != null) {
                    cboxVillage.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxVillage);
                    if (customerDetail != null)
                        cboxVillage.setValue(customerDetail.getVillage());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    private void loadBank() {
        var task = new BankLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Bank> list = task.get();
                if (list != null) {
                    cboxBankName.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadBranch(Bank bank) {
        var task = new BranchLoadTask(bank);
        task.setOnSucceeded(e -> {
            try {
                List<Branch> list = task.get();
                if (list != null) {
                    cboxBranchName.setItems(FXCollections.observableList(list));
                    new AutoCompleteComboBoxListener<>(cboxBranchName);
                    if (customerDetail != null)
                        cboxBranchName.setValue(customerDetail.getBranch());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
