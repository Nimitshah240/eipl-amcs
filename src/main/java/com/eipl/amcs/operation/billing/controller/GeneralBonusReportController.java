package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.org.convertor.BankConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.task.MemberWiseBonusListLoadTask;
import com.eipl.amcs.operation.billing.task.MemberWiseBonusLoadTask;
import com.eipl.amcs.operation.procurement.dto.PrinterHelper;
import com.eipl.amcs.report.task.BonusRegisterAllExcelTask;
import com.eipl.amcs.report.task.BonusRegisterReportExcelTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.task.HardwareDeviceConfigLoadTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.eipl.amcs.utils.AppConstant.DB_LOC;

public class GeneralBonusReportController implements MyInitialization {

    private final ArrayList<String> masterLines = new ArrayList<>();
    public PopupCallback callback;
    public Integer index = 0;
    public BigDecimal sum = BigDecimal.ZERO;
    public BigDecimal sum1 = BigDecimal.ZERO;
    public BigDecimal sum2 = BigDecimal.ZERO;
    public BigDecimal finalAmount = BigDecimal.ZERO;
    public BigDecimal kapat = BigDecimal.ZERO;
    public BigDecimal totalamount = BigDecimal.ZERO;
    public Bonus bonus;
    public Map<String, Object> map;
    List<Map<String, Object>> maplist = new ArrayList<>();
    List<Map<String, Object>> mapList = new ArrayList<>();
    List<String> colList = new ArrayList<>();
    List<Member> list2 = new ArrayList<>();
    String printer = "";
    String slipLanguage = "";
    List<HardwareDeviceConfig> hardwareDeviceConfigs = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    @FXML
    private AutoSearchTextField<Bank> cboxBank;
    @FXML
    private AutoSearchTextField<Member> cboxMember;
    @FXML
    private E_DatePicker dpFromDate, dpToDate;
    @FXML
    private AutoSearchTextField<String> cboxReportType, cboxType;
    @FXML
    private AutoSearchTextField<MilkType> cboxMilkType;
    private PrinterHelper printerHelper;
    private ResourceBundle resourceBundle;
    private BonusSummary dto = null;
    private List<MilkType> listMilkType;
    private File slipFile = null;

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
        loadHardware();
        slipLanguage = MainApp.getProperty("slip.language", "English");

        this.resourceBundle = resourceBundle;
        loadBank();
        loadMember();
        readFile();
        cboxReportType.getItems().addAll(resourceBundle.getString("all"), resourceBundle.getString("cash"), resourceBundle.getString("bank"), resourceBundle.getString("excel"), resourceBundle.getString("excel1"), resourceBundle.getString("excel2"), resourceBundle.getString("bonusslip"), resourceBundle.getString("summary"), resourceBundle.getString("bonus"));
        cboxType.getItems().addAll(resourceBundle.getString("union"), resourceBundle.getString("society"));
        setupComboBox();
        cboxReportType.getSelectionModel().select(0);
        cboxType.getSelectionModel().select(0);
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> stage.close());
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
    }

    private String getPrinterName(List<HardwareDeviceConfig> devices) {
        for (HardwareDeviceConfig device : hardwareDeviceConfigs) {
            if (device.getDeviceType().equalsIgnoreCase("PRINTER")) {
                return device.getxCol1();
            }
        }
        return null;
    }

    public void loadHardware() {
        HardwareDeviceConfigLoadTask task = new HardwareDeviceConfigLoadTask();
        task.setOnSucceeded(e -> {
            try {
                hardwareDeviceConfigs = task.get();
                printer = getPrinterName(hardwareDeviceConfigs);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
//        cboxBank.setConverter(new BankConvertor(cboxBank));
        cboxBank.getSelectionModel().select(0);
//        cboxMember.setConverter(new MemberReportConvertor(cboxMember));
//        cboxMember.setCellFactory(new MemberCellFactory());
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", dpFromDate.getValue());
        params.put("p_to_date", dpToDate.getValue());
        params.put("p_member_code", cboxMember.getValue().getCode());
        params.put("p_bonus_type", cboxType.getSelectionModel().getSelectedIndex());
        params.put("p_milk_type_code", cboxMilkType.getValue().getCode().toString());

        try {
            params.put("p_bank_code", cboxBank.getValue().getCode());
        } catch (Exception ee) {
            params.put("p_bank_code", 0);
        }
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        switch (cboxReportType.getSelectionModel().getSelectedIndex()) {
            case 0:
                params.put("p_payment_type", 0);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_REGISTER1, params);
                JasperViewer.viewReport(print, false);

                break;
            case 1:
                params.put("p_payment_type", 0);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_REGISTER_CASH1, params);
                JasperViewer.viewReport(print, false);
                break;
            case 2:
                params.put("p_payment_type", 1);
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_REGISTER_BANK1, params);
                JasperViewer.viewReport(print, false);
                break;
            case 3:
                excelDev(dpFromDate.getValue(), dpToDate.getValue());
                break;
            case 4:
                loadExcel2();
                break;
            case 5:
                loadExcel3();
                break;

            case 6:
                printerHelper = new PrinterHelper(printer, "Arial Unicode MS", 11);
                if (!cboxMember.getValue().getCodeEx().equalsIgnoreCase("0")) {
                    loadBonusData();
                } else {
                    loadBonusSummaryData();
                }
                break;
            case 7:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS_SUMMARY, params);
                JasperViewer.viewReport(print, false);
                break;
            case 8:
                print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.BONUS, params);
                JasperViewer.viewReport(print, false);
                break;
        }
    }

    private void excelDev(LocalDate fromDate, LocalDate toDate) {

        String jdbcUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
        String username = "root";
        String password = AppConstant.EIPL_DB_PASS;

        try {
            // Establish database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            Statement statement = connection.createStatement();

            // Execute the stored procedure
            String sql = "CALL GetBonusSummary('" + fromDate + "', '" + toDate + "', '" + cboxMilkType.getValue().getCode().toString() + "')";
            ResultSet resultSet = statement.executeQuery(sql);

            // Create a FileChooser for selecting the output folder
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Bonus Data");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                String outputFile = file.getAbsolutePath();

                // Create a new Excel workbook
                Workbook workbook = new HSSFWorkbook(); // Use HSSFWorkbook for .xls format
                Sheet sheet = workbook.createSheet("Bonus Data");

                // Create headers
                Row headerRow = sheet.createRow(0);
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Cell cell = headerRow.createCell(i - 1);
                    cell.setCellValue(columnName);
                }

                // Populate data rows
                int rowNumber = 1;
                String prevMemberName = null;
                while (resultSet.next()) {
                    String currentMemberName = resultSet.getString("name");
                    if (!Objects.equals(currentMemberName, prevMemberName)) {
                        Row dataRow = sheet.createRow(rowNumber++);
                        for (int i = 1; i <= columnCount; i++) {
                            Cell cell = dataRow.createCell(i - 1);
                            cell.setCellValue(resultSet.getString(i));
                        }
                        prevMemberName = currentMemberName;
                    } else {
                        Row dataRow = sheet.createRow(rowNumber++);
                        for (int i = 2; i <= columnCount; i++) {
                            Cell cell = dataRow.createCell(i - 1);
                            cell.setCellValue(resultSet.getString(i));
                        }
                    }
                }
                for (int i = 0; i < columnCount; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Write the workbook to the specified Excel file
                try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                    workbook.write(fileOut);
                }

                // Clean up resources
                resultSet.close();
                statement.close();
                connection.close();
                workbook.close();
                MyAlert alert = new InformationAlert(MainApp.stage, resourceBundle.getString("bonus"), resourceBundle.getString("successful"));
                alert.createAlert();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadExcel1() {
        String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
        try (Connection connection = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
            String call = "{call rpt_bonus_excel(?,?,?,?,?,?,?,?)}";
            try (CallableStatement stmt = connection.prepareCall(call)) {
                stmt.setString(1, MainApp.identityDto.getSociety().getCode());
                stmt.setDate(2, Date.valueOf(dpFromDate.getValue()));
                stmt.setDate(3, Date.valueOf(dpToDate.getValue()));
                stmt.setString(4, cboxMember.getValue().getCode());
                stmt.setString(5, MainApp.locale);
                stmt.setInt(6, cboxReportType.getSelectionModel().getSelectedIndex());
                stmt.setString(7, cboxBank.getValue().getCode());
                stmt.setInt(8, cboxType.getSelectionModel().getSelectedIndex());
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData rsm = rs.getMetaData();
                columns.clear();

                for (int i = 3; i <= rsm.getColumnCount() - 4; i++) {
                    String col = rsm.getColumnName(i);
                    columns.add(col);
                }
                Collections.sort(columns);
                columns.add(0, "member_code");
                columns.add(1, "member_name");
                columns.add("account_no");
                columns.add("bonus_amount");
                columns.add("kapat");
                columns.add("total_amount");
                colList.clear();

                for (String str : columns) {
                    String col = str.replace("-", ".");
                    col = col.replace(".to.", " to ");
                    col = col.replace(".1.Qty", " Qty");
                    col = col.replace(".2.Amount", " Amount");
                    col = col.replace(".3.BonusAmount", " Bonus Amount");
                    colList.add(col);
                }
                loadExcelDoc1();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadExcel2() {
        String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
        try (Connection connection = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
            String call = "{call rpt_bonus_for_all(?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement stmt = connection.prepareCall(call)) {
                stmt.setString(1, MainApp.identityDto.getSociety().getCode());
                stmt.setDate(2, Date.valueOf(dpFromDate.getValue()));
                stmt.setDate(3, Date.valueOf(dpToDate.getValue()));
                stmt.setString(4, cboxMember.getValue().getCode());
                stmt.setString(5, MainApp.locale);
                stmt.setInt(6, cboxReportType.getSelectionModel().getSelectedIndex());
                stmt.setString(7, cboxBank.getValue().getCode());
                stmt.setInt(8, cboxType.getSelectionModel().getSelectedIndex());
                stmt.setString(9, cboxMilkType.getValue().getCode().toString());
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData rsm = rs.getMetaData();
                colList.clear();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    colList.add(rsm.getColumnName(i));
                }
                loadExcelDoc2();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadExcel3() {
        String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
        try (Connection connection = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
            String call = "{call rpt_bonus_for_all(?,?,?,?,?,?,?,?,?)}";
            try (CallableStatement stmt = connection.prepareCall(call)) {
                stmt.setString(1, MainApp.identityDto.getSociety().getCode());
                stmt.setDate(2, Date.valueOf(dpFromDate.getValue()));
                stmt.setDate(3, Date.valueOf(dpToDate.getValue()));
                stmt.setString(4, cboxMember.getValue().getCode());
                stmt.setString(5, MainApp.locale);
                stmt.setInt(6, cboxReportType.getSelectionModel().getSelectedIndex());
                stmt.setString(7, cboxBank.getValue().getCode());
                stmt.setInt(8, cboxType.getSelectionModel().getSelectedIndex());
                stmt.setString(9, cboxMilkType.getValue().getCode().toString());
                ResultSet rs = stmt.executeQuery();
                ResultSetMetaData rsm = rs.getMetaData();
                colList.clear();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    colList.add(rsm.getColumnName(i));
                }
                loadExcelDoc3();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadExcelDoc1() {
        BonusRegisterAllExcelTask task = new BonusRegisterAllExcelTask(MainApp.identityDto.getSociety().getCode(), cboxMember.getValue().getCode(), MainApp.locale, 0, cboxBank.getValue().getCode(), dpFromDate.getValue(), dpToDate.getValue(), cboxType.getSelectionModel().getSelectedIndex());
        task.setOnSucceeded(e -> {
            try {
                mapList = task.get();
                exportExcel(mapList);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    public void loadExcelDoc2() {
        BonusRegisterReportExcelTask task = new BonusRegisterReportExcelTask(MainApp.identityDto.getSociety().getCode(), cboxMember.getValue().getCode(), MainApp.locale, 0, cboxBank.getValue().getCode(), dpFromDate.getValue(), dpToDate.getValue(), cboxType.getSelectionModel().getSelectedIndex(), cboxMilkType.getValue().getCode().toString());
        task.setOnSucceeded(e -> {
            try {
                mapList = task.get();
                exportExcel1(mapList);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    public void loadExcelDoc3() {
        BonusRegisterReportExcelTask task = new BonusRegisterReportExcelTask(MainApp.identityDto.getSociety().getCode(), cboxMember.getValue().getCode(), MainApp.locale, 1, cboxBank.getValue().getCode(), dpFromDate.getValue(), dpToDate.getValue(), cboxType.getSelectionModel().getSelectedIndex(), cboxMilkType.getValue().getCode().toString());
        task.setOnSucceeded(e -> {
            try {
                mapList = task.get();
                exportExcel2(mapList);
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void loadBank() {
        var task = new BankLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Bank> list = task.get();
                List<Bank> list2 = new ArrayList<>();
                Bank m = new Bank();
                m.setCode("0");
                m.setName(resourceBundle.getString("allbank"));
                list2.add(m);
                if (list != null) list2.addAll(list);
                cboxBank.setItems(FXCollections.observableList(list2));
                cboxBank.getSelectionModel().select(0);

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadBonusData() {

        var task = new MemberWiseBonusLoadTask(dpFromDate.getValue(), dpToDate.getValue(), cboxMember.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                map = task.get();
                if (map != null) {
                    if (printerHelper != null) {
                        placeVariables(map, null);
                        print(masterLines);
                        masterLines.clear();
                    }
                }

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

    }

    private void loadBonusSummaryData() {
        var task = new MemberWiseBonusListLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                maplist = task.get();
                if (printerHelper != null) {
                    for (Map<String, Object> stringObjectMap : maplist) {
                        placeVariables1(stringObjectMap, null);
                        print(masterLines);
                        masterLines.clear();
                    }

                } else {
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void exportExcel(List<Map<String, Object>> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Bonus Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> finalResultToDisplay = new ArrayList<>();
                CellStyle style;
                DataFormat format = wb.createDataFormat();
                style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.0"));
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
                cell.setCellValue(dpFromDate.getValue().toString() + " - " + dpToDate.getValue().toString());
                sheet.autoSizeColumn(1);

                List<String> cols = new ArrayList<>();
                cols.addAll(colList);
                row = sheet.createRow(6);
                int cellValueHeading = 0;
                for (String columnTitle : cols) {

                    cell = row.createCell(cellValueHeading);
                    if (columnTitle.equalsIgnoreCase("member_code")) {
                        cell.setCellValue("Code");
                    } else if (columnTitle.equalsIgnoreCase("member_name")) {
                        cell.setCellValue("Member Name");
                    } else if (columnTitle.equalsIgnoreCase("bonus_amount")) {
                        cell.setCellValue("Bonus Amount");
                        index = cellValueHeading;
                    } else if (columnTitle.equalsIgnoreCase("kapat")) {
                        cell.setCellValue("Kapaat");
                    } else if (columnTitle.equalsIgnoreCase("total_amount")) {
                        cell.setCellValue("Total Amount");
                    } else {
                        cell.setCellValue(columnTitle);
                    }
                    sheet.autoSizeColumn(cellValueHeading);
                    cellValueHeading++;
                }

                for (String column : columns) {
                    finalResultToDisplay.add(column);
                }
                int rowCnt = 7;
                for (Map<String, Object> item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        if (columnTitle.equalsIgnoreCase("bonus_amount")) {
                            sheet.autoSizeColumn(cellValueHeading);
                            cell = row.createCell(cellValueHeading++);
                            cell.setCellValue((Double.parseDouble(item.get(columnTitle).toString())));
                            sheet.autoSizeColumn(cellValueHeading);
                            cell.setCellStyle(style);
                            sum = sum.add(new BigDecimal(item.get(columnTitle).toString()));
                        } else if (columnTitle.equalsIgnoreCase("kapat")) {
                            sheet.autoSizeColumn(cellValueHeading);
                            cell = row.createCell(cellValueHeading++);
                            cell.setCellValue(Double.parseDouble(item.get(columnTitle).toString()));
                            sheet.autoSizeColumn(cellValueHeading);
                            cell.setCellStyle(style);
                            sum1 = sum1.add(new BigDecimal(item.get(columnTitle).toString()));
                        } else if (columnTitle.equalsIgnoreCase("total_amount")) {
                            sheet.autoSizeColumn(cellValueHeading);
                            cell = row.createCell(cellValueHeading++);
                            cell.setCellValue(Double.parseDouble(item.get(columnTitle).toString()));
                            sheet.autoSizeColumn(cellValueHeading);
                            cell.setCellStyle(style);
                            sum2 = sum2.add(new BigDecimal(item.get(columnTitle).toString()));
                        } else {
                            sheet.autoSizeColumn(cellValueHeading);
                            cell = row.createCell(cellValueHeading++);
                            cell.setCellValue(item.get(columnTitle) != null ? item.get(columnTitle).toString() : "");
                            sheet.autoSizeColumn(cellValueHeading);
                        }
                    }
                    rowCnt++;
                }
                row = sheet.createRow(rowCnt);
                cell = row.createCell(index - 1);
                cell.setCellValue("Total");
                sheet.autoSizeColumn(index - 1);

                cell = row.createCell(index);
                cell.setCellValue(sum.doubleValue());
                sheet.autoSizeColumn(index);
                sum = BigDecimal.ZERO;

                cell = row.createCell(index + 1);
                cell.setCellValue(sum1.doubleValue());
                sheet.autoSizeColumn(index + 1);
                sum1 = BigDecimal.ZERO;

                cell = row.createCell(index + 2);
                cell.setCellValue(sum2.doubleValue());
                sheet.autoSizeColumn(index + 2);
                sum2 = BigDecimal.ZERO;

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
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("bonus"), resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("bonus"), resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    private void exportExcel1(List<Map<String, Object>> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Bonus Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> finalResultToDisplay = new ArrayList<>();
                CellStyle style;
                DataFormat format = wb.createDataFormat();
                style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.0"));
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
                cell.setCellValue(dpFromDate.getValue().toString() + " - " + dpToDate.getValue().toString());

                List<String> cols = new ArrayList<>();
                cols.addAll(colList);
                row = sheet.createRow(6);
                int cellValueHeading = 0;
                cell = row.createCell(cellValueHeading);
                cell.setCellValue("Sr. No");
                cellValueHeading++;
                for (String columnTitle : cols) {
                    finalResultToDisplay.add(columnTitle);
                    cell = row.createCell(cellValueHeading);
                    if (columnTitle.equalsIgnoreCase("account_no")) {
                        continue;
                    } else if (columnTitle.equalsIgnoreCase("member_name")) {
                        cell.setCellValue("Member Name");
                    } else if (columnTitle.equalsIgnoreCase("member_code")) {
                        cell.setCellValue("Code");
                    } else if (columnTitle.equalsIgnoreCase("bonus_amount")) {
                        cell.setCellValue("Bonus Amount");
                    } else if (columnTitle.equalsIgnoreCase("kapat")) {
                        cell.setCellValue("Kapaat");
                    } else if (columnTitle.equalsIgnoreCase("total_amount")) {
                        cell.setCellValue("Total Amount");
                    } else {
                        continue;
                    }
                    sheet.autoSizeColumn(cellValueHeading);
                    cellValueHeading++;
                }
                int rowCnt = 7;
                int count = 1;
                BigDecimal total = BigDecimal.ZERO;
                for (Map<String, Object> item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);

                    for (String columnTitle : finalResultToDisplay) {
                        if (cellValueHeading == 0) {
                            cell = row.createCell(cellValueHeading++);
                            cell.setCellValue(count++);
                        }
                        if (columnTitle.equalsIgnoreCase("bonus_amount") || columnTitle.equalsIgnoreCase("member_name") || columnTitle.equalsIgnoreCase("member_code") || columnTitle.equalsIgnoreCase("kapat") || columnTitle.equalsIgnoreCase("total_amount")) {
                            if (columnTitle.equalsIgnoreCase("bonus_amount")) {
                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(Double.parseDouble(String.valueOf(item.get(columnTitle))));
                                sheet.autoSizeColumn(cellValueHeading);
                                cell.setCellStyle(style);
                                finalAmount = finalAmount.add(new BigDecimal(item.get(columnTitle).toString()));

                            } else if (columnTitle.equalsIgnoreCase("kapat")) {
                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                if (item.get(columnTitle) != null)
                                    cell.setCellValue((Double.valueOf(item.get(columnTitle).toString())));
                                sheet.autoSizeColumn(cellValueHeading);
                                cell.setCellStyle(style);
                                kapat = kapat.add(new BigDecimal(item.get(columnTitle).toString()));
                            } else if (columnTitle.equalsIgnoreCase("total_amount")) {
                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue((Double.valueOf(item.get(columnTitle).toString())));
                                sheet.autoSizeColumn(cellValueHeading);
                                cell.setCellStyle(style);
                                totalamount = totalamount.add(new BigDecimal(item.get(columnTitle).toString()));
                            } else {

                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                if (item.get(columnTitle) != null)
                                    cell.setCellValue(item.get(columnTitle).toString());
                                sheet.autoSizeColumn(cellValueHeading);
                            }
                        }
                    }
                    rowCnt++;
                }

                row = sheet.createRow(rowCnt);
                cell = row.createCell(0);
                cell.setCellValue("TOTAL");
                cell = row.createCell(2);
                cell.setCellValue(total.doubleValue());
                cell.setCellStyle(style);
                sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 2));
                cell = row.createCell(3);
                cell.setCellValue(finalAmount.doubleValue());
                finalAmount = BigDecimal.ZERO;
                cell = row.createCell(4);
                cell.setCellValue(kapat.doubleValue());
                kapat = BigDecimal.ZERO;
                cell = row.createCell(5);
                cell.setCellValue(totalamount.doubleValue());
                totalamount = BigDecimal.ZERO;
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
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("bonus"), resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("bonus"), resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    private void exportExcel2(List<Map<String, Object>> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Bonus Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> finalResultToDisplay = new ArrayList<>();
                CellStyle style;
                DataFormat format = wb.createDataFormat();
                style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.0"));
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
                cell.setCellValue(dpFromDate.getValue().toString() + " - " + dpToDate.getValue().toString());

                List<String> cols = new ArrayList<>();
                cols.addAll(colList);
                row = sheet.createRow(6);
                int cellValueHeading = 0;
                cell = row.createCell(cellValueHeading);
                cell.setCellValue("Sr. No");
                cellValueHeading++;
                for (String columnTitle : cols) {
                    finalResultToDisplay.add(columnTitle);
                    cell = row.createCell(cellValueHeading);
                    if (columnTitle.equalsIgnoreCase("account_no")) {
                        cell.setCellValue("Account No.");
                    } else if (columnTitle.equalsIgnoreCase("member_name")) {
                        cell.setCellValue("Member Name");
                    } else if (columnTitle.equalsIgnoreCase("member_code")) {
                        cell.setCellValue("Code");
                    } else if (columnTitle.equalsIgnoreCase("bonus_amount")) {
                        cell.setCellValue("Bonus Amount");
                    } else if (columnTitle.equalsIgnoreCase("kapat")) {
                        cell.setCellValue("Kapaat");
                    } else if (columnTitle.equalsIgnoreCase("total_amount")) {
                        cell.setCellValue("Total Amount");
                    } else {
                        continue;
                    }
                    sheet.autoSizeColumn(cellValueHeading);
                    cellValueHeading++;
                }
                int rowCnt = 7;
                int count = 1;
                BigDecimal total = BigDecimal.ZERO;
                for (Map<String, Object> item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);

                    for (String columnTitle : finalResultToDisplay) {
                        if (cellValueHeading == 0) {
                            cell = row.createCell(cellValueHeading++);
                            cell.setCellValue(count++);
                        }
                        if (columnTitle.equalsIgnoreCase("bonus_amount") || columnTitle.equalsIgnoreCase("member_name") || columnTitle.equalsIgnoreCase("account_no") || columnTitle.equalsIgnoreCase("member_code") || columnTitle.equalsIgnoreCase("kapat") || columnTitle.equalsIgnoreCase("total_amount")) {
                            if (columnTitle.equalsIgnoreCase("bonus_amount")) {
                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(Double.parseDouble(item.get(columnTitle).toString()));
                                sheet.autoSizeColumn(cellValueHeading);
                                cell.setCellStyle(style);
                                finalAmount = finalAmount.add(new BigDecimal(item.get(columnTitle).toString()));

                            } else if (columnTitle.equalsIgnoreCase("kapat")) {
                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(Double.parseDouble(item.get(columnTitle).toString()));
                                sheet.autoSizeColumn(cellValueHeading);
                                cell.setCellStyle(style);
                                kapat = kapat.add(new BigDecimal(item.get(columnTitle).toString()));
                            } else if (columnTitle.equalsIgnoreCase("total_amount")) {
                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(Double.parseDouble(item.get(columnTitle).toString()));
                                sheet.autoSizeColumn(cellValueHeading);
                                cell.setCellStyle(style);
                                totalamount = totalamount.add(new BigDecimal(item.get(columnTitle).toString()));
                            } else {
                                sheet.autoSizeColumn(cellValueHeading);
                                cell = row.createCell(cellValueHeading++);
                                if (item.get(columnTitle) != null)
                                    cell.setCellValue(item.get(columnTitle).toString());
                                sheet.autoSizeColumn(cellValueHeading);
                            }
                        }
                    }
                    rowCnt++;
                }

                row = sheet.createRow(rowCnt);
                cell = row.createCell(0);
                cell.setCellValue("TOTAL");
                cell = row.createCell(3);
                cell.setCellValue(total.doubleValue());
                cell.setCellStyle(style);
                sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 3));
                cell = row.createCell(4);
                cell.setCellValue(finalAmount.doubleValue());
                finalAmount = BigDecimal.valueOf(0);
                cell = row.createCell(5);
                cell.setCellValue(kapat.doubleValue());
                kapat = BigDecimal.valueOf(0);
                cell = row.createCell(6);
                cell.setCellValue(totalamount.doubleValue());
                totalamount = BigDecimal.valueOf(0);

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
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("bonus"), resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("bonus"), resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void loadMember() {
        MemberLoadTask task = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Member> list = task.get();
                if (list != null) {
                    list2 = new ArrayList<>();
                    Member m = new Member();
                    m.setCode("0");
                    m.setCodeEx("0");
                    m.setFirstName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxMember.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxMember);
                    cboxMember.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();


        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null && !list.isEmpty()) {
                    listMilkType = new ArrayList<>();
                    listMilkType.add(0, new MilkType(0, MainApp.bundle.getString("all")));
                    listMilkType.addAll(list);
                    cboxMilkType.setItems(FXCollections.observableList(listMilkType));
                    cboxMilkType.getSelectionModel().select(0);
//                    cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

    private void readFile() {
        try {
            if (slipLanguage.equalsIgnoreCase("English")) slipFile = new File("resources/collection/BonusSlip.txt");
            else if (slipLanguage.equalsIgnoreCase("Hindi"))
                slipFile = new File("resources/collection/BonusSlipLocalHindi.txt");
            else slipFile = new File("resources/collection/BonusSlipLocal.txt");
        } catch (Exception exception) {
            System.out.println("Unexcpected error occurred!");
            exception.printStackTrace();
        }
    }

    private void print(List<String> masterLines) {
        for (int i = 0; i < Integer.parseInt(MainApp.getProperty("no.of.enter", "0")); i++) {
            masterLines.add("\n");
        }
        masterLines.add(".");
        printerHelper.print(masterLines);
    }

    private void placeVariables(Map<String, Object> map, Object[] resp) {
        List<String> lines = null;
        try {
            masterLines.clear();
            lines = Files.readAllLines(slipFile.toPath(), StandardCharsets.UTF_8);

            if (lines == null) return;
            for (int i = 0; i < lines.size(); ) {
                System.out.println(i);
                String s = lines.get(i);
                if (s.contains("{dcsshort}")) {
                    s = s.replace("{dcsshort}", MainApp.identityDto.getSociety().getName() + "-" + MainApp.identityDto.getSociety().getCodeEx());
                }
                if (s.contains("{membername}")) {
                    if (map.get("member_code") != null) {
                        if (MainApp.getProperty("slip.language", "").equalsIgnoreCase("Gujarati"))
                            s = s.replace("{membername}", list2.stream().filter(p -> p.getCode().equalsIgnoreCase((String) map.get("member_code"))).findAny().get().toMemberName("gu"));
                        else
                            s = s.replace("{membername}", list2.stream().filter(p -> p.getCode().equalsIgnoreCase((String) map.get("member_code"))).findAny().get().toMemberName("en"));
                    } else {
                        i += 1;
                        continue;
                    }
                }
                if (s.contains("{code}")) {
                    s = s.replace("{code}", cboxMember.getValue().getCodeEx());
                }
                if (s.contains("{fromdate}")) {
                    s = s.replace("{fromdate}", dpFromDate.getValue().toString());
                }
                if (s.contains("{todate}")) {
                    s = s.replace("{todate}", dpToDate.getValue().toString());
                }
                if (s.contains("{amount}")) {
                    s = s.replace("{amount}", String.valueOf(map.get("amt")));
                }
                if (s.contains("{qty}")) {
                    s = s.replace("{qty}", String.valueOf(map.get("qty")));

                }
                if (s.contains("{bonus}")) {
                    s = s.replace("{bonus}", String.valueOf(map.get("bonus")));
                }
                if (s.contains("{kapaat}")) {
                    s = s.replace("{kapaat}", String.valueOf(map.get("kapaat")));
                }
                if (s.contains("{total}")) {
                    if (map.get("kapaat") != null)
                        s = s.replace("{total}", BigDecimal.valueOf((Double) map.get("bonus") - (Double) map.get("kapaat")).setScale(2, RoundingMode.HALF_DOWN) + "(" + BigDecimal.valueOf((Double) map.get("bonus") * 100 / (Double) map.get("amt")).setScale(2, RoundingMode.HALF_DOWN) + "%)");
                    else
                        s = s.replace("{total}", BigDecimal.valueOf((Double) map.get("bonus") - 0).setScale(2, RoundingMode.HALF_DOWN) + "(" + BigDecimal.valueOf((Double) map.get("bonus") * 100 / (Double) map.get("amt")).setScale(2, RoundingMode.HALF_DOWN) + "%)");

                }

                lines.set(i, s);
                if (s.contains("{")) continue;
                else i++;

                masterLines.add(s);
                s = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void placeVariables1(Map<String, Object> map, Object[] resp) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(slipFile.toPath(), StandardCharsets.UTF_8);

            if (lines == null) return;
            for (int i = 0; i < lines.size(); ) {
                System.out.println(i);
                String s = lines.get(i);
                if (s.contains("{dcsshort}")) {
                    s = s.replace("{dcsshort}", MainApp.identityDto.getSociety().getName() + "-" + MainApp.identityDto.getSociety().getCodeEx());
                }

                if (s.contains("{membername}")) {
                    if (map.get("member_code") != null) {
                        if (MainApp.getProperty("slip.language", "").equalsIgnoreCase("Gujarati"))
                            s = s.replace("{membername}", list2.stream().filter(p -> p.getCode().equalsIgnoreCase((String) map.get("member_code"))).findAny().get().toMemberName("gu"));
                        else
                            s = s.replace("{membername}", list2.stream().filter(p -> p.getCode().equalsIgnoreCase((String) map.get("member_code"))).findAny().get().toMemberName("en"));
                    } else {
                        i += 1;
                        continue;
                    }
                }
                if (s.contains("{code}")) {
                    s = s.replace("{code}", list2.stream().filter(p -> p.getCode().equalsIgnoreCase((String) map.get("member_code"))).findAny().get().getCodeEx());
                }
                if (s.contains("{fromdate}")) {
                    s = s.replace("{fromdate}", dpFromDate.getValue().toString());
                }
                if (s.contains("{todate}")) {
                    s = s.replace("{todate}", dpToDate.getValue().toString());
                }
                if (s.contains("{amount}")) {
                    s = s.replace("{amount}", String.valueOf(map.get("amt")));
                }
                if (s.contains("{qty}")) {
                    s = s.replace("{qty}", String.valueOf(map.get("qty")));

                }
                if (s.contains("{bonus}")) {
                    s = s.replace("{bonus}", String.valueOf(map.get("bonus")));
                }
                if (s.contains("{kapaat}")) {
                    s = s.replace("{kapaat}", String.valueOf(map.get("kapaat")));
                }
                if (s.contains("{total}")) {
                    if (map.get("kapaat") != null)
                        s = s.replace("{total}", BigDecimal.valueOf((Double) map.get("bonus") - (Double) map.get("kapaat")).setScale(2, RoundingMode.HALF_DOWN) + "(" + BigDecimal.valueOf((Double) map.get("bonus") * 100 / (Double) map.get("amt")).setScale(2, RoundingMode.HALF_DOWN) + "%)");
                    else
                        s = s.replace("{total}", BigDecimal.valueOf((Double) map.get("bonus") - 0).setScale(2, RoundingMode.HALF_DOWN) + "(" + BigDecimal.valueOf((Double) map.get("bonus") * 100 / (Double) map.get("amt")).setScale(2, RoundingMode.HALF_DOWN) + "%)");

                }
                lines.set(i, s);
                if (s.contains("{")) continue;
                else i++;

                masterLines.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}