package com.eipl.amcs.report.accounting;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.master.account.converter.LedgerConvertor;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.task.LedgerLoadTask;
import com.eipl.amcs.master.operation.convertor.LedgerCellFactory;
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
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class RptLedgerBookSummaryController implements MyInitialization {


    @FXML
    private Button btnGenerate, btnClose;


    @FXML
    private ComboBox<Ledger> cboxLedgerName;

    @FXML
    private DatePicker dpFromDate, dpToDate;

    @FXML
    private SwingNode reportNode;

    @FXML
    private AnchorPane root;


    private ResourceBundle resourceBundle;

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

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnGenerate.setOnAction(e -> validateAndGenerateReport());
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        if (MainApp.locale.equals("en")) {
            params.put("p_society_name", MainApp.identityDto.getSociety().getName());
        } else {
            params.put("p_society_name", MainApp.identityDto.getSociety().getNameLocal() == null ? MainApp.identityDto.getSociety().getName() : MainApp.identityDto.getSociety().getNameLocal());
        }
        params.put("p_ledger_code", cboxLedgerName.getValue().getCode());
        params.put("p_from_date", java.sql.Date.valueOf(dpFromDate.getValue()));
        params.put("p_to_date", java.sql.Date.valueOf(dpToDate.getValue()));
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.RPT_LEDGER_BOOK_SUMMARY, params);
        JasperViewer.viewReport(print, false);
    }

    @Override
    public void setupComboBox() {
        cboxLedgerName.setConverter(new LedgerConvertor(cboxLedgerName));
        cboxLedgerName.setCellFactory(new LedgerCellFactory());
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
                    m.setName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxLedgerName.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxLedgerName);
                    cboxLedgerName.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();


    }
}
