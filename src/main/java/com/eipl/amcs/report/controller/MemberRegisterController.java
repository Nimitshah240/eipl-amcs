package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.operation.task.MemberTypeLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
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

public class MemberRegisterController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    @FXML
    private SwingNode reportNode;
    @FXML
    private DatePicker dpfromDate;
    @FXML
    private ComboBox<String> cboxFormat;
    @FXML
    private ComboBox<MemberType> cboxMember;
    @FXML
    private TextField txtSocietyCode;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        cboxFormat.getItems().addAll("Format 1", "Format 2");
        setupComboBox();
        cboxFormat.getSelectionModel().select(0);
        dpfromDate.setValue(LocalDate.now());
        dpfromDate.setConverter(new LocalDateConvertor());
        dpfromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpfromDate.setValue(dpfromDate.getConverter().fromString(dpfromDate.getEditor().getText()));
            }
        });
        loadData();
        btnGenerate.setOnAction(e -> validateAndGenerate());
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        txtSocietyCode.setText(MainApp.identityDto.getSociety().getCode());
        txtSocietyCode.setDisable(true);
    }

    private void validateAndGenerate() {
        switch (cboxFormat.getSelectionModel().getSelectedIndex() + 1) {
            case 1:
                validateAndGenerateReportTwo();
                break;
            case 2:
                validateAndGenerateReport();
                break;
        }
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", dpfromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("p_member_type", cboxMember.getSelectionModel().getSelectedIndex() + 1);
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_REGISTER, params);
        JasperViewer.viewReport(print, false);
    }

    private void validateAndGenerateReportTwo() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_from_date", dpfromDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        params.put("p_member_type", cboxMember.getSelectionModel().getSelectedIndex() + 1);
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_REGISTER_ONE, params);
        JasperViewer.viewReport(print, false);
    }
//        errorMsg = new StringBuilder();
//        if (!validate()) {
//            MyAlert alert = new ErrorAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "memberregister"),
//                    errorMsg.toString());
//            alert.createAlert();
//            return;
//        }
//        var task = new MemberRegisterReportLoadTask(MainApp.identityDto.getSociety().getCode(), 1,
//                dpfromDate.getValue());
//        task.setOnSucceeded(e -> {
//            try {
//                List<MemberRegister> list = task.get();
//                if (list != null && !list.isEmpty()) {
//                    Map<String, Object> params = new HashMap<>();
//                    params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
//                    params.put("p_from_date", dpfromDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                    params.put("p_member_type", cboxMember.getSelectionModel().getSelectedIndex() + 1);
//                    JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEMBER_REGISTER, params,
//                            new JRBeanCollectionDataSource(list));
//                    JasperViewer.viewReport(print, false);
//                }
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            } catch (ExecutionException ex) {
//                ex.printStackTrace();
//            }
//        });
//        new Thread(task).start();
//    }


    private boolean validate() {
        return true;
    }

    @Override
    public void loadData() {
        MemberTypeLoadTask task = new MemberTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MemberType> list = task.get();
                if (list != null) {
                    cboxMember.setItems(FXCollections.observableList(list));
                    cboxMember.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
