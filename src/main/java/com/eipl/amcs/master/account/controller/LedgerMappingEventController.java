package com.eipl.amcs.master.account.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.account.dto.EventMappingDto;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.task.LedgerMappingEventLoadTask;
import com.eipl.amcs.master.account.task.LedgerMappingEventSaveTask;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LedgerMappingEventController implements MyInitialization {

    @FXML
    AnchorPane root;
    @FXML
    TableView<LedgerMappingEvent> tableData;
    @FXML
    TableColumn<LedgerMappingEvent, String> colEvent, colDescription, colVoucherNarration, colVoucherTxnCreditNarration, colVoucherTxnDebitNarration, colVoucherNarrationLocal, colVoucherTxnCreditNarrationLocal, colVoucherTxnDebitNarrationLocal;
    @FXML
    TableColumn<LedgerMappingEvent, Ledger> colDebitLedger, colCreditLedger;
    @FXML
    TableColumn<LedgerMappingEvent, VoucherType> colVoucherTypes;
    @FXML
    TableColumn<LedgerMappingEvent, Boolean> colDebitSubLedger, colCreditSubLedger, colAction;

    @FXML
    Button btnSave, btnClose;

    private ResourceBundle resourceBundle;
    private ObservableList<Ledger> ledgerList;
    private final StringConverter<Ledger> converter = new StringConverter<>() {
        @Override
        public String toString(Ledger object) {
            if (object == null) return null;
            return object.toString();
        }

        @Override
        public Ledger fromString(String string) {
            if (string == null || string.isEmpty()) return null;
            return ledgerList.stream().filter(p -> p.toString().equalsIgnoreCase(string)).findFirst().orElse(null);
        }
    };
    private ObservableList<VoucherType> voucherList;
    private final StringConverter<VoucherType> voucherTypeConverter = new StringConverter<>() {
        @Override
        public String toString(VoucherType object) {
            if (object == null) return null;
            return object.toString();
        }

        @Override
        public VoucherType fromString(String string) {
            if (string == null || string.isEmpty()) return null;
            return voucherList.stream().filter(p -> p.toString().equalsIgnoreCase(string)).findFirst().orElse(null);
        }
    };

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        loadData();

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
//        btnSave.setDisable(true);
        tableData.setEditable(false);
        btnSave.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("SUB_MENU_LEDGER_MAPPING_EVENT_SAVE"))
                throw new UnAuthorizedAccessException();
            saveData();
        });
    }

    @Override
    public void saveData() {
        for (LedgerMappingEvent item : tableData.getItems()) {
            item.setSociety(MainApp.identityDto.getSociety());
            item.setUnionCode(MainApp.identityDto.getUnion().getCode());
        }
        LedgerMappingEventSaveTask task = new LedgerMappingEventSaveTask(tableData.getItems());
        task.setOnSucceeded(e -> {
            MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("mapping"),
                    resourceBundle.getString("save.successful"));
            alert.createAlert();
            loadData();
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        colEvent.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEvents().getEventName()));
        colDescription.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEvents().getDescription()));
        colVoucherNarration.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherNarration()));
        colVoucherTxnCreditNarration.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherTxnCreditNarration()));
        colVoucherTxnDebitNarration.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherTxnDebitNarration()));
        colVoucherNarrationLocal.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherNarrationLocal()));
        colVoucherTxnCreditNarrationLocal.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherTxnCreditNarrationLocal()));
        colVoucherTxnDebitNarrationLocal.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getVoucherTxnDebitNarrationLocal()));

        colDebitLedger.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDebitLedger()));
        colDebitLedger.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colDebitLedger.setOnEditCommit(event -> {
            LedgerMappingEvent obj = event.getRowValue();
            obj.setDebitLedger(event.getNewValue());
            obj.setEventcode(obj.getEvents().getEventCode());
        });
        colCreditLedger.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getCreditLedger()));
        colCreditLedger.setCellFactory(ComboBoxTableCell.forTableColumn(converter, ledgerList));
        colCreditLedger.setOnEditCommit(event -> {
            LedgerMappingEvent obj = event.getRowValue();
            obj.setCreditLedger(event.getNewValue());
        });
        colVoucherTypes.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getVoucherType()));
        colVoucherTypes.setCellFactory(ComboBoxTableCell.forTableColumn(voucherTypeConverter, voucherList));
        colVoucherTypes.setOnEditCommit(event -> {
            LedgerMappingEvent obj = event.getRowValue();
            obj.setVoucherType(event.getNewValue());
        });
        colDebitSubLedger.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getDebitSubLedger()
                != null ? cell.getValue().getDebitSubLedger() : false));
//        colDebitSubLedger.setCellFactory(cell -> {
//            return new TableCell<LedgerMappingEvent, Boolean>() {
//                @Override
//                protected void updateItem(Boolean item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        CheckBox chk = new CheckBox();
//                        chk.setSelected(item);
//                        LedgerMappingEvent mappingEvent = getTableRow().getItem();
//                        if (mappingEvent != null) {
//                            chk.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                                mappingEvent.setDebitSubLedger(newValue);
//                            });
//                            if (mappingEvent.getEvents().getSubLedgerDebit())
//                                setGraphic(chk);
//                            else {
//                                setGraphic(null);
//                                mappingEvent.setDebitSubLedger(false);
//                            }
//                        }
//                    }
//                }
//            };
//        });
        colCreditSubLedger.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getCreditSubLedger()
                != null ? cell.getValue().getCreditSubLedger() : false));
//        colCreditSubLedger.setCellFactory(cell -> {
//            return new TableCell<LedgerMappingEvent, Boolean>() {
//                @Override
//                protected void updateItem(Boolean item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        CheckBox chk = new CheckBox();
//                        chk.setSelected(item);
//                        LedgerMappingEvent mappingEvent = getTableRow().getItem();
//                        if (mappingEvent != null) {
//                            chk.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                                mappingEvent.setCreditSubLedger(newValue);
//                            });
//                            if (mappingEvent.getEvents().getSubLedgerCredit())
//                                setGraphic(chk);
//                            else {
//                                setGraphic(null);
//                                mappingEvent.setCreditSubLedger(false);
//                            }
//                        }
//                    }
//                }
//            };
//        });
        colAction.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getxCol1()
                != null && cell.getValue().getxCol1().equalsIgnoreCase("1")));
//        colAction.setCellFactory(cell -> {
//            return new TableCell<LedgerMappingEvent, Boolean>() {
//                @Override
//                protected void updateItem(Boolean item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        CheckBox chk = new CheckBox();
//                        chk.setSelected(item);
//                        LedgerMappingEvent mappingEvent = getTableRow().getItem();
//                        if (mappingEvent != null) {
//                            chk.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                                if (newValue) {
//                                    mappingEvent.setxCol1("1");
//                                } else {
//                                    mappingEvent.setxCol1("0");
//                                }
//                            });
//                            if (mappingEvent.getxCol1() != null && mappingEvent.getxCol1().equalsIgnoreCase("1"))
//                                setGraphic(chk);
//                            else {
//                                setGraphic(chk);
//                                mappingEvent.setxCol1("0");
//                            }
//                        }
//                    }
//                }
//            };
//        });
    }

    @Override
    public void loadData() {
        LedgerMappingEventLoadTask task = new LedgerMappingEventLoadTask();
        task.setOnSucceeded(e -> {
            try {
                EventMappingDto dto = task.get();
                if (dto == null) return;

                ledgerList = FXCollections.observableArrayList(dto.getLedgerList());
                voucherList = FXCollections.observableArrayList(dto.getVoucherTypeList());
                setupTable();
                tableData.setItems(FXCollections.observableList(dto.getListMapping()));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        new Thread(task).start();
    }

}
