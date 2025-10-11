package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.geo.converter.DistrictConvertor;
import com.eipl.amcs.master.geo.converter.StateConvertor;
import com.eipl.amcs.master.geo.converter.SubDistrictConvertor;
import com.eipl.amcs.master.geo.converter.VillageConvertor;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
import com.eipl.amcs.master.geo.service.DistrictService;
import com.eipl.amcs.master.geo.service.StateService;
import com.eipl.amcs.master.geo.service.SubDistrictService;
import com.eipl.amcs.master.geo.service.VillageService;
import com.eipl.amcs.master.global.convertor.CustomerTypeConvertor;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.convertor.BranchConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.service.BankService;
import com.eipl.amcs.master.org.service.BranchService;
import com.eipl.amcs.util.CommonUtil;
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

import static com.eipl.amcs.MainApp.context;

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

    private CustomerService customerService;
    private DistrictService districtService;
    private StateService stateService;
    private SubDistrictService subDistrictService;
    private BankService bankService;
    private BranchService branchService;
    private NextCodeService nextCodeService;
    private VillageService villageService;


    public CustomerAddEditController() {
        stateService = context.getBean(StateService.class);
        customerService = context.getBean(CustomerService.class);
        districtService = context.getBean(DistrictService.class);
        villageService = context.getBean(VillageService.class);
        branchService = context.getBean(BranchService.class);
        bankService = context.getBean(BankService.class);
        subDistrictService = context.getBean(SubDistrictService.class);
        nextCodeService = context.getBean(NextCodeService.class);
    }

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
        try {
            Customer customer = customerService.findByCustomerCode(code);
            CustomerDetails md = customerService.findDetailByCustomer(customer);
            if (md != null) {
                this.customerDetail = md;
                loadData();
                setValuesInControls();
            } else {
                loadData();
                setValuesInControls();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void saveData() {
        try {
            customerService.save(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                    resourceBundle.getString("customer.insert.successful"));
            alert.createAlert();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Customer.fxml")));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateData() {
        try {
            customerService.update(dto, CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("customer"),
                    resourceBundle.getString("customer.update.successful"));
            alert.createAlert();
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/operation/Customer.fxml")));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void getNextCustomerCode() {
        try {
            String nextCode = nextCodeService.getNextCode("Customer", "code", MainApp.identityDto.getSociety().getCode(), 4);
            if (nextCode == null || nextCode.isEmpty())
                return;
            txtCode.setText(nextCode);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void setValuesInObject() {
        customer.setCode(txtCode.getText());
        customerDetail.setCode(txtCode.getText());
        customer.setSociety(MainApp.identityDto.getSociety());
        customer.setUnion(MainApp.identityDto.getUnion());
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
        if (txtName.getText() == null || txtName.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("namenullerror") + "\n");
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
        try {
            List<State> list = stateService.findAll();
            if (list != null) {
                cboxState.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxState);

                if (customerDetail != null)
                    cboxState.setValue(customerDetail.getState());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        try {
            List<District> list = districtService.findAll(state.getCode());
            if (list != null) {
                cboxDistrict.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxDistrict);
                if (customerDetail != null)
                    cboxDistrict.setValue(customerDetail.getDistrict());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSubDistrict(District district) {
        try {
            List<SubDistrict> list = subDistrictService.findAll(district.getCode());
            if (list != null) {
                cboxSubDistrict.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxSubDistrict);
                if (customerDetail != null)
                    cboxSubDistrict.setValue(customerDetail.getSubDistrict());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadVillage(SubDistrict subDistrict) {
        try {
            List<Village> list = villageService.findAll(subDistrict.getCode());
            if (list != null) {
                cboxVillage.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxVillage);
                if (customerDetail != null)
                    cboxVillage.setValue(customerDetail.getVillage());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    private void loadBank() {
        try {
            List<Bank> list = bankService.findAll();
            if (list != null) {
                cboxBankName.setItems(FXCollections.observableList(list));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadBranch(Bank bank) {
        try {
            List<Branch> list = branchService.findAll(bank.getCode());
            if (list != null) {
                cboxBranchName.setItems(FXCollections.observableList(list));
                new AutoCompleteComboBoxListener<>(cboxBranchName);
                if (customerDetail != null)
                    cboxBranchName.setValue(customerDetail.getBranch());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
