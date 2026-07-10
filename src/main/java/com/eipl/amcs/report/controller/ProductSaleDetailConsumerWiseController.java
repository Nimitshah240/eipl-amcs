package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductCellFactory;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductLocalCellFactory;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JRParameter;

public class ProductSaleDetailConsumerWiseController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate;
    @FXML
    private E_DatePicker dpToDate, dpFromDate;
    @FXML
    private AutoSearchTextField<Product> cboxProductCode;
    @FXML
    private AutoSearchTextField<String> cboxFormat,cboxLanguage;

    private ResourceBundle resourceBundle;
    private List<Product> listProduct;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        cboxFormat.getItems().addAll("Format 1", "Format 2", "Format 3");
        cboxFormat.getSelectionModel().select(0);

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

        loadData();
        setupComboBox();

        btnGenerate.setOnAction(e -> validateAndGenerate());

        String[] arr = MainApp.getProperty(AppConstant.Props.APP_LANGUAGE, "Gujarati,Hindi,English").split(",");
        cboxLanguage.setItems(FXCollections.observableList(Arrays.asList(arr)));
        if (MainApp.getLocale().equalsIgnoreCase("gu")) {
            cboxLanguage.setValue("Gujarati");
        } else if (MainApp.getLocale().equalsIgnoreCase("hi")) {
            cboxLanguage.setValue("Hindi");
        } else {
            cboxLanguage.setValue("English");
        }
    }

    private void validateAndGenerate() {
        switch (cboxFormat.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                validateAndGenerateReport();
                break;
            case 2:
                validateAndGenerateReportTwo();
                break;
            case 3:
                validateAndGenerateReportThree();
                break;
        }
    }

    @Override
    public void setupComboBox() {
//        cboxProductCode.setConverter(new ProductConvertor(cboxProductCode));

//        new AutoCompleteComboBoxListener<>(cboxProductCode);
    }
    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_product_code", cboxProductCode.getValue().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PRODUCT_SALE_DETAIL_CONSUMER_WISE, params);
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReportTwo() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_product_code", cboxProductCode.getValue().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PRODUCT_SALE_DETAILS, params);
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReportThree() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", dpFromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("p_to_date", dpToDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.CODE_WISE_PRODUCT_SALE_DETAIL, params);
        JasperViewer.viewReport(print, false);
    }

    private boolean validate() {
        return true;
    }

    @Override
    public void loadData() {
        var task1 = new ProductLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Product> list = task1.get().stream().filter(ee -> ee.getCreatedBy() == null ||
                        ee.getCreatedBy().equalsIgnoreCase("SYSTEM")).collect(Collectors.toList());
                if (list != null && !list.isEmpty()) {
                    listProduct = new ArrayList<>();
                    listProduct.add(new Product("0", "ALL", "બધા"));
                    listProduct.addAll(1, list);
                    cboxProductCode.setItems(FXCollections.observableList(listProduct));
                    cboxProductCode.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }
}
