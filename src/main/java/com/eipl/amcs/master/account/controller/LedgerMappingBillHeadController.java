package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.dto.BillHeadMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingBillHead;
import com.eipl.amcs.master.account.service.LedgerMappingBillHeadService;
import com.eipl.amcs.master.account.service.LedgerService;
import com.eipl.amcs.master.operation.convertor.LedgerCellFactory;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.service.BillHeadService;
import com.eipl.amcs.util.CommonUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class LedgerMappingBillHeadController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerMappingBillHead> tableBillHeadData;
    @FXML
    TableColumn<LedgerMappingBillHead, String> colMemberBillHead;
    @FXML
    TableColumn<LedgerMappingBillHead, Ledger> colLedgerMaster;

    @FXML
    TableColumn<LedgerMappingBillHead, Boolean> colSubLedger, colAction;
    @FXML
    TableColumn<LedgerMappingBillHead, String> colType;
    @FXML
    Button btnSave, btnClose;

    private LedgerMappingBillHeadService ledgerMappingBillHeadService;
    private ResourceBundle resourceBundle;
    private BillHeadService billHeadService;
    private LedgerService ledgerService;

    public LedgerMappingBillHeadController() {
        ledgerMappingBillHeadService = MainApp.context.getBean(LedgerMappingBillHeadService.class);
        billHeadService = MainApp.context.getBean(BillHeadService.class);
        ledgerService = MainApp.context.getBean(LedgerService.class);
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
        for (LedgerMappingBillHead item : tableBillHeadData.getItems()) {
            item.setType((int) item.getBillHead().getHeadType());
            item.setCreditDebit(item.getBillHead().getHeadType() == 1 ? true : false);
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            item.setSociety(MainApp.identityDto.getSociety());
            item.setBillCriteria(null);
        }

        ledgerMappingBillHeadService.save(tableBillHeadData.getItems(), CommonUtil.setIdentityHeader());
        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                resourceBundle.getString("save.successful"));
        alert.createAlert();
    }

    @Override
    public void setupTable() {
        colMemberBillHead.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getBillHead().getName()));
        colLedgerMaster.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getBillHead().getCode().
                equalsIgnoreCase("105") ? null : cell.getValue().getLedger()));
        colLedgerMaster.setCellFactory(
                cell -> {
                    return new TableCell<LedgerMappingBillHead, Ledger>() {
                        @Override
                        protected void updateItem(Ledger item, boolean empty) {
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                setText(null);
                                ComboBox<Ledger> cbox = new ComboBox<>(ledgerList);
                                cbox.setConverter(new LedgerConvertor(cbox));
                                cbox.setCellFactory(new LedgerCellFactory());
                                if (item == null)
                                    cbox.getSelectionModel().select(0);
                                else
                                    cbox.getSelectionModel().select(item);
                                cbox.setOnAction(event -> {
                                    if (cbox.getValue().getName().equals("None"))
                                        ((LedgerMappingBillHead) getTableRow().getItem()).setLedger(null);
                                    else
                                        ((LedgerMappingBillHead) getTableRow().getItem()).setLedger(cbox.getValue());
                                });
                                cbox.setMaxWidth(Double.MAX_VALUE);
                                if (getTableRow().getItem() != null && getTableRow().getItem().getBillHead() != null) {
                                    if (((LedgerMappingBillHead) getTableRow().getItem()).getBillHead().getCode()
                                            .endsWith("105"))
                                        setGraphic(null);
                                    else
                                        setGraphic(cbox);
                                }
                            }
                        }
                    };
                });
//                ComboBoxTableCell.forTableColumn(converter, ledgerList);

        colLedgerMaster.setOnEditCommit(event -> {
            LedgerMappingBillHead obj = event.getRowValue();
            if (obj.getBillHead().getCode().equalsIgnoreCase("105"))
                colLedgerMaster.setGraphic(null);
            obj.setLedger(event.getNewValue());
        });
        colType.setCellValueFactory(cell -> new SimpleObjectProperty(!cell.getValue().getBillHead().getCode().equalsIgnoreCase("105") ?
                cell.getValue().getBillHead().getHeadType() == 1 ?
                        resourceBundle.getString("credit") : resourceBundle.getString("debit") : ""));
//        colType.setCellFactory(ComboBoxTableCell.forTableColumn(converterString, FXCollections.observableList(typeList)));
//        colType.setOnEditCommit(event -> {
//            LedgerMappingBillHead obj = event.getRowValue();
//            obj.setCreditDebit(event.getRowValue().getBillHead().getHeadType()==1?true:false);
//        });

        colSubLedger.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getHasSubLedger() != null ? cell.getValue().getHasSubLedger() : false));
        colSubLedger.setCellFactory(cell -> {
            return new TableCell<LedgerMappingBillHead, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        CheckBox chk = new CheckBox();
                        chk.setSelected(item);
                        chk.selectedProperty().addListener(
                                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                    if (newValue) {
                                        ((LedgerMappingBillHead) getTableRow().getItem()).setHasSubLedger(true);
                                    } else {
                                        ((LedgerMappingBillHead) getTableRow().getItem()).setHasSubLedger(false);
                                    }
                                });
                        if (getTableRow().getItem() != null && getTableRow().getItem().getBillHead() != null) {
                            if (((LedgerMappingBillHead) getTableRow().getItem()).getBillHead().getCode()
                                    .endsWith("105"))
                                setGraphic(null);
                            else
                                setGraphic(chk);
                        }
                    }
                }
            };
        });
        colAction.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getxCol1()
                != null && cell.getValue().getxCol1().equalsIgnoreCase("1")));
        colAction.setCellFactory(cell -> {
            return new TableCell<LedgerMappingBillHead, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        CheckBox chk = new CheckBox();
                        chk.setSelected(item);
                        LedgerMappingBillHead mappingEvent = (LedgerMappingBillHead) getTableRow().getItem();
                        if (mappingEvent != null) {
                            chk.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                    mappingEvent.setxCol1("1");
                                } else {
                                    mappingEvent.setxCol1("0");
                                }
                            });
                            if (getTableRow().getItem() != null && getTableRow().getItem().getBillHead() != null) {
                                if (((LedgerMappingBillHead) getTableRow().getItem()).getBillHead().getCode()
                                        .endsWith("105"))
                                    setGraphic(null);
                                else
                                    setGraphic(chk);
                            }
                        }
                    }
                }
            };
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

        CompletableFuture<List<BillHead>> billHeadListFuture = CompletableFuture.supplyAsync(() -> billHeadService.findAll());
        CompletableFuture<List<Ledger>> ledgerListFuture = CompletableFuture.supplyAsync(() -> ledgerService.findAllByIsActive());
        CompletableFuture<List<LedgerMappingBillHead>> ledgerMappingBillHeadListFuture = CompletableFuture.supplyAsync(() -> ledgerMappingBillHeadService.findAll());
        CompletableFuture.allOf(billHeadListFuture, ledgerListFuture, ledgerMappingBillHeadListFuture)
                .whenCompleteAsync((result, ex) -> {
                    try {
                        List<BillHead> billHeadList = billHeadListFuture.get();
                        List<Ledger> ledgerList1 = ledgerListFuture.get();
                        List<LedgerMappingBillHead> listMapping = ledgerMappingBillHeadListFuture.get();
                        if (!billHeadList.isEmpty() && !listMapping.isEmpty() && !ledgerList1.isEmpty()) {
                            for (LedgerMappingBillHead mp : listMapping) {
                                billHeadList.removeIf(p -> p.getCode().equalsIgnoreCase(mp.getBillHead().getCode()));
                            }
                            for (BillHead billHead : billHeadList) {
                                LedgerMappingBillHead mp = new LedgerMappingBillHead();
                                mp.setBillHead(billHead);
                                listMapping.add(mp);
                            }
                            List<Ledger> list = new ArrayList<>(ledgerList1);
                            list.add(0, new Ledger("None"));//"0",
                            BillHeadMappingDto dto = new BillHeadMappingDto(listMapping, list);

                            ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                            typeList = new ArrayList<>();
                            typeList.add(resourceBundle.getString("debit"));
                            typeList.add(resourceBundle.getString("credit"));
                            setupTable();
                            tableBillHeadData.setItems(FXCollections.observableList(dto.getListMapping()));
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
