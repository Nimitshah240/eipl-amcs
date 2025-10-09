package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.ProductGroupMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.service.LedgerMappingProductGroupService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.service.ProductGroupService;
import com.eipl.amcs.util.CommonUtil;
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
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static com.eipl.amcs.MainApp.context;

public class LedgerMappingProductGroupController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerMappingProductGroup> tableData;
    @FXML
    TableColumn<LedgerMappingProductGroup, String> colProductGroupCode, colProductGroupName;
    @FXML
    TableColumn<LedgerMappingProductGroup, Ledger> colLedgerPurchase, colLedgerSale;

    @FXML
    Button btnSave, btnClose;

    private ResourceBundle resourceBundle;
    private LedgerMappingProductGroupService ledgerMappingProductGroupService;
    private ProductGroupService productGroupService;
    private LedgerService ledgerService;

    public LedgerMappingProductGroupController() {
        ledgerMappingProductGroupService = context.getBean(LedgerMappingProductGroupService.class);
        productGroupService = context.getBean(ProductGroupService.class);
        ledgerService = context.getBean(LedgerService.class);
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnSave.setOnAction(e -> {
            saveData();
        });
    }


    @Override
    public void saveData() {
        for (LedgerMappingProductGroup item : tableData.getItems()) {
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            item.setSociety(MainApp.identityDto.getSociety());
        }
        ledgerMappingProductGroupService.save(tableData.getItems(), CommonUtil.setIdentityHeader());
        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                resourceBundle.getString("save.successful"));
        alert.createAlert();
    }

    @Override
    public void setupTable() {
        colProductGroupCode.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductGroup().getCode().toString()));
        colProductGroupName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProductGroup().getName()));

        colLedgerSale.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLedgerSaleCode()));
        colLedgerSale.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedgerSale.setOnEditCommit(event -> {
            LedgerMappingProductGroup obj = event.getRowValue();
            obj.setLedgerSaleCode(event.getNewValue());
        });
        colLedgerPurchase.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLedgerPurchaseCode()));
        colLedgerPurchase.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedgerPurchase.setOnEditCommit(event -> {
            LedgerMappingProductGroup obj = event.getRowValue();
            obj.setLedgerPurchaseCode(event.getNewValue());
        });
    }

    private StringConverter<Ledger> converter = new StringConverter<>() {
        @Override
        public String toString(Ledger object) {
            if (object == null)
                return null;
            return object.toString();
        }

        @Override
        public Ledger fromString(String string) {
            if (string == null || string.isEmpty())
                return null;
            return ledgerList.stream().filter(p -> p.toString().equalsIgnoreCase(string))
                    .findFirst().orElse(null);
        }
    };

    private ObservableList<Ledger> ledgerList;

    @Override
    public void loadData() {
        try {
            CompletableFuture<List<ProductGroup>> productGroupListFuture = CompletableFuture.supplyAsync(() -> productGroupService.findAll());
            CompletableFuture<List<Ledger>> ledgerListFuture = CompletableFuture.supplyAsync(() -> ledgerService.findAllByIsActive());
            CompletableFuture<List<LedgerMappingProductGroup>> ledgerMappingProductGroupListFuture = CompletableFuture.supplyAsync(() -> ledgerMappingProductGroupService.findAll());
            CompletableFuture.allOf(productGroupListFuture, ledgerListFuture, ledgerMappingProductGroupListFuture)
                    .whenCompleteAsync((result, ex) -> {
                        try {

                            List<ProductGroup> productGroupList = productGroupListFuture.get();
                            List<Ledger> ledgerList1 = ledgerListFuture.get();
                            List<LedgerMappingProductGroup> ledgerMappingProductGroupList = ledgerMappingProductGroupListFuture.get();

                            List<LedgerMappingProductGroup> listMapping = new ArrayList<>(ledgerMappingProductGroupList);
                            for (LedgerMappingProductGroup mp : listMapping) {
                                productGroupList.removeIf(p -> p.getCode().toString().equalsIgnoreCase(mp.getProductGroup().getCode().toString()));
                            }
                            for (ProductGroup productGroup : productGroupList) {
                                LedgerMappingProductGroup mp = new LedgerMappingProductGroup();
                                mp.setProductGroup(productGroup);
                                listMapping.add(mp);
                            }
                            List<Ledger> list = new ArrayList<>(ledgerList1);
                            list.add(0, new Ledger("None"));
                            ProductGroupMappingDto dto = new ProductGroupMappingDto(listMapping, list);

                            if (dto == null)
                                return;

                            ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                            setupTable();
                            tableData.setItems(FXCollections.observableList(dto.getListMapping()));
                        } catch (Exception e) {
                            System.out.println(e);
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
