package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.inventory.convertor.ProductCellFactory;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class StockReportController implements MyInitialization {

    @FXML
    private Button btnGenerate, btnGenerate1, btnClose,btnGenerate11;
    @FXML
    private AutoSearchTextField<Product> cboxProduct, cboxProduct1;
    @FXML
    private E_DatePicker dpFromDate, dpToDate, dpFromDate1, dpToDate1,dpFromDate11,dpToDate11;
    @FXML
    private SwingNode reportNode;
    @FXML
    private AnchorPane root;
    @FXML
    private AutoSearchTextField<String> cboxLanguage, cboxLanguage1,cboxLanguage11;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
//        cboxProduct1.setConverter(new ProductConvertor(cboxProduct1));
//
//        cboxProduct.setCellFactory(new ProductCellFactory());
//        cboxProduct1.setCellFactory(new ProductCellFactory());
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        dpFromDate1.setValue(LocalDate.now());
        dpToDate1.setValue(LocalDate.now());
        dpFromDate11.setValue(LocalDate.now());
        dpToDate11.setValue(LocalDate.now());
        loadData();
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnGenerate1.setOnAction(e -> validateAndGenerateReport1());
        btnGenerate11.setOnAction(e -> validateAndGenerateReport2());

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });


        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLanguage1.setItems(FXCollections.observableList(Arrays.asList(arr)));
        cboxLanguage11.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage.setValue("Gujarati");
            cboxLanguage1.setValue("Gujarati");
            cboxLanguage11.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage.setValue("Hindi");
            cboxLanguage1.setValue("Hindi");
            cboxLanguage11.setValue("Hindi");
        } else {
            cboxLanguage.setValue("English");
            cboxLanguage1.setValue("English");
            cboxLanguage11.setValue("English");
        }

    }


    //    FOR STOCK STATEMENT
    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", String.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", String.valueOf(dpToDate.getValue()));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PRODUCT_STOCK_STATEMENT, params);
        JasperViewer.viewReport(print, false);
    }

    //    FOR STOCK LEDGER
    private void validateAndGenerateReport1() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = cboxLanguage1.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", String.valueOf(dpFromDate1.getValue()));
        params.put("p_to_date", String.valueOf(dpToDate1.getValue()));
        params.put("p_product_code", cboxProduct.getSelectionModel().getSelectedItem().getCode());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PRODUCT_STOCK_LEDGER, params);
        JasperViewer.viewReport(print, false);
    }

    //    FOR STOCK LEDGER SUMMARY
    private void validateAndGenerateReport2() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = cboxLanguage11.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", String.valueOf(dpFromDate11.getValue()));
        params.put("p_to_date", String.valueOf(dpToDate11.getValue()));
        params.put("p_product_code", cboxProduct1.getSelectionModel().getSelectedItem().getCode());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = null;

        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PRODUCT_STOCK_LEDGER_SUMMARY, params);
        JasperViewer.viewReport(print, false);
    }

    @Override
    public void setupComboBox() {

    }

    @Override
    public void loadData() {
        ProductLoadTask task = new ProductLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Product> list = task.get();
                if (list != null) {
                    List<Product> list2 = new ArrayList<>();
                    Product m = new Product();
                    m.setCode("0");
                    m.setName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxProduct.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxProduct);
                    cboxProduct.getSelectionModel().select(0);
                    cboxProduct1.setItems(FXCollections.observableList(list2));
//                    new AutoCompleteComboBoxListener<>(cboxProduct1);
                    cboxProduct1.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
