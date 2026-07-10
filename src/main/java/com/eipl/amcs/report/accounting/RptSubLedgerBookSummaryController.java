package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.master.account.task.SubLedgerLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.CustomerTypeKeyValDto;
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
import java.util.stream.Collectors;

public class RptSubLedgerBookSummaryController implements MyInitialization {


    @FXML
    private Button btnGenerate, btnClose;

    @FXML
    private AutoSearchTextField<CustomerTypeKeyValDto> cboxType;

    @FXML
    private AutoSearchTextField<SubLedger> cboxSubLedgerName;
    @FXML
    private AutoSearchTextField<String> cboxLanguage;

    @FXML
    private E_DatePicker dpFromDate, dpToDate;

    @FXML
    private SwingNode reportNode;

    @FXML
    private AnchorPane root;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        loadData();
        setupComboBox();
        loadDataType();
        loadData((short) 1);

        cboxType.setOnAction(e -> {
            if (cboxType.getSelectionModel().getSelectedIndex() == 0) {
                loadData((short) cboxType.getSelectionModel().getSelectedIndex());
            } else {
                loadData((short) cboxType.getSelectionModel().getSelectedIndex());
            }
        });

        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

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

    private void loadDataType() {
        List<CustomerTypeKeyValDto> list = new ArrayList<CustomerTypeKeyValDto>();
        List<CustomerTypeKeyValDto> list2 = new ArrayList<>();
        CustomerTypeKeyValDto m = new CustomerTypeKeyValDto();
        m.setKey((short) 0);
        m.setValue("All");
        list2.add(m);
        list2.addAll(list);
        list2.addAll(CommonUtils.getAllCustomerTypes());
        cboxType.setItems(FXCollections.observableList(list2));
//        new AutoCompleteComboBoxListener<>(cboxType);
        cboxType.getSelectionModel().select(0);
    }

    private String getLocaleString() {
        return cboxLanguage.getSelectionModel().getSelectedItem().substring(0, 2).toLowerCase();
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        String localeStr = getLocaleString();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_sub_ledger_code", cboxSubLedgerName.getValue().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_Type", cboxType.getSelectionModel().getSelectedIndex());
        params.put("p_locale", localeStr);
        params.put(JRParameter.REPORT_LOCALE, new Locale(localeStr));
        if (localeStr.equalsIgnoreCase("en"))
            params.put("p_society_name", MainApp.identityDto.getSociety().getName());
        else
            params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() != null ? MainApp.identityDto.getSociety().getNameLocal() : MainApp.identityDto.getSociety().getName());


        if (cboxSubLedgerName.getSelectionModel().getSelectedIndex() == 0)
            params.put("p_sub_ledger_name", localeStr.equalsIgnoreCase("en") ? "ALL" : "બધા");
        else
            params.put("p_sub_ledger_name", cboxSubLedgerName.getValue().getName());
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RPT_SUB_LEDGER_BOOK_SUMMARY, params);

        JasperViewer.viewReport(print, false);
    }

    @Override
    public void setupComboBox() {
//        cboxSubLedgerName.setConverter(new SubLedgerConvertor(cboxSubLedgerName));
//        cboxSubLedgerName.setCellFactory(new SubLedgerCellFactory());
        cboxSubLedgerName.getSelectionModel().select(0);
    }

    public void loadData(Short type) {
        SubLedgerLoadTask task = new SubLedgerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SubLedger> list = task.get();
                if (list != null) {
                    List<SubLedger> tempList = list.stream().filter(p -> p.getType() == type).collect(Collectors.toList());
                    List<SubLedger> list2 = new ArrayList<>();
                    SubLedger m = new SubLedger();
                    m.setCode("00000000000");
                    m.setName("All");
                    list2.add(m);
                    list2.addAll(tempList);
                    cboxSubLedgerName.setItems(FXCollections.observableList(list2));
                    cboxSubLedgerName.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}

