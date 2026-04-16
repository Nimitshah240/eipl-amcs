package com.eipl.amcs.master.inventory.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_ComboBox;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.account.converter.TaxConvertor;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.master.account.task.TaxLoadTask;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.UnitLoadTask;
import com.eipl.amcs.master.inventory.convertor.ProductGroupConvertor;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.task.ProductGroupLoadTask;
import com.eipl.amcs.master.inventory.task.ProductNumberLoadTask;
import com.eipl.amcs.master.inventory.task.ProductSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ProductAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private Button btnClose, btnSaveUpdate;
    @FXML
    private E_ComboBox<ProductGroup> cboxProductGroup;
    @FXML
    private E_ComboBox<Tax> cboxTaxName, cboxOtherTax;
    @FXML
    private E_ComboBox<MilkType> cboxMilkType;
    @FXML
    private E_ComboBox<Ledger> cboxPurchaseLedger, cboxSaleLedger, cboxStockLedger, cboxLocalSaleLedger, cboxCouponLedger;
    @FXML
    private CheckBox chkIsMilk;

    @FXML
    private E_TextField txtCode, txtName, txtLocalName, txtReferenceCode;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private Product dto = null;
    private List<Unit> unitList = new ArrayList<>();

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnShown(e -> cboxProductGroup.requestFocus());
    }

    @Override
    public Node getRoot() {
        return root;
    }


    public void setProduct(Product dto) {
        loadUnit();
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
            loadControls();
        } else {
            getNextProductCode();
        }
        loadProductGroup();
        loadTax();
    }

    @Override
    public void loadData() {
        loadMilkType();
        loadLedger();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadUnit();
        setupComboBox();
        loadData();
        txtReferenceCode.setEditable(false);
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
        chkIsMilk.selectedProperty().addListener((obs, oldVal, newVal) -> {
            cboxMilkType.setDisable(!newVal);
            cboxLocalSaleLedger.setDisable(!newVal);
            cboxCouponLedger.setDisable(!newVal);
            if (!newVal){
                cboxMilkType.getSelectionModel().select(null);
                cboxLocalSaleLedger.getSelectionModel().select(null);
                cboxCouponLedger.getSelectionModel().select(null);
            }
        });

    }

    public void loadControls() {
        cboxProductGroup.getSelectionModel().select(dto.getProductGroup());
        cboxTaxName.getSelectionModel().select(dto.getTax());
        txtCode.setText(dto.getCode());
        txtName.setText(dto.getName());
        txtLocalName.setText(dto.getNameLocal());
        txtReferenceCode.setText(dto.getReferenceCode());
        cboxOtherTax.getSelectionModel().select(dto.getOtherStateTax());
        cboxPurchaseLedger.getSelectionModel().select(dto.getPurchaseLedger());
        cboxSaleLedger.getSelectionModel().select(dto.getSaleLedger());
        cboxStockLedger.getSelectionModel().select(dto.getStockLedger());
        cboxMilkType.getSelectionModel().select(dto.getMilkType());
        cboxLocalSaleLedger.getSelectionModel().select(dto.getLocalSaleLedger());
        cboxCouponLedger.getSelectionModel().select(dto.getCouponLedger());
        chkIsMilk.setSelected(dto.isMilk());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product"), errorMsg.toString());
            alert.createAlert();
            return;
        }

        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            if (this.dto != null) {
                dto = setValuesInObject();
                updateData();
            }
        } else {
            dto = new Product();
            dto = setValuesInObject();
            saveData();
        }
    }

    private void getNextProductCode() {
        var task = new ProductNumberLoadTask(MainApp.identityDto.getSociety().getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty()) return;
                txtCode.setText(nextCode);
                txtReferenceCode.setText(nextCode.replace(MainApp.identityDto.getSociety().getCode(), ""));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private Product setValuesInObject() {
        dto.setCode(txtCode.getText());
        dto.setName(txtName.getText());
        dto.setNameLocal(txtLocalName.getText());
        dto.setTax(cboxTaxName.getValue());
        dto.setProductGroup(cboxProductGroup.getValue());
        dto.setPrimaryUom(unitList.get(0));
        dto.setConversionUnit(unitList.get(0));
        dto.setReferenceCode(txtReferenceCode.getText());
        dto.setSociety(MainApp.identityDto.getSociety());
        dto.setUnion(MainApp.identityDto.getUnion());
        dto.setIndent(true);
        dto.setEntryType((short) 1);
        dto.setIndent(true);
        dto.setBaseConversionFactor(BigDecimal.ZERO);
        dto.setConversionFactor(BigDecimal.ZERO);
        dto.setActive(true);
        dto.setConversionUnit(unitList.get(0));
        dto.setPrimaryUom(unitList.get(0));

        dto.setOtherStateTax(cboxOtherTax.getSelectionModel().getSelectedItem());
        dto.setPurchaseLedger(cboxPurchaseLedger.getSelectionModel().getSelectedItem());
        dto.setSaleLedger(cboxSaleLedger.getSelectionModel().getSelectedItem());
        dto.setLocalSaleLedger(cboxLocalSaleLedger.getSelectionModel().getSelectedItem());
        dto.setCouponLedger(cboxCouponLedger.getSelectionModel().getSelectedItem());
        dto.setStockLedger(cboxStockLedger.getSelectionModel().getSelectedItem());
        dto.setMilkType(cboxMilkType.getSelectionModel().getSelectedItem());
        dto.setMilk(chkIsMilk.isSelected());

        dto.setOriginatingOrgCode(MainApp.identityDto.getSociety().getCode());
        dto.setOriginatingOrgType("VLC");
        dto.setOriginatingType(0);
        return dto;
    }

    private boolean validate() {
        if (cboxProductGroup.getValue() == null)
            errorMsg.append(resourceBundle.getString("productgroupnullerror") + "\n");
        if (cboxTaxName.getValue() == null) errorMsg.append(resourceBundle.getString("taxnullerror") + "\n");
//        if (txtReferenceCode.getText() == null || txtReferenceCode.getText().trim().isEmpty())
//            errorMsg.append(resourceBundle.getString("referencecodenullerror") + "\n");
        if (txtName.getText() == null || txtName.getText().trim().isEmpty())
            errorMsg.append(resourceBundle.getString("productnamenullerror") + "\n");
//        if (txtLocalName.getText() == null || txtLocalName.getText().trim().isEmpty())
//            errorMsg.append(resourceBundle.getString("productlocalnamenullerror") + "\n");

        if (cboxOtherTax.getValue() == null) errorMsg.append(resourceBundle.getString("othertaxnullerror") + "\n");
        if (cboxPurchaseLedger.getValue() == null) errorMsg.append(resourceBundle.getString("purhcaseledgernullerror") + "\n");
        if (cboxSaleLedger.getValue() == null) errorMsg.append(resourceBundle.getString("saleledgernullerror") + "\n");
        if (cboxStockLedger.getValue() == null) errorMsg.append(resourceBundle.getString("stockledgernullerror") + "\n");
        if (chkIsMilk.isSelected()) {
            if (cboxMilkType.getValue() == null) errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
            if (cboxLocalSaleLedger.getValue() == null) errorMsg.append(resourceBundle.getString("localsaleledgernullerror") + "\n");
            if (cboxCouponLedger.getValue() == null) errorMsg.append(resourceBundle.getString("couponledgernullerror") + "\n");
        }
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new ProductSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product"), sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("product"), resourceBundle.getString("product.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new ProductSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + subError.getMessage() + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("product"), sb.toString());
                    alert.createAlert();
                    return;
                }

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("product"), resourceBundle.getString("product.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxProductGroup.setConverter(new ProductGroupConvertor(cboxProductGroup));
        cboxTaxName.setConverter(new TaxConvertor(cboxTaxName));
    }

    private void loadProductGroup() {
        var task = new ProductGroupLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<ProductGroup> list = task.get();
                if (list != null) cboxProductGroup.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadUnit() {
        var task = new UnitLoadTask();
        task.setOnSucceeded(e -> {
            try {
                unitList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadTax() {
        var task = new TaxLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Tax> list = CommonUtils.getTaxFromDto(task.get());
                if (list != null) {
                    cboxTaxName.setItems(FXCollections.observableList(list));
                    cboxOtherTax.setItems(FXCollections.observableList(list));
                }
                if (dto == null) {
                    cboxTaxName.getSelectionModel().select(list.stream().filter(p -> p.getName().equalsIgnoreCase("NIL")).findFirst().orElse(null));
                    cboxOtherTax.getSelectionModel().select(list.stream().filter(p -> p.getName().equalsIgnoreCase("NIL")).findFirst().orElse(null));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMilkType() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) cboxMilkType.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadLedger() {
        var task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Ledger> list = task.get();
                if (list != null) {
                    cboxPurchaseLedger.setItems(FXCollections.observableArrayList(list));
                    cboxStockLedger.setItems(FXCollections.observableArrayList(list));
                    cboxSaleLedger.setItems(FXCollections.observableArrayList(list));
                    cboxLocalSaleLedger.setItems(FXCollections.observableArrayList(list));
                    cboxCouponLedger.setItems(FXCollections.observableArrayList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
