package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
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
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BankReportController implements MyInitialization {

    public BigDecimal textFileTotal = BigDecimal.ZERO;
    List<PaymentForBank> list;
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private ComboBox<Bank> cboxBank;
    @FXML
    private ComboBox<String> cboxReportType;
    private ResourceBundle resourceBundle;
    private MemberBillSummary dto = null;

    public static String rightPadding(String input, char ch, int L) {
        String result = String.format("%" + (-L) + "s", input).replace(' ', ch);
        return result;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setSummay(MemberBillSummary dto) {
        if (dto != null) {
            this.dto = dto;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadBank();
        cboxReportType.getItems().addAll(resourceBundle.getString("report"), resourceBundle.getString("excel"),
                resourceBundle.getString("textfile"), resourceBundle.getString("textfile"), resourceBundle.getString("ifscreport"), resourceBundle.getString("excelUnion")
        );
        setupComboBox();
        cboxBank.getSelectionModel().select(0);
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> stage.close());
    }

    @Override
    public void setupComboBox() {
        cboxBank.setConverter(new BankConvertor(cboxBank));
        cboxBank.getSelectionModel().select(0);
        cboxReportType.getSelectionModel().select(0);
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_society_payment_cycle_code", dto.getPaymentCycle().getCode());
        params.put("p_payment_mode", 1);
        params.put("p_bank_code", cboxBank.getValue().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        switch (cboxReportType.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PAYMENT_REGISTER_BANK, params);
                JasperViewer.viewReport(print, false);
                break;
            case 2:
                loadExcelData(0);
                break;
            case 3:
                loadExcelData(1);
                break;
            case 4:
                loadExcelData(2);
                break;
            case 5:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PAYMENT_REGISTER_BANK_IFSC, params);
                JasperViewer.viewReport(print, false);
                break;
            case 6:
                loadExcelData(3);
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

    private void loadExcelData(int var) {
        var task = new PaymentRegisterReportExcelTask(MainApp.identityDto.getSociety().getCode(), dto.getPaymentCycle().getCode(), MainApp.locale
                , 1, cboxBank.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                list = task.get();
                if (var == 0)
                    exportExcel(list);
                else if (var == 1)
                    exportTextFile(list);
                else if (var == 3)
                    exportExcel2(list);
                else
                    exportTextFile1(list);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void exportTextFile(List<PaymentForBank> list) {
        try {
            List<PaymentForBank> temp = new ArrayList<>();
            for (PaymentForBank paymentForBank : list) {
                textFileTotal = textFileTotal.add(paymentForBank.getNet_amount());
            }
            temp.addAll(list);
            PaymentForBank total = new PaymentForBank();
            total.setMember_Code("");
            total.setMember_name("Total");
            total.setBank_acno("");
            total.setNet_amount(textFileTotal);
            temp.add(total);

            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Billing Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                BufferedWriter bw = new BufferedWriter
                        (new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                sb.append("Society Name : " + MainApp.identityDto.getSociety().getName());
                bw.write(sb.toString());
                bw.newLine();
                String fromShift = "";
                if (dto.getPaymentCycle().getFromDate().toLocalTime().getHour() == 6) {
                    fromShift = "M";
                } else {
                    fromShift = "E";
                }
                String toShift = "";
                if (dto.getPaymentCycle().getToDate().toLocalTime().getHour() == 6) {
                    toShift = "M";
                } else {
                    toShift = "E";
                }
                sb = new StringBuilder();
                sb.append("Payment Statement From :" + dto.getPaymentCycle().getFromDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " " + fromShift + " - " +
                        dto.getPaymentCycle().getToDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " " + toShift);
                bw.write(sb.toString());
                bw.newLine();
                sb = new StringBuilder();
                sb.append(String.format("%92s", "--------------------------------------------------------------------------------------------"));
                bw.write(sb.toString());
                bw.newLine();
                sb = new StringBuilder();
                sb.append(String.format("%4s", " Sr. No"));
                sb.append(String.format("%5s", "code"));
                sb.append(String.format("%30s", "Member Name"));
                sb.append(String.format("%40s", "BANK A/C."));
                sb.append(String.format("%50s", "IFSC Code"));
                sb.append(String.format("%10s", "Payment"));
                bw.write(sb.toString());
                bw.newLine();
                sb = new StringBuilder();
                sb.append(String.format("%92s", "--------------------------------------------------------------------------------------------"));
                bw.write(sb.toString());
                bw.newLine();
                int counter = 1;
                for (PaymentForBank paymentForBank : temp) {
                    sb = new StringBuilder();
                    if (counter == temp.size()) {
                        sb.append(String.format("%4s", ""));
                    } else {
                        sb.append(String.format("%4s", counter++));
                    }
                    sb.append(String.format("%8s", paymentForBank.getMember_Code()));
                    sb.append(String.format("%10s", " "));
                    sb.append(String.format(rightPadding(paymentForBank.getMember_name(), ' ', 40)));
                    sb.append(String.format("%20s", paymentForBank.getBank_acno()));
                    sb.append(String.format("%20s", paymentForBank.getIfsc()));
                    sb.append(String.format("%10s", paymentForBank.getNet_amount()));
                    bw.write(sb.toString());
                    bw.newLine();
                }
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportTextFile1(List<PaymentForBank> list) {
        try {
            List<PaymentForBank> temp = new ArrayList<>();
            for (PaymentForBank paymentForBank : list) {
                textFileTotal = textFileTotal.add(paymentForBank.getNet_amount());
            }
            temp.addAll(list);
            PaymentForBank total = new PaymentForBank();
            total.setMember_Code("");
            total.setMember_name("Total");
            total.setBank_acno("");
            total.setNet_amount(textFileTotal);
            temp.add(total);

            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Billing Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                BufferedWriter bw = new BufferedWriter
                        (new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                sb.append("Society Name : " + MainApp.identityDto.getSociety().getName());
                bw.write(sb.toString());
                bw.newLine();
                String fromShift = "";
                if (dto.getPaymentCycle().getFromDate().toLocalTime().getHour() == 6) {
                    fromShift = "M";
                } else {
                    fromShift = "E";
                }
                String toShift = "";
                if (dto.getPaymentCycle().getToDate().toLocalTime().getHour() == 6) {
                    toShift = "M";
                } else {
                    toShift = "E";
                }
                sb = new StringBuilder();
                sb.append("Payment Statement From :" + dto.getPaymentCycle().getFromDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " " + fromShift + " - " +
                        dto.getPaymentCycle().getToDate().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " " + toShift);
                bw.write(sb.toString());
                bw.newLine();
                sb = new StringBuilder();
                sb.append(String.format("%92s", "--------------------------------------------------------------------------------------------"));
                bw.write(sb.toString());
                bw.newLine();
                sb = new StringBuilder();
                sb.append(String.format("%4s", " Sr. No"));
                sb.append(String.format("%5s", "code"));
                sb.append(String.format("%30s", "Member Name"));
                sb.append(String.format("%10s", "Payment"));
                sb.append(String.format("%40s", "BANK A/C."));
                sb.append(String.format("%50s", "IFSC CODE"));
                bw.write(sb.toString());
                bw.newLine();
                sb = new StringBuilder();
                sb.append(String.format("%92s", "--------------------------------------------------------------------------------------------"));
                bw.write(sb.toString());
                bw.newLine();
                int counter = 1;
                for (PaymentForBank paymentForBank : temp) {
                    sb = new StringBuilder();
                    if (counter == temp.size()) {
                        sb.append(String.format("%4s", ""));
                    } else {
                        sb.append(String.format("%4s", counter++));
                    }
                    sb.append(String.format("%8s", paymentForBank.getMember_Code()));
                    sb.append(String.format("%10s", " "));
                    sb.append(String.format(rightPadding(paymentForBank.getMember_name(), ' ', 40)));
                    sb.append(String.format("%10s", paymentForBank.getNet_amount()));
                    sb.append(String.format("%20s", paymentForBank.getBank_acno()));
                    sb.append(String.format("%20s", paymentForBank.getIfsc()));
                    bw.write(sb.toString());
                    bw.newLine();
                }
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportExcel(List<PaymentForBank> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Billing Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("Sr. No.", "Member Code", "Member Name", "Bank A/C", "IFSC CODE", "Payment For Member");
                List<String> strColumnTodisplay = null;
                CellStyle style;
                strColumnTodisplay = new ArrayList<>(strColumns);
                DataFormat format = wb.createDataFormat();
                style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.00"));

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

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
                row = sheet.createRow(4);
                cell = row.createCell(0);
                cell.setCellValue("Date: ");
                cell = row.createCell(1);
                cell.setCellValue(dto.getPaymentCycle().getFromDate().toLocalDate().format(formatter) + " To " + dto.getPaymentCycle().getToDate().toLocalDate().format(formatter));

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

                            case "Payment For Member":
                                cell = row.createCell(cellValueHeading++);
                                total = total.add(item.getNet_amount());
                                cell.setCellValue(item.getNet_amount().doubleValue());
                                sheet.autoSizeColumn(cellValueHeading);

                                cell.setCellStyle(style);
                                break;
                            case "Bank A/C":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getBank_acno());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "IFSC CODE":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getIfsc());
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
                cell = row.createCell(5);
                cell.setCellValue(total.doubleValue());
                cell.setCellStyle(style);
                sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 4));
                sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 4));
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

    private void exportExcel2(List<PaymentForBank> list) {
        MyAlert alert;
        boolean exported = true;
        try {
            if (list.isEmpty()) {
                alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("bankreport"),
                        resourceBundle.getString("no.data"));
                alert.createAlert();
                return;
            }
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Billing Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");

                HSSFCellStyle centerStyle = wb.createCellStyle();
                centerStyle.setBorderBottom(BorderStyle.THIN);
                centerStyle.setBorderTop(BorderStyle.THIN);
                centerStyle.setBorderLeft(BorderStyle.THIN);
                centerStyle.setBorderRight(BorderStyle.THIN);
                centerStyle.setAlignment(HorizontalAlignment.CENTER);
                centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                List<String> strColumns = Arrays.asList("Soc Code", "Soc Name", "Payment Period", "Cust Code", "Cust Name", "Bank A/C", "IFSC CODE", "Bank Name", "Branch Name", "Pay Value");
                List<String> strColumnTodisplay = null;
                CellStyle style;
                strColumnTodisplay = new ArrayList<>(strColumns);
                DataFormat format = wb.createDataFormat();
                style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.00"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

                List<String> finalResultToDisplay = strColumnTodisplay.stream().collect(Collectors.toList());

                List<String> mainHeader = Arrays.asList("Code :", "Name: ", "Date: ");
                List<String> mainHeaderValue = Arrays.asList(MainApp.identityDto.getSociety().getCode(),
                        MainApp.identityDto.getSociety().getName(),
                        dto.getPaymentCycle().getFromDate().toLocalDate().format(formatter) + " To " + dto.getPaymentCycle().getToDate().toLocalDate().format(formatter));

                HSSFRow row = null;
                HSSFCell cell = null;
                for (int i = 0; i < mainHeader.size(); i++) {
                    row = sheet.createRow(i + 2);
                    cell = row.createCell(2);
                    cell.setCellStyle(centerStyle);
                    cell.setCellValue(mainHeader.get(i));
                    cell = row.createCell(3);
                    cell.setCellStyle(centerStyle);
                    cell.setCellValue(mainHeaderValue.get(i));
                    for (int j = 0; j < 3; j++) {
                        cell = row.createCell(4 + j);
                        cell.setCellStyle(centerStyle);
                    }
                }

                row = sheet.createRow(6);
                int cellValueHeading = 0;
                for (String columnTitle : finalResultToDisplay) {
                    cell = row.createCell(cellValueHeading);
                    cell.setCellValue(columnTitle);
                    cell.setCellStyle(centerStyle);
                    cellValueHeading++;
                }
                int rowCnt = 7;
                BigDecimal total = BigDecimal.ZERO;
                for (PaymentForBank item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        switch (columnTitle) {
                            case "Soc Code":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getSoc_code());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Soc Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getSoc_name());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Payment Period":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getPayment_period());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;

                            case "Cust Code":
                                cell = row.createCell(cellValueHeading++);
                                total = total.add(item.getNet_amount());
                                cell.setCellValue(item.getMember_Code());
                                sheet.autoSizeColumn(cellValueHeading);

                                cell.setCellStyle(style);
                                break;
                            case "Cust Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMember_name());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Bank A/C":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getBank_acno());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "IFSC CODE":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getIfsc());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Bank Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getBank_name());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Branch Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getBranch_name());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Pay Value":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getNet_amount().doubleValue());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            default:
                                break;
                        }
                        cell.setCellStyle(centerStyle);
                    }
                    rowCnt++;
                }

                row = sheet.createRow(rowCnt);
                cell = row.createCell(0);
                cell.setCellValue("TOTAL");
                cell = row.createCell(9);
                cell.setCellValue(total.doubleValue());
                cell.setCellStyle(style);
                sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 3));
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 6));
                sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 6));
                sheet.addMergedRegion(new CellRangeAddress(4, 4, 3, 6));
                try {
                    wb.close();
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
        if (exported) {
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("bankreport"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("bankreport"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }
}