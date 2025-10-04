package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.procurement.controller.SocietyPaymentCycleEditController;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.task.BonusListLoadTask;
import com.eipl.amcs.operation.billing.task.BonusSaveTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.NameConcatUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BonusDisburseController extends SocietyPaymentCycleEditController implements MyInitialization, PopupCallback {
    @FXML
    private StackPane root;
    @FXML
    private ComboBox<SocietyPaymentCycle> cboxPaymentCycle;
    @FXML
    private Button btnDisburse, btnClose, btnExport;
    @FXML
    private CheckBox chkSelectAll;
    @FXML
    private DatePicker dpDisburseDate;
    @FXML
    private TableColumn<Bonus, Boolean> colSelect;
    @FXML
    private TableView<Bonus> tableBonusDisburse;
    @FXML
    private TableColumn<Bonus, String> colMemberCode, colMemberName, colStatus, colType;
    @FXML
    private TableColumn<Bonus, Number> colMilkQty, colMilkAmount, colBonusAmt;

    private List<Bonus> bonusList;
    private BonusSummary bonusSummary;
    private ResourceBundle resourceBundle;
    private ObjectProperty<Bonus> propBonus;
    private BonusDto dto;


    public BonusDisburseController() {
        propBonus = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setBonusSummary(BonusSummary bonusSummary) {
        String code = bonusSummary.getCode();
        this.bonusSummary = bonusSummary;
        loadData(code);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bonusList = new ArrayList<>();
        dpDisburseDate.setValue(LocalDate.now());
        this.resourceBundle = resourceBundle;
        setupTable();
        dpDisburseDate.setValue(LocalDate.now());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/billing/BonusSummary.fxml"))));


        btnDisburse.setOnAction(e -> {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                    CommonUtils.getResourceString(resourceBundle, "member.bill.disburse.confirmation"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                disburseBonus();
            }
        });

        btnExport.setOnAction(event -> {
            List<Bonus> list = bonusList.stream().collect(Collectors.toList());
            exportExcel(list);
        });
        chkSelectAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                for (Bonus bonus : bonusList) {
                    bonus.selectedProperty().setValue(true);
                }
            } else {
                for (Bonus bonus : bonusList) {
                    bonus.selectedProperty().setValue(false);
                }
            }
        });

    }

    @Override
    public void setupTable() {
        try {
            tableBonusDisburse.setEditable(true);
            colSelect.setEditable(false);
            colSelect.setCellValueFactory(data -> data.getValue().selectedProperty());
            colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
            colMemberCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getCodeEx()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colBonusAmt.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBonusAmount()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() == 0 ? "PENDING" : "DONE"));
            colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() == 0 ? "Union Bonus" : "Society Bonus"));
            colMilkQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQty()));
            colMilkAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkAmount()));
            propBonus.bind(tableBonusDisburse.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("BonusDisburse setuptable Exception");
            e.printStackTrace();
        }
    }


    private void disburseBonus() {
        dto.getBonusSummary().setStatus((short) 1);
        dto.getBonusSummary().setDisbursedDate(dpDisburseDate.getValue());
        for (Bonus bonus : bonusList) {
            bonus.setStatus((short) 1);
            bonus.selectedProperty().setValue(false);
        }
        dto.setBonusList(bonusList);
        var task = new BonusSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("bonus"),
                        resourceBundle.getString("bonus.disburse.successful"));
                alert.createAlert();
                tableBonusDisburse.setItems(FXCollections.observableList(bonusList));
                tableBonusDisburse.refresh();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void exportExcel(List<Bonus> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Bonus Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("M. Code", "M. Name", "Qty", "Milk Amount", "Bonus Amount", "Type",
                        "Status");
                List<String> strColumnTodisplay = null;
                List<String> items = null;
                strColumnTodisplay = new ArrayList<>(strColumns);
                List<String> finalResultToDisplay = strColumnTodisplay.stream().collect(Collectors.toList());
                // Create header column
                HSSFRow row = sheet.createRow(0);
                HSSFCell cell = null;
                int cellValueHeading = 0;
                for (String columnTitle : finalResultToDisplay) {
                    cell = row.createCell(cellValueHeading);
                    cell.setCellValue(columnTitle);
                    cellValueHeading++;
                }
                int rowCnt = 1;
                for (Bonus item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        switch (columnTitle) {
                            case "M. Code":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMember().getCodeEx());
                                break;
                            case "M. Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(NameConcatUtil.nameConcate(item.getMember()));
                                break;
                            case "Qty":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getMilkQty()));
                                break;
                            case "Milk Amount":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getMilkAmount()));
                                break;
                            case "Bonus Amount":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getBonusAmount()));
                                break;
                            case "Type":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getType() == 0 ? "Society Bonus" : "Union Bonus");
                                break;
                            case "Status":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getStatus() == 0 ? "PENDING" : "DONE");
                                break;
                            default:
                                break;
                        }
                    }
                    rowCnt++;
                }
                try {
                    wb.close();
                } catch (IOException e1) {
                    exported = false;
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    wb.write(out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    exported = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exported = false;
        }
        MyAlert alert;
        if (exported) {
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("bonus"),
                    resourceBundle.getString("export.done"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("bonus"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }


    public void loadData(String code) {
        var task = new BonusListLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                dto = task.get();
                bonusList = dto.getBonusList();
                if (bonusList != null) {
                    tableBonusDisburse.setItems(FXCollections.observableList(bonusList));
                } else {

                }
            } catch (Exception exception) {

            }
        });
        new Thread(task).start();
    }

}
