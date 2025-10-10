package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.dto.VoucherTypeMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.account.service.VoucherTypeLedgerConfigService;
import com.eipl.amcs.master.account.service.VoucherTypeService;
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

public class VoucherTypeLedgerConfigController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<VoucherTypeLedgerConfig> tableData;
    @FXML
    TableColumn<VoucherTypeLedgerConfig, String> colType, colVoucherType;
    @FXML
    TableColumn<VoucherTypeLedgerConfig, Ledger> colLedger;
    @FXML
    Button btnSave, btnClose;

    private ResourceBundle resourceBundle;

    private VoucherTypeService voucherTypeService;
    private VoucherTypeLedgerConfigService voucherTypeLedgerConfigService;
    private LedgerService ledgerService;

    public VoucherTypeLedgerConfigController() {
        voucherTypeService = context.getBean(VoucherTypeService.class);
        voucherTypeLedgerConfigService = context.getBean(VoucherTypeLedgerConfigService.class);
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
        try {
            for (VoucherTypeLedgerConfig item : tableData.getItems()) {
                item.setSociety(MainApp.identityDto.getSociety());
                item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            }
            voucherTypeLedgerConfigService.save(tableData.getItems(), CommonUtil.setIdentityHeader());
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("save.successful"));
            alert.createAlert();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setupTable() {
        colVoucherType.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherType().getName()));

        colLedger.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLedger()));
        colLedger.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colLedger.setOnEditCommit(event -> {
            VoucherTypeLedgerConfig obj = event.getRowValue();
            obj.setLedger(event.getNewValue());
        });
        colType.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCreditDebit() != null ?
                cell.getValue().getCreditDebit() == false ? resourceBundle.getString("debit") : resourceBundle.getString("credit") : ""));
        colType.setCellFactory(ComboBoxTableCell.forTableColumn(converterString, FXCollections.observableList(typeList)));
        colType.setOnEditCommit(event -> {
            VoucherTypeLedgerConfig obj = event.getRowValue();
            obj.setCreditDebit(event.getNewValue().equalsIgnoreCase(resourceBundle.getString("debit")) ? false : true);
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
    private StringConverter<String> converterString = new StringConverter<>() {
        @Override
        public String toString(String object) {
            if (object == null)
                return null;
            return object;
        }

        @Override
        public String fromString(String string) {
            if (string == null || string.isEmpty())
                return null;
            return typeList.stream().filter(p -> p.equalsIgnoreCase(string))
                    .findFirst().orElse(null);
        }
    };

    private ObservableList<Ledger> ledgerList;
    private List<String> typeList;

    @Override
    public void loadData() {
        try {
            CompletableFuture<List<VoucherType>> voucherTypeListFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println("Nimit : fetching... voucher");
                List<VoucherType> v = voucherTypeService.findAll();
                System.out.println("Nimit : fetched... voucher");
                return v;
            });
            CompletableFuture<List<Ledger>> ledgerListFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println("Nimit : fetching... ledger");
                List<Ledger> l = ledgerService.findAllByIsActive();
                System.out.println("Nimit : fetched... ledger");
                return l;
            });
            CompletableFuture<List<VoucherTypeLedgerConfig>> voucherTypeLedgerConfigListFuture = CompletableFuture.supplyAsync(() -> {
                System.out.println("Nimit : fetching... voucher type");
                List<VoucherTypeLedgerConfig> vl = voucherTypeLedgerConfigService.findAll();
                System.out.println("Nimit : fetched... voucher type");
                return vl;

            });

            CompletableFuture.allOf(voucherTypeListFuture, ledgerListFuture, voucherTypeLedgerConfigListFuture)
                    .whenCompleteAsync((result, ex) -> {
                        try {
                            System.out.println("Nimit : inside");

                            List<VoucherType> voucherTypeList = voucherTypeListFuture.get();
                            List<Ledger> ledgerList1 = ledgerListFuture.get();
                            List<VoucherTypeLedgerConfig> voucherTypeLedgerConfigList = voucherTypeLedgerConfigListFuture.get();

                            List<VoucherTypeLedgerConfig> listMapping = new ArrayList<>(voucherTypeLedgerConfigList);
                            for (VoucherTypeLedgerConfig mp : listMapping) {
                                voucherTypeList.removeIf(p -> p.getCode().toString().equalsIgnoreCase(mp.getVoucherType().getCode().toString()));
                            }
                            for (VoucherType voucherType : voucherTypeList) {
                                VoucherTypeLedgerConfig mp = new VoucherTypeLedgerConfig();
                                mp.setVoucherType(voucherType);
                                listMapping.add(mp);
                            }

                            List<Ledger> list = new ArrayList<>(ledgerList1);
                            list.add(0, new Ledger("None"));//"0",

                            VoucherTypeMappingDto dto = new VoucherTypeMappingDto(listMapping, list);
                            if (dto == null)
                                return;

                            ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                            typeList = new ArrayList<>();
                            typeList.add(resourceBundle.getString("debit"));
                            typeList.add(resourceBundle.getString("credit"));
                            setupTable();
                            tableData.setItems(FXCollections.observableList(dto.getListMapping()));
                        } catch (Exception e) {
                            System.out.println(e);
                            throw new RuntimeException(e);
                        }
                    });
            System.out.println("Nimit : outside");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
