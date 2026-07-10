package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.task.RejectedMilkCollectionDeleteTask;
import com.eipl.amcs.operation.procurement.task.RejectedMilkCollectionLoadByFilterTask;
import com.eipl.amcs.operation.procurement.task.RejectedMilkCollectionLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.FormatterFactory;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class RejectedMilkCollectionController implements MyInitialization, PopupCallback {

    private final ObjectProperty<RejectedMilkCollection> propRejectedMilkCollection;
    private ResourceBundle resourceBundle;

    @FXML
    private StackPane root;
    @FXML
    private E_DatePicker dpFromDate, dpToDate;
    @FXML
    private AutoSearchTextField<Shift> cboxFromShift, cboxToShift;
    @FXML
    private E_Button btnSearch, btnAdd, btnEdit, btnDelete, btnClose, btnExport;
    @FXML
    private TableView<RejectedMilkCollection> tableCollection;
    @FXML
    private TableColumn<RejectedMilkCollection, Number> colFat, colSnf, colQty;
    @FXML
    private TableColumn<RejectedMilkCollection, MilkType> colMilkType;
    @FXML
    private TableColumn<RejectedMilkCollection, String> colSrNo, colMemberName, colRemark, colMemberCode;
    @FXML
    private TableColumn<RejectedMilkCollection, LocalDate> colDate;
    @FXML
    private TableColumn<RejectedMilkCollection, Shift> colShift;

    public RejectedMilkCollectionController() {
        propRejectedMilkCollection = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        propRejectedMilkCollection.addListener((observable, oldValue, newValue) -> {
            btnEdit.setDisable(newValue == null);
            btnDelete.setDisable(newValue == null);
        });

        setupTable();
        setupComboBox();
        clearControls();
        loadData();
        loadShift();

        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "RejectedMilkCollectionAddEdit", null, this, resourceBundle.getString("rejectedmilkaddedit"));
        });

        btnEdit.setOnAction(e -> {
            RejectedMilkCollection dto = propRejectedMilkCollection.get();
            if (dto != null) editRejectedMilk(dto);
        });

        btnDelete.setOnAction(e -> {
            deleteData();
        });

        btnSearch.setOnAction(e -> searchData());
        btnExport.setOnAction(e -> exportToCsv());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        FocusUtils.requestFocus(btnAdd);

        tableCollection.setRowFactory(tv -> {
            TableRow<RejectedMilkCollection> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    editRejectedMilk(row.getItem());
                }
            });
            return row;
        });

        tableCollection.setOnKeyPressed(event -> {
            RejectedMilkCollection dto = tableCollection.getSelectionModel().getSelectedItem();
            if (dto == null) return;
            switch (event.getCode()) {
                case DELETE:
                    deleteData();
                    break;
                case ENTER:
                    editRejectedMilk(dto);
                    break;
            }
        });
    }

    private void editRejectedMilk(RejectedMilkCollection rejectedMilk) {
        if (rejectedMilk != null) {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "RejectedMilkCollectionAddEdit", rejectedMilk, this, "Edit Rejected Milk");
        }
    }

    @Override
    public void deleteData() {
        RejectedMilkCollection rejectedMilk = propRejectedMilkCollection.get();
        if (rejectedMilk != null) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilk"), resourceBundle.getString("alert.delete"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                var task = new RejectedMilkCollectionDeleteTask(rejectedMilk.getMilkCollectionRejectedCode());
                task.setOnSucceeded(e -> {
                    try {
                        if (task.get()) {
                            new InformationAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilk"), resourceBundle.getString("rejectedmilk.delete.successful")).createAlert();
                            loadData();
                        } else {
                            new ErrorAlert(MainApp.getStage(), resourceBundle.getString("rejectedmilk"), resourceBundle.getString("rejectedmilk.delete.failed")).createAlert();
                        }
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
        colSrNo.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMilkCollectionRejectedCode()));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate().toLocalDate()));
        colShift.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getShift()));
        colMemberCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().getCodeEx()));
        colMemberName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().toMemberName()));
        colMilkType.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMilkType()));
        colFat.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFat()));
        colSnf.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSnf()));
        colQty.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQty()));
        colRemark.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRemark()));
        propRejectedMilkCollection.bind(tableCollection.getSelectionModel().selectedItemProperty());
        TableLocalizationUtil.localizeTable(tableCollection);

    }

    @Override
    public void loadData() {
        tableCollection.setItems(null);
        var task = new RejectedMilkCollectionLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<RejectedMilkCollection> list = task.get();
                if (list != null)
                    tableCollection.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void searchData() {
        tableCollection.setItems(null);
        LocalDateTime fromDate = CommonUtils.getLocalDateTimeFromDateAndShift(dpFromDate.getValue(), cboxFromShift.getValue());
        LocalDateTime toDate = CommonUtils.getLocalDateTimeFromDateAndShift(dpToDate.getValue(), cboxToShift.getValue());

        var task = new RejectedMilkCollectionLoadByFilterTask(fromDate, toDate);
        task.setOnSucceeded(e -> {
            try {
                List<RejectedMilkCollection> list = task.get();
                if (list != null)
                    tableCollection.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                if (list != null) {
                    cboxFromShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxToShift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxFromShift.getSelectionModel().selectFirst();
                    cboxToShift.getSelectionModel().selectLast();
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void clearControls() {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        if (cboxFromShift.getItems() != null && !cboxFromShift.getItems().isEmpty()) {
            cboxFromShift.getSelectionModel().selectFirst();
        }
        if (cboxToShift.getItems() != null && !cboxToShift.getItems().isEmpty()) {
            cboxToShift.getSelectionModel().selectLast();
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag) {
            clearControls();
            loadData();
        }
    }

    private void exportToCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as CSV");
        fileChooser.setInitialFileName("RejectedMilkDetails.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(MainApp.getStage());

        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                writer.write('\uFEFF'); // Add BOM for UTF-8
                // Custom Header
                writer.append(String.format("\"%s (%s)\"", MainApp.identityDto.getSociety().toString(), FormatterFactory.formatNumber(MainApp.identityDto.getSociety().getCodeEx())));
                writer.newLine();
                writer.append("\"" + resourceBundle.getString("rejectedmilkcollection") + "\"").append('\n');
                writer.append("\"" + resourceBundle.getString("export.date") + ": ").append(FormatterFactory.formatDate(LocalDate.now())).append("\"\n");
                writer.append('\n'); // Blank line

                // CSV Header
                writer.append(escapeCsv(colDate.getText())).append(',');
                writer.append(escapeCsv(colShift.getText())).append(',');
                writer.append(escapeCsv(colMemberName.getText())).append(',');
                writer.append(escapeCsv(colMilkType.getText())).append(',');
                writer.append(escapeCsv(colFat.getText())).append(',');
                writer.append(escapeCsv(colSnf.getText())).append(',');
                writer.append(escapeCsv(colQty.getText())).append(',');
                writer.append(escapeCsv(colRemark.getText())).append('\n');

                // Data
                for (RejectedMilkCollection item : tableCollection.getItems()) {
                    writer.append(escapeCsv(FormatterFactory.formatDate(item.getDate().toLocalDate()))).append(',');
//                    writer.append(escapeCsv(item.getShift().getName())).append(',');
                    writer.append(escapeCsv(resourceBundle.getString(item.getShift().getName()))).append(',');
                    writer.append(escapeCsv(item.getMember().toMemberName())).append(',');
                    //writer.append(escapeCsv(item.getMilkType().getName())).append(',');
                    writer.append(escapeCsv(resourceBundle.getString(item.getMilkType().getName().toLowerCase()))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatNumber(item.getFat()))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatNumber(item.getSnf()))).append(',');
                    writer.append(escapeCsv(FormatterFactory.formatNumber(item.getQty()))).append(',');
                    writer.append(escapeCsv(item.getRemark())).append('\n');
                }
                new InformationAlert(MainApp.getStage(), "Export Success", "Data exported successfully.").createAlert();
            } catch (IOException ex) {
                new ErrorAlert(MainApp.getStage(), "Export Error", "Error exporting data: " + ex.getMessage()).createAlert();
                ex.printStackTrace();
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}