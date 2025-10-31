package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.operation.administartion.task.MeetingAgendaDeleteTask;
import com.eipl.amcs.operation.administartion.task.MeetingAgendaLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class MeetingAgendaController implements MyInitialization, PopupCallback {

    private final ObjectProperty<MeetingAgenda> propMeetingDto;
    @FXML
    StackPane root;
    @FXML
    TableView<MeetingAgenda> tableMeeting;
    @FXML
    TableColumn<MeetingAgenda, String> colCode, colSubjectLine, colMeetingTime;
    @FXML
    TableColumn<MeetingAgenda, Object> colDate, colMeetingDate, colMeetingType;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete, btnReport, btnMom;
    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;

    public MeetingAgendaController() {
        propMeetingDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        propMeetingDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                btnMom.setDisable(false);
                btnReport.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
                btnMom.setDisable(true);
                btnReport.setDisable(true);
            }
        });

        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MeetingAddEdit", null, this);
        });
        btnEdit.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MeetingAddEdit", propMeetingDto.get(), this);
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnMom.setOnAction(e -> {
            if (propMeetingDto.get() != null) {
                MomController controller = (MomController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/operation/administration/Mom.fxml"));
                controller.setMom(propMeetingDto.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            }
        });
        btnReport.setOnAction(e -> {
            validateAndGenerateReport();
        });
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_from_date", propMeetingDto.get().getDate());
        params.put("p_to_date", LocalDateTime.now());
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.MEETING_REGISTER, params);
        JasperViewer.viewReport(print, false);
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colSubjectLine.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSubjectLine()));
            colMeetingTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMeetingTime()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
            colMeetingDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMeetingDate()));
            colMeetingType.setCellValueFactory(data -> new SimpleObjectProperty<>(CommonUtils.getMeetingType(data.getValue().getMeetingType())));

            propMeetingDto.bind(tableMeeting.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("MeetingAgenda setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableMeeting.setItems(null);
        MeetingAgendaLoadTask task = new MeetingAgendaLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MeetingAgenda> list = task.get();
                if (list != null)
                    tableMeeting.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }


    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            MeetingAgenda dto = propMeetingDto.get();
            if (dto != null) {
                var task = new MeetingAgendaDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("meeting"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        loadData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }
}
