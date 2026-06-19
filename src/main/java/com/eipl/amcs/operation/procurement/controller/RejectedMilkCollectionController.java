package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.operation.procurement.model.RejectedMilkCollection;
import com.eipl.amcs.operation.procurement.task.RejectedMilkCollectionDeleteTask;
import com.eipl.amcs.operation.procurement.task.RejectedMilkCollectionLoadByFilterTask;
import com.eipl.amcs.operation.procurement.task.RejectedMilkCollectionLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private ComboBox<Shift> cboxFromShift, cboxToShift;
    @FXML
    private ComboBox<Dock> cboxDock;
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

        propRejectedMilkCollection.addListener((observable, oldValue, newValue) -> {
            btnEdit.setDisable(newValue == null);
            btnDelete.setDisable(newValue == null);
        });

        setupTable();
        setupComboBox();
        loadData();
        loadShift();

        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "RejectedMilkCollectionAddEdit", null, this, "Add Rejected Milk");
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

    @Override
    public void setupComboBox() {
        cboxFromShift.setConverter(new ShiftConvertor(cboxFromShift));
        cboxToShift.setConverter(new ShiftConvertor(cboxToShift));
        new AutoCompleteComboBoxListener<>(cboxFromShift);
        new AutoCompleteComboBoxListener<>(cboxToShift);
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
        colSrNo.setCellValueFactory(new PropertyValueFactory<>("milkCollectionRejectedCode"));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate().toLocalDate()));
        colShift.setCellValueFactory(new PropertyValueFactory<>("shift"));
        colMemberCode.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getMember() != null) {
                return new SimpleStringProperty(cellData.getValue().getMember().getCode());
            }
            return new SimpleStringProperty("");
        });
        colMemberName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMember().getFirstName()));
        colMilkType.setCellValueFactory(new PropertyValueFactory<>("milkType"));
        colFat.setCellValueFactory(new PropertyValueFactory<>("fat"));
        colSnf.setCellValueFactory(new PropertyValueFactory<>("snf"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colRemark.setCellValueFactory(new PropertyValueFactory<>("remark"));

        propRejectedMilkCollection.bind(tableCollection.getSelectionModel().selectedItemProperty());
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
    public void reloadData(boolean flag) {
        if (flag) loadData();
    }

    private void exportToCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save as CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(MainApp.getStage());

        if (file != null) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                // Custom Header
                writer.append(MainApp.identityDto.getSociety().getName() + " - " + MainApp.identityDto.getSociety().getCode()).append('\n');
                writer.append("Rejected Milk Collection").append('\n');
                writer.append("Export Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append('\n');
                writer.append('\n'); // Blank line

                // CSV Header
                writer.append("Date,Shift,Member,Milk Type,Fat,SNF,Qty,Remark\n");

                // Data
                for (RejectedMilkCollection item : tableCollection.getItems()) {
                    writer.append(escapeCsv(item.getDate().toLocalDate().toString())).append(',');
                    writer.append(escapeCsv(item.getShift().getName())).append(',');
                    writer.append(escapeCsv(item.getMember().toMemberName())).append(',');
                    writer.append(escapeCsv(item.getMilkType().getName())).append(',');
                    writer.append(escapeCsv(item.getFat().toPlainString())).append(',');
                    writer.append(escapeCsv(item.getSnf().toPlainString())).append(',');
                    writer.append(escapeCsv(item.getQty().toPlainString())).append(',');
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
