package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class PaymentRegisterController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    private Stage stage;
    private PopupCallback callback;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private E_TextField txtSampleNo;

    private ResourceBundle resourceBundle;

    @FXML
    private ComboBox<Shift> cboxfromshift, cboxtoshift;

    @Override
    public Node getRoot() {
        return root;
    }

    private MemberBillSummary dto = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadData();
        setupComboBox();
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        btnGenerate.setOnAction(e -> validateAndGenerateReport());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

    private void validateAndGenerateReport() {
        try {
            if (txtSampleNo.getText() == null || txtSampleNo.getText().trim().equals("")) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("paymentregister"),
                        resourceBundle.getString("sampleno.cannot.be.null"));
                alert.createAlert();
                return;
            }
            if (txtSampleNo.getText().length() > 4) {
                MyAlert alert = new ErrorAlert(MainApp.getStage(),
                        resourceBundle.getString("paymentregister"),
                        resourceBundle.getString("samplenovalidation"));
                alert.createAlert();
                return;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Map<String, Object> params = new HashMap<>();
            JasperPrint print = null;
            params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
            params.put("p_member_code", MainApp.identityDto.getSociety().getCode() + String.format("%04d", Integer.parseInt(txtSampleNo.getText().trim())));
            params.put("p_from_date", dpFromDate.getValue().format(formatter) + " " + (cboxfromshift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
            params.put("p_to_date", dpToDate.getValue().format(formatter) + " " + (cboxtoshift.getValue().getName().equals("Morning") ? "06:00:00" : "18:00:00"));
            params.put("p_locale", MainApp.locale);
            print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.PAYMENT_REGISTER_WITH_DEDUCTION, params);
            JasperViewer.viewReport(print, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setupComboBox() {
        try {
            cboxfromshift.setConverter(new ShiftConvertor(cboxfromshift));
            cboxtoshift.setConverter(new ShiftConvertor(cboxtoshift));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void loadData() {
        var task1 = new ShiftLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<Shift> list = task1.get();
                if (list != null) {
                    cboxfromshift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxfromshift.getSelectionModel().select(0);
                    cboxtoshift.setItems(FXCollections.observableList(CommonUtils.removeAllShift(list)));
                    cboxtoshift.getSelectionModel().select(1);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();
    }
}