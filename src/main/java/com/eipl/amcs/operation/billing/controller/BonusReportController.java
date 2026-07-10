package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.report.dto.PaymentForBank;
import com.eipl.amcs.report.task.PaymentRegisterReportExcelTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BonusReportController implements MyInitialization {

    public PopupCallback callback;
    List<PaymentForBank> list;
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    @FXML
    private AutoSearchTextField<Bank> cboxBank;
    @FXML
    private AutoSearchTextField<String> cboxReportType;


    private ResourceBundle resourceBundle;
    private BonusSummary dto = null;

    @Override
    public Node getRoot() {
        return root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSummay(BonusSummary dto) {
        if (dto != null) {
            this.dto = dto;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadBank();
        cboxBank.setDisable(true);
        cboxReportType.getItems().addAll(resourceBundle.getString("all"),
                resourceBundle.getString("cash"), resourceBundle.getString("bank"));
        setupComboBox();
        cboxReportType.getSelectionModel().select(0);
        cboxReportType.setOnAction(e -> {
            cboxBank.setDisable(true);
            if (cboxReportType.getSelectionModel().getSelectedIndex() == 2) {
                cboxBank.setDisable(false);
            }
        });
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> stage.close());
    }

    @Override
    public void setupComboBox() {
//        cboxBank.setConverter(new BankConvertor(cboxBank));
        cboxBank.getSelectionModel().select(0);
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_bonus_summary_code", dto.getCode());

        try {
            params.put("p_bank_code", cboxBank.getValue().getCode());
        } catch (Exception ee) {
            params.put("p_bank_code", 0);
        }
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        switch (cboxReportType.getSelectionModel().getSelectedIndex()) {
            case 0:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_REGISTER, params);
                JasperViewer.viewReport(print, false);
                break;
            case 1:
                params.put("p_payment_type", 0);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_REGISTER_CASH, params);
                JasperViewer.viewReport(print, false);
                break;
            case 2:
                params.put("p_payment_type", 1);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_REGISTER_BANK, params);
                JasperViewer.viewReport(print, false);
                break;
        }
    }

    private void loadBank() {
        var task = new BankLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Bank> list = task.get();
                if (list != null) {
                    List<Bank> list2 = new ArrayList<>();
                    Bank m = new Bank();
                    m.setCode("0");
                    m.setName(resourceBundle.getString("allbank"));
                    list2.add(m);
                    list2.addAll(list);
                    cboxBank.setItems(FXCollections.observableList(list2));
                    cboxBank.getSelectionModel().select(0);
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadExcelData() {
        var task = new PaymentRegisterReportExcelTask(MainApp.identityDto.getSociety().getCode(), "", MainApp.locale
                , 1, cboxBank.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                list = task.get();
                exportExcel(list);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void exportExcel(List<PaymentForBank> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Bonus Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("Sr. No.", "Member Code", "Member Name", "Bank A/C", "Payment");
                List<String> strColumnTodisplay = null;
                strColumnTodisplay = new ArrayList<>(strColumns);
                List<String> finalResultToDisplay = strColumnTodisplay.stream().collect(Collectors.toList());
                // Create header column
                HSSFRow row = sheet.createRow(2);
                HSSFCell cell = row.createCell(0);
                cell.setCellValue("Code: ");
                cell = row.createCell(1);
                cell.setCellValue(MainApp.identityDto.getSociety().getCode());

                row = sheet.createRow(3);
                cell = row.createCell(0);
                cell.setCellValue("Name: ");
                cell = row.createCell(1);
                cell.setCellValue(MainApp.identityDto.getSociety().getName());

                row = sheet.createRow(4);
                cell = row.createCell(0);
                cell.setCellValue("Date: ");
                cell = row.createCell(1);

                row = sheet.createRow(6);
                int cellValueHeading = 0;
                for (String columnTitle : finalResultToDisplay) {
                    cell = row.createCell(cellValueHeading);
                    cell.setCellValue(columnTitle);
                    cellValueHeading++;
                }
                int rowCnt = 7;
                BigDecimal total = BigDecimal.ZERO;
                for (PaymentForBank item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        switch (columnTitle) {
                            case "Sr. No.":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getSr_no().toString());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Member Code":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMember_Code());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Member Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMember_name());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;

                            case "Payment":
                                cell = row.createCell(cellValueHeading++);
                                total = total.add(item.getNet_amount());
                                cell.setCellValue(item.getNet_amount().toString());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Bank A/C":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getBank_acno());
                                sheet.autoSizeColumn(cellValueHeading);
                            default:
                                break;
                        }
                    }
                    rowCnt++;
                }

                row = sheet.createRow(rowCnt);
                cell = row.createCell(0);
                cell.setCellValue("TOTAL");
                cell = row.createCell(4);
                cell.setCellValue(total.toString());
                sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
                sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));

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
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }
}