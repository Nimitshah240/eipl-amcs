package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.cellfactory.SocietyPaymentCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.dto.MilkSummaryDataEntry;
import com.eipl.amcs.operation.billing.task.MilkCollectionSummaryImportTask;
import com.eipl.amcs.operation.billing.task.MilkCollectionSummaryListSaveTask;
import com.eipl.amcs.operation.billing.task.MilkSummaryDataEntryTask;
import com.eipl.amcs.operation.procurement.dto.CollectionImportDto;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.task.MilkCollectionDeleteTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;


public class MilkSummaryDataEntryController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<MilkCollection> tableMilkCollectionSummary;
    @FXML
    TableColumn<MilkCollection, LocalDate> colDate;
    @FXML
    TableColumn<MilkCollection, SocietyPaymentCycle> colPaymentCycle;
    @FXML
    TableColumn<MilkCollection, String> colMemberCode, colMemberName;
    @FXML
    TableColumn<MilkCollection, MilkType> colMilkType;
    @FXML
    TableColumn<MilkCollection, BigDecimal> colMilkQuantity, colMilkAmount;

    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnSearch, btnImport;

    @FXML
    private DatePicker dpFromDate, dpToDate;

    private ResourceBundle resourceBundle;


    private final ObjectProperty<MilkCollection> milkSummaryDataEntry;

    public MilkSummaryDataEntryController() {
        milkSummaryDataEntry = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFromDate.setValue(LocalDate.now());
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setValue(LocalDate.now());
        dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });

        setupTable();
        loadPreRequisiteData();
        milkSummaryDataEntry.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_SUMMARY_DATA_ENTRY_EDIT"))
                throw new UnAuthorizedAccessException();
            MilkCollection dto = milkSummaryDataEntry.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkSummaryDataEntryAddEdit", dto, this);
        });
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_SUMMARY_DATA_ENTRY_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MilkSummaryDataEntryAddEdit", null, this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_SUMMARY_DATA_ENTRY_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnSearch.setOnAction(e -> loadData());

        btnImport.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MILK_SUMMARY_DATA_ENTRY_IMPORT"))
                throw new UnAuthorizedAccessException();
            // loadImportPreReq();
            File file = CommonUtils.openExcelFileDialog(resourceBundle.getString("milkcollectionsummarydataentry"));
            if (file == null) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                        resourceBundle.getString("select.file"));
                alert.createAlert();
                return;
            }

            if (!file.getName().contains(MainApp.identityDto.getSociety().getCode())) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                        resourceBundle.getString("invalid.file"));
                alert.createAlert();
                return;
            }

            MainApp.paneDrop.setVisible(true);
            MainApp.lblMessage.setText("Preparing Milk Collection...");
            startImport(file);
        });

    }

    private List<Member> memberList;
    private List<MilkType> milkTypeList;

    private void loadPreRequisiteData() {
        var task2 = new MemberLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                memberList = task2.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();

        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                milkTypeList = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private List<MilkSummaryDataEntry> listSummaryData;

    private void startImport(File file) {
        var task = new MilkCollectionSummaryImportTask(file, milkTypeList, memberList);
        task.setOnSucceeded(e -> {
            try {
                listSummaryData = task.get();
                if (listSummaryData == null || listSummaryData.isEmpty()) {
                    MainApp.paneDrop.setVisible(false);
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                            resourceBundle.getString("invalid.file"));
                    alert.createAlert();
                    return;
                }
                MainApp.lblMessage.setText("Importing Milk Collection Summary...");
                startImportProcess();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void startImportProcess() {
        var task = new MilkCollectionSummaryListSaveTask(listSummaryData);
        task.setOnSucceeded(e -> {
            try {
                MainApp.paneDrop.setVisible(false);
                List<CollectionImportDto> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("Import success: ");
                builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("success")).count());
                builder.append("\n");
                builder.append("Import fail: ");
                builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("error")).count());
                builder.append("\n");

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                        builder.toString());
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            MilkCollection collection = milkSummaryDataEntry.get();
            if (collection != null) {
                var task = new MilkCollectionDeleteTask(collection.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || respDelete.booleanValue() == false) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        MyAlert alert1 = new InformationAlert(MainApp.getStage(), resourceBundle.getString("milkcollectionsummarydataentry"),
                                resourceBundle.getString("record.delete.successful"));
                        alert1.createAlert();
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void setupTable() {
        try {
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCollectionDate().toLocalDate()));
            colDate.setCellFactory(new LocalDateCellFactory<>());
            colPaymentCycle.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getSocietyPaymentCycle()));
            colPaymentCycle.setCellFactory(new SocietyPaymentCellFactory<>());
            colMemberCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getCodeEx()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getMilkType()));
            colMilkQuantity.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQty()));
            colMilkAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));

            milkSummaryDataEntry.bind(tableMilkCollectionSummary.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("MilkSummary setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableMilkCollectionSummary.setItems(null);
        MilkSummaryDataEntryTask task = new MilkSummaryDataEntryTask(LocalDateTime.of(dpFromDate.getValue(), LocalTime.of(6, 0)),
                LocalDateTime.of(dpToDate.getValue(), LocalTime.of(18, 0)));
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollection> list = task.get();
                if (list != null)
                    tableMilkCollectionSummary.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}

