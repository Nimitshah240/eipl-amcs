package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.inventory.convertor.ProductCellFactory;
import com.eipl.amcs.master.inventory.convertor.ProductConvertor;
import com.eipl.amcs.master.inventory.convertor.ProductLocalCellFactory;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.dto.ProductStockValuationWithSaleAndPurchase;
import com.eipl.amcs.report.task.StockValuationTaskWithProduct;
import com.eipl.amcs.report.task.StockValuationTaskWithSaleAndPurchase;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.FocusUtils;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class RptStockValuationController implements MyInitialization {
    @FXML
    private Button btnClose;

    @FXML
    private Button btnGenerate;
    @FXML
    private Label lblAsOnDate;

    @FXML
    private DatePicker dpAsOnDate, dpFromDate, dpToDate;

    @FXML
    private SwingNode reportNode;
    @FXML
    private ComboBox<Product> cboxProduct;
    @FXML
    private ComboBox<String> cboxFormat;
    private List<Product> listProductList;

    @FXML
    private AnchorPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cboxFormat.getItems().addAll("Format 1", "Format 2");
        cboxFormat.getSelectionModel().select(0);
        dpAsOnDate.setValue(LocalDate.now());
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        loadProduct();
        setupComboBox();
        cboxFormat.setOnAction(e -> {
            if (cboxFormat.getSelectionModel().getSelectedIndex() == 0) {
                dpToDate.setDisable(true);
                lblAsOnDate.setText(resources.getString("asondate"));
            } else {
                dpToDate.setDisable(false);
                lblAsOnDate.setText(resources.getString("fromdate"));
            }
        });
        FocusUtils.requestFocus(btnGenerate);
        btnGenerate.setOnAction(e -> {
            validateAndGenerate();
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

    }

    private void validateAndGenerate() {
        switch (cboxFormat.getSelectionModel().getSelectedIndex()) {
            case 0:
                loadData();
                break;
            case 1:
                loadData1();
                break;
        }
    }

    @Override
    public void setupComboBox() {
        cboxProduct.setConverter(new ProductConvertor(cboxProduct));
        if (MainApp.locale.equalsIgnoreCase("gu")) {
            cboxProduct.setCellFactory(new ProductLocalCellFactory());
        } else {
            cboxProduct.setCellFactory(new ProductCellFactory());
        }
        new AutoCompleteComboBoxListener<>(cboxProduct);
    }

    @Override
    public void loadData() {
        StockValuationTaskWithProduct task = new StockValuationTaskWithProduct(MainApp.identityDto.getSociety().getCode(), dpAsOnDate.getValue(), MainApp.locale, cboxProduct.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                List<ProductStockValuation> list = task.get();
                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (MainApp.locale.equals("en")) {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                params.put("p_as_on_date", dpAsOnDate.getValue());
                params.put("p_locale", MainApp.locale);
                params.put("p_product_code", cboxProduct.getValue().getCode());
                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_STOCK_VALUATION, params, new JRBeanCollectionDataSource(list));
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadProduct() {
        var task1 = new ProductLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Product> list = task1.get();
                if (list != null && !list.isEmpty()) {
                    listProductList = new ArrayList<>();
                    listProductList.add(new Product("0", "ALL", "બધા"));
                    listProductList.addAll(1, list);
                    cboxProduct.setItems(FXCollections.observableList(listProductList));
                    cboxProduct.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }

    public void loadData1() {
        StockValuationTaskWithSaleAndPurchase task = new StockValuationTaskWithSaleAndPurchase(MainApp.identityDto.getSociety().getCode(), dpFromDate.getValue(), dpToDate.getValue(), MainApp.locale, cboxProduct.getValue().getCode());
        task.setOnSucceeded(e -> {
            try {
                List<ProductStockValuationWithSaleAndPurchase> list = task.get();
                Map<String, Object> params = new HashMap<>();
                params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
                if (MainApp.locale.equals("en")) {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getName());
                } else {
                    params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
                }
                params.put("p_from_date", dpFromDate.getValue().toString());
                params.put("p_to_date", dpToDate.getValue().toString());
                params.put("p_locale", MainApp.locale);
                params.put("p_product_code", cboxProduct.getValue().getCode());
                JasperPrint print = ReportGenerate.getReportDataSourceViewer(AppConstant.ReportPath.RPT_STOCK_VALUATION_WITH_SALE_PURCHASE, params, new JRBeanCollectionDataSource(list));
                JasperViewer.viewReport(print, false);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

    }
}

