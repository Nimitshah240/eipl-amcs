package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.master.operation.model.Vendor;
import com.eipl.amcs.master.operation.task.VendorLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ItemReportController implements MyInitialization {

    @FXML
    private Button btnGenerate, btnGenerate1, btnClose;
    @FXML
    private AutoSearchTextField<Ledger> cboxLedgerName, cboxLedgerName1;
    @FXML
    private AutoSearchTextField<Vendor> cboxCustomer;
    @FXML
    private AutoSearchTextField<String> cboxSaleType;
    @FXML
    private E_DatePicker dpFromDate, dpToDate, dpFromDate1, dpToDate1;
    @FXML
    private SwingNode reportNode;
    @FXML
    private AnchorPane root;
    @FXML
    private AutoSearchTextField<String> cboxLanguage, cboxLanguage1;

    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        loadData();
        setupComboBox();
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        List<String> saleType = new ArrayList<>();
        saleType.add(resourceBundle.getString("all"));
        saleType.add("Cash");
        saleType.add("Credit");
        cboxSaleType.setItems(FXCollections.observableList(saleType));
        cboxSaleType.getSelectionModel().select(0);

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage.setValue("Gujarati");
            cboxLanguage1.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage.setValue("Hindi");
            cboxLanguage1.setValue("Hindi");
        } else {
            cboxLanguage.setValue("English");
            cboxLanguage1.setValue("English");
        }

//        cboxLedgerName1.setConverter(new LedgerConvertor(cboxLedgerName1));
//        cboxLedgerName.setConverter(new LedgerConvertor(cboxLedgerName));
//        cboxCustomer.setConverter(new CustomerConvertor(cboxCustomer));

    }


    //    FOR PURCHASE
    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", String.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", String.valueOf(dpToDate.getValue()));
        params.put("p_customer_code", cboxCustomer.getValue().getCode());
        params.put("p_ledger_code", cboxLedgerName.getValue().getCode());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.ITEM_PURCHASE_REGISTER, params);
        JasperViewer.viewReport(print, false);
    }

    //    FOR SALE
    private void validateAndGenerateReport1() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", String.valueOf(dpFromDate1.getValue()));
        params.put("p_to_date", String.valueOf(dpToDate1.getValue()));
        params.put("p_sales_type", (cboxSaleType.getSelectionModel().getSelectedIndex()));
        params.put("p_ledger_code", cboxLedgerName1.getValue().getCode());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.ITEM_SALE_REGISTER, params);
        JasperViewer.viewReport(print, false);
    }

    @Override
    public void setupComboBox() {
//        cboxLedgerName.setConverter(new LedgerConvertor(cboxLedgerName));
//        cboxLedgerName.setCellFactory(new LedgerCellFactory());
        cboxLedgerName.getSelectionModel().select(0);
    }

    @Override
    public void loadData() {
        LedgerLoadTask task = new LedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Ledger> list = task.get();
                if (list != null) {
                    List<Ledger> list2 = new ArrayList<>();
                    Ledger m = new Ledger();
                    m.setCode("0");
                    m.setName(resourceBundle.getString("all"));
                    list2.add(m);
                    list2.addAll(list);
                    cboxLedgerName.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxLedgerName);
                    cboxLedgerName.getSelectionModel().select(0);
                    cboxLedgerName1.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxLedgerName1);
                    cboxLedgerName1.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        VendorLoadTask task1 = new VendorLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Vendor> list = task1.get();
                if (list != null) {
                    List<Vendor> list2 = new ArrayList<>();
                    Vendor m = new Vendor();
                    m.setCode("0");
                    m.setVendorName("All");
                    m.setVendorNameLocal(resourceBundle.getString("all"));
                    list2.add(m);
                    list2.addAll(list);
                    cboxCustomer.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxCustomer);
                    cboxCustomer.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }
}
