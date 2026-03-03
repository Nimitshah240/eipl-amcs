package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.dto.BillHeadMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingBillHead;
import com.eipl.amcs.master.account.task.LedgerMappingBillHeadLoadTask;
import com.eipl.amcs.master.account.task.LedgerMappingBillHeadSaveTask;
import com.eipl.amcs.master.operation.convertor.LedgerCellFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LedgerMappingBillHeadController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerMappingBillHead> tableBillHeadData,tableBillHeadData1;
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

    private ResourceBundle resourceBundle;
    private ObservableList<Ledger> ledgerList;

    private List<String> typeList;

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
            if (!MainApp.user.getPermissions().contains("SUB_MENU_LEDGER_MAPPING_BILL_HEAD_SAVE"))
                throw new UnAuthorizedAccessException();
            saveData();
        });
    }

    @Override
    public void saveData() {
        for (LedgerMappingBillHead item : tableBillHeadData.getItems()) {
            item.setType((int) item.getBillHead().getHeadType());
            item.setCreditDebit(item.getBillHead().getHeadType() == 1);
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
            item.setSociety(MainApp.identityDto.getSociety());
            item.setBillCriteria(null);
        }
        LedgerMappingBillHeadSaveTask task = new LedgerMappingBillHeadSaveTask(tableBillHeadData.getItems());
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("save.successful"));
            alert.createAlert();
        });
        new Thread(task).start();
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
                                        getTableRow().getItem().setLedger(null);
                                    else
                                        getTableRow().getItem().setLedger(cbox.getValue());
                                });
                                cbox.setMaxWidth(Double.MAX_VALUE);
                                if (getTableRow().getItem() != null && getTableRow().getItem().getBillHead() != null) {
                                    if (getTableRow().getItem().getBillHead().getCode()
                                            .endsWith("105"))
                                        setGraphic(null);
                                    else
                                        setGraphic(cbox);
                                }
                            }
                        }
                    };
                });

        colLedgerMaster.setOnEditCommit(event -> {
            LedgerMappingBillHead obj = event.getRowValue();
            if (obj.getBillHead().getCode().equalsIgnoreCase("105"))
                colLedgerMaster.setGraphic(null);
            obj.setLedger(event.getNewValue());
        });
        colType.setCellValueFactory(cell -> new SimpleObjectProperty(!cell.getValue().getBillHead().getCode().equalsIgnoreCase("105") ?
                cell.getValue().getBillHead().getHeadType() == 1 ?
                        resourceBundle.getString("credit") : resourceBundle.getString("debit") : ""));

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
                                    getTableRow().getItem().setHasSubLedger(newValue);
                                });
                        if (getTableRow().getItem() != null && getTableRow().getItem().getBillHead() != null) {
                            if (getTableRow().getItem().getBillHead().getCode()
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
                        LedgerMappingBillHead mappingEvent = getTableRow().getItem();
                        if (mappingEvent != null) {
                            chk.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                                if (newValue) {
                                    mappingEvent.setxCol1("1");
                                } else {
                                    mappingEvent.setxCol1("0");
                                }
                            });
                            if (getTableRow().getItem() != null && getTableRow().getItem().getBillHead() != null) {
                                if (getTableRow().getItem().getBillHead().getCode()
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

    @Override
    public void loadData() {
        LedgerMappingBillHeadLoadTask task = new LedgerMappingBillHeadLoadTask();
        task.setOnSucceeded(e -> {
            try {
                BillHeadMappingDto dto = task.get();
                if (dto == null)
                    return;

                ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                typeList = new ArrayList<>();
                typeList.add(resourceBundle.getString("debit"));
                typeList.add(resourceBundle.getString("credit"));
                setupTable();
                tableBillHeadData.setItems(FXCollections.observableList(dto.getListMapping()));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        new Thread(task).start();
    }

}
