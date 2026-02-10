package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.master.procurement.converter.SocietyPaymentCycleConvertor;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleLoadTask;
import com.eipl.amcs.operation.procurement.dto.PrinterHelper;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import com.eipl.amcs.setting.task.HardwareDeviceConfigLoadTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.eipl.amcs.utils.AppConstant.DB_LOC;

public class MemberMilkCollectionSlipController implements MyInitialization {

    private final ArrayList<String> masterLines = new ArrayList<>();
    List<Map<String, Object>> data = new ArrayList<>();
    String slipLanguage = "";
    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnPrint;
    @FXML
    private ComboBox<Member> cboxMemberCode;
    @FXML
    private TextField txtRange;
    @FXML
    private ComboBox<SocietyPaymentCycle> cboxSocietyPaymentCycleCode;
    private List<SocietyPaymentCycle> paymentCycleList = new ArrayList<>();
    private ResourceBundle resourceBundle;
    private File slipFile = null;
    private PrinterHelper printerHelper;

    public static String leftPadding(String input, char ch, int L) {
        String result = String.format("%" + L + "s", input).replace(' ', ch);
        return result;
    }

    // Function to perform right padding
    public static String rightPadding(String input, char ch, int L) {
        String result = String.format("%" + (-L) + "s", input).replace(' ', ch);
        return result;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtRange.setText("0");
        this.resourceBundle = resourceBundle;
        loadData();
        setupComboBox();
        readFile();
        loadHardware();
        slipLanguage = MainApp.getProperty("slip.language", "English");
        btnPrint.setOnAction(e -> {
            if (txtRange.getText().equalsIgnoreCase("0") || txtRange.getText() == null || txtRange.getText().equalsIgnoreCase(""))
                loadTextFile();
            else {
                String[] arr = txtRange.getText().split("-");
                for (int i = Integer.parseInt(arr[0]); i <= Integer.parseInt(arr[1]); i++) {
                    String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", i);
                    loadTextFilePartTwo(code);
                }
            }
        });
        btnGenerate.setOnAction(e -> {
            if (txtRange.getText().equalsIgnoreCase("0") || txtRange.getText() == null || txtRange.getText().equalsIgnoreCase(""))
                validateAndGenerateReport();
            else {
                String[] arr = txtRange.getText().split("-");
                for (int i = Integer.parseInt(arr[0]); i <= Integer.parseInt(arr[1]); i++) {
                    String code = MainApp.identityDto.getSociety().getCode() + String.format("%04d", i);
                    validateAndGenerateReport2(code);
                }
            }
        });
    }

    public void loadHardware() {
        HardwareDeviceConfigLoadTask task = new HardwareDeviceConfigLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<HardwareDeviceConfig> list = task.get();
                if (list != null) {
                    String printer = getPrinterName(list);
                    if (printer != null && !printer.isEmpty()) {
                        if (printerHelper == null) printerHelper = new PrinterHelper(printer, "Arial Unicode MS", 11);
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private String getPrinterName(List<HardwareDeviceConfig> devices) {
        for (HardwareDeviceConfig device : devices) {
            if (device.getDeviceType().equalsIgnoreCase("PRINTER")) {
                return device.getxCol1();
            }
        }
        return null;
    }

    @Override
    public void setupComboBox() {
        cboxMemberCode.setConverter(new MemberReportConvertor(cboxMemberCode));
        cboxMemberCode.setCellFactory(new MemberCellFactory());
        cboxSocietyPaymentCycleCode.setConverter(new SocietyPaymentCycleConvertor(cboxSocietyPaymentCycleCode));
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_member_code", cboxMemberCode.getValue().getCode());
        params.put("p_society_payment_cycle_code", cboxSocietyPaymentCycleCode.getValue().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_MILK_COLLECTION_SLIP, params);
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReport2(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_member_code", code);
        params.put("p_society_payment_cycle_code", cboxSocietyPaymentCycleCode.getValue().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_MILK_COLLECTION_SLIP, params);
        JasperViewer.viewReport(print, false);
    }

    private void loadTextFile() {
        masterLines.clear();
        data.clear();
        String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
        String query = "call rpt_member_milk_collection_slip('" + MainApp.identityDto.getSociety().getCode() + "','" +
                cboxSocietyPaymentCycleCode.getValue().getCode() + "','" + cboxMemberCode.getValue().getCode() + "','" + MainApp.getLocale() + "')";
        try (Connection connection = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("member_code_ex", rs.getString("member_code_ex"));
                map.put("member_original_code", rs.getString("member_original_code"));
                map.put("member_name", rs.getString("member_name"));
                map.put("collection_date", rs.getString("collection_date"));
                map.put("quantity", rs.getString("quantity"));
                map.put("fat", rs.getString("fat"));
                map.put("snf", rs.getString("snf"));
                map.put("amount", rs.getString("amount"));
                map.put("rate", rs.getString("rate"));
                map.put("animal_type_name", rs.getString("animal_type_name"));
                map.put("s_code", rs.getString("s_code"));
                map.put("pc_code", rs.getString("pc_code"));
                map.put("society_payment_cycle_code", rs.getString("society_payment_cycle_code"));
                data.add(map);
            }
            placeVariables(data);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTextFilePartTwo(String code) {
        masterLines.clear();
        data.clear();
        String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
        String query = "call rpt_member_milk_collection_slip('" + MainApp.identityDto.getSociety().getCode() + "','" +
                cboxSocietyPaymentCycleCode.getValue().getCode() + "','" + code + "','" + MainApp.getLocale() + "')";
        try (Connection connection = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("member_code_ex", rs.getString("member_code_ex"));
                map.put("member_original_code", rs.getString("member_original_code"));
                map.put("member_name", rs.getString("member_name"));
                map.put("collection_date", rs.getString("collection_date"));
                map.put("quantity", rs.getString("quantity"));
                map.put("fat", rs.getString("fat"));
                map.put("snf", rs.getString("snf"));
                map.put("amount", rs.getString("amount"));
                map.put("rate", rs.getString("rate"));
                map.put("animal_type_name", rs.getString("animal_type_name"));
                map.put("s_code", rs.getString("s_code"));
                map.put("pc_code", rs.getString("pc_code"));
                map.put("society_payment_cycle_code", rs.getString("society_payment_cycle_code"));
                data.add(map);
            }
            placeVariables(data);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTextFileForDeduction(String code) {
        String mysqlUrl = "jdbc:mysql://" + DB_LOC + ":3366/" + AppConstant.EIPL_DB_NAME;
        String query = "call rpt_member_milk_collection_slip_head_wise('" + MainApp.identityDto.getSociety().getCode() + "','" +
                cboxSocietyPaymentCycleCode.getValue().getCode() + "','" + code + "','" + MainApp.getLocale() + "')";
        try (Connection connection = DriverManager.getConnection(mysqlUrl, "root", AppConstant.EIPL_DB_PASS)) {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("member_code", rs.getString("member_code"));
                map.put("head_name", rs.getString("head_name"));
                map.put("amount", rs.getString("amount"));
                masterLines.add(map.get("head_name").toString() + " - " + map.get("amount").toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void placeVariables(List<Map<String, Object>> mapList) {

        Map<Object, List<Map<String, Object>>> mapMemberCollection = mapList.stream()
                .collect(Collectors.groupingBy(stringObjectMap -> stringObjectMap.get("member_code_ex")));
        Map<Object, List<Map<String, Object>>> mapMemberCollection1 = new TreeMap<>(mapMemberCollection);
        mapMemberCollection1.forEach((key, val) -> {
            if (MainApp.getLocale().equalsIgnoreCase("en")) {
                masterLines.add("-----------Collection Slip-------------");
                masterLines.add("Society: " + MainApp.identityDto.getSociety().getCode() + "-" + MainApp.identityDto.getSociety().getName());
                masterLines.add("Code: " + val.get(0).get("member_code_ex"));
                masterLines.add("Name: " + val.get(0).get("member_name"));
                masterLines.add("Period: " + val.get(0).get("pc_code"));
                masterLines.add("---------------------------------------");
                masterLines.add("Date  Milk  Qty    FAT    Rate   Amount");
                masterLines.add("---------------------------------------");
                StringBuilder stringBuilder = new StringBuilder();
                BigDecimal qty = BigDecimal.ZERO;
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal fat = BigDecimal.ZERO;
                for (Map<String, Object> m : val) {
                    stringBuilder.append(m.get("collection_date").toString().replace(" ", ""));
                    stringBuilder.append(String.format("%4s", m.get("animal_type_name").toString().charAt(0)));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("quantity").toString())));
                    qty = qty.add(new BigDecimal(m.get("quantity").toString()));
                    stringBuilder.append(String.format("%7.2f", new BigDecimal(m.get("fat").toString())));
                    fat = fat.add(new BigDecimal(m.get("fat").toString()).multiply(new BigDecimal(m.get("quantity").toString())).divide(BigDecimal.valueOf(100)));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("rate").toString())));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("amount").toString())));
                    amount = amount.add(new BigDecimal(m.get("amount").toString()));
                    masterLines.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
                masterLines.add("---------------------------------------");
                stringBuilder.append(String.format("%6s", "Total"));
                stringBuilder.append(String.format("%11.2f", new BigDecimal(qty.toString())));
                stringBuilder.append(String.format("%7.2f", fat.divide(qty, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100))));
                stringBuilder.append(String.format("%16.2f", new BigDecimal(amount.toString())));
                masterLines.add(stringBuilder.toString());
                stringBuilder.setLength(0);

                loadTextFileForDeduction((String) val.get(0).get("member_original_code"));


                for (int i = 0; i < Integer.parseInt(MainApp.getProperty("no.of.enters.collection.slip", "0")); i++) {
                    masterLines.add("\n");
                }
                print(masterLines);
                masterLines.clear();
            }
            else if (MainApp.getLocale().equalsIgnoreCase("gu")){
                masterLines.add("----------------સંગ્રહ કાપલી---------------");
                if (MainApp.identityDto.getSociety().getNameLocal() != null) {
                    masterLines.add("મંડળી: " + MainApp.identityDto.getSociety().getCode() + "-" +
                            MainApp.identityDto.getSociety().getNameLocal());
                } else {
                    masterLines.add("મંડળી: " + MainApp.identityDto.getSociety().getCode() + "-" +
                            MainApp.identityDto.getSociety().getName());
                }
                masterLines.add("કોડ: " + val.get(0).get("member_code_ex"));
                masterLines.add("નામ: " + val.get(0).get("member_name"));
                masterLines.add("સમયગાળો: " + val.get(0).get("pc_code"));
                masterLines.add("---------------------------------------");
                masterLines.add("તારીખ   દૂધ    લિટર     ફેટ      રેટ      રકમ");
                masterLines.add("---------------------------------------");
                StringBuilder stringBuilder = new StringBuilder();
                BigDecimal qty = BigDecimal.ZERO;
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal fat = BigDecimal.ZERO;
                for (Map<String, Object> m : val) {
                    stringBuilder.append(m.get("collection_date").toString().replace(" ", ""));
                    stringBuilder.append(String.format("%4s", m.get("animal_type_name").toString().charAt(0)));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("quantity").toString())));
                    qty = qty.add(new BigDecimal(m.get("quantity").toString()));
                    stringBuilder.append(String.format("%7.2f", new BigDecimal(m.get("fat").toString())));
                    fat = fat.add(new BigDecimal(m.get("fat").toString()).multiply(new BigDecimal(m.get("quantity").toString())).divide(BigDecimal.valueOf(100)));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("rate").toString())));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("amount").toString())));
                    amount = amount.add(new BigDecimal(m.get("amount").toString()));
                    masterLines.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
                masterLines.add("---------------------------------------");
                stringBuilder.append(String.format("%6s", "Total"));
                stringBuilder.append(String.format("%11.2f", new BigDecimal(qty.toString())));
                stringBuilder.append(String.format("%7.2f", fat.divide(qty, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100))));
                stringBuilder.append(String.format("%16.2f", new BigDecimal(amount.toString())));
                masterLines.add(stringBuilder.toString());
                stringBuilder.setLength(0);

                loadTextFileForDeduction((String) val.get(0).get("member_original_code"));
                for (int i = 0; i < Integer.parseInt(MainApp.getProperty("no.of.enters.collection.slip", "0")); i++) {
                    masterLines.add("\n");
                }
                print(masterLines);
                masterLines.clear();
            } else if (MainApp.getLocale().equalsIgnoreCase("hi")){
                masterLines.add("----------------संग्रहण पर्ची---------------");
                if (MainApp.identityDto.getSociety().getNameLocal() != null) {
                    masterLines.add("मंडरी: " + MainApp.identityDto.getSociety().getCode() + "-" +
                            MainApp.identityDto.getSociety().getNameLocal());
                } else {
                    masterLines.add("मंडरी: " + MainApp.identityDto.getSociety().getCode() + "-" +
                            MainApp.identityDto.getSociety().getName());
                }
                masterLines.add("कोड: " + val.get(0).get("member_code_ex"));
                masterLines.add("नाम: " + val.get(0).get("member_name"));
                masterLines.add("अवधि: " + val.get(0).get("pc_code"));
                masterLines.add("---------------------------------------");
                masterLines.add("तारीख   दूध    लीटर     फेट      दर      रुपये");
                masterLines.add("---------------------------------------");
                StringBuilder stringBuilder = new StringBuilder();
                BigDecimal qty = BigDecimal.ZERO;
                BigDecimal amount = BigDecimal.ZERO;
                BigDecimal fat = BigDecimal.ZERO;
                for (Map<String, Object> m : val) {
                    stringBuilder.append(m.get("collection_date").toString().replace(" ", ""));
                    stringBuilder.append(String.format("%4s", m.get("animal_type_name").toString().charAt(0)));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("quantity").toString())));
                    qty = qty.add(new BigDecimal(m.get("quantity").toString()));
                    stringBuilder.append(String.format("%7.2f", new BigDecimal(m.get("fat").toString())));
                    fat = fat.add(new BigDecimal(m.get("fat").toString()).multiply(new BigDecimal(m.get("quantity").toString())).divide(BigDecimal.valueOf(100)));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("rate").toString())));
                    stringBuilder.append(String.format("%8.2f", new BigDecimal(m.get("amount").toString())));
                    amount = amount.add(new BigDecimal(m.get("amount").toString()));
                    masterLines.add(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
                masterLines.add("---------------------------------------");
                stringBuilder.append(String.format("%6s", "कुल"));
                stringBuilder.append(String.format("%11.2f", new BigDecimal(qty.toString())));
                stringBuilder.append(String.format("%7.2f", fat.divide(qty, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100))));
                stringBuilder.append(String.format("%16.2f", new BigDecimal(amount.toString())));
                masterLines.add(stringBuilder.toString());
                stringBuilder.setLength(0);

                loadTextFileForDeduction((String) val.get(0).get("member_original_code"));
                for (int i = 0; i < Integer.parseInt(MainApp.getProperty("no.of.enters.collection.slip", "0")); i++) {
                    masterLines.add("\n");
                }
                print(masterLines);
                masterLines.clear();
            }
        });
    }

    private void print(List<String> masterLines) {
        printerHelper.print(masterLines);
    }


    private void readFile() {
        try {
            if (slipLanguage.equalsIgnoreCase("English")) slipFile = new File("resources/collection/Slip.txt");
            else slipFile = new File("resources/collection/Slip.txt");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private boolean validate() {
        return true;
    }

    @Override
    public void loadData() {
        MemberLoadTask task = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Member> list = task.get();
                if (list != null) {
                    List<Member> list2 = new ArrayList<>();
                    Member m = new Member();
                    m.setCode("0");
                    m.setCodeEx("0");
                    m.setFirstName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxMemberCode.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxMemberCode);
                    cboxMemberCode.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new SocietyPaymentCycleLoadTask();
        task1.setOnSucceeded(e -> {
            paymentCycleList = new ArrayList<>();
            List<SocietyPaymentCycle> paymentCycleList1 = new ArrayList<>();
            try {
                paymentCycleList.addAll(task1.get());
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            paymentCycleList1.addAll(paymentCycleList.stream().filter(e1 -> e1.getFromDate().toLocalDate().isAfter(LocalDate.now().minusMonths(3))).collect(Collectors.toList()));
            cboxSocietyPaymentCycleCode.setItems(FXCollections.observableList(paymentCycleList1));
            cboxSocietyPaymentCycleCode.getSelectionModel().select(0);
        });
        new Thread(task1).start();
    }
}
