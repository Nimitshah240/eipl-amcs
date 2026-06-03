package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.account.model.Committee;
import com.eipl.amcs.operation.administartion.task.CommitteeDeleteTask;
import com.eipl.amcs.operation.administartion.task.CommitteeLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CommitteeController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Committee> propCommitteeDto;
    @FXML
    AnchorPane root;
    @FXML
    TableView<Committee> tableCommittee;
    @FXML
    TableColumn<Committee, String> colName, colNameLocal, colElectionDate, colFormationDate, colCommitteeMembmers;
    @FXML
    DatePicker dpDate;
    @FXML
    E_Button btnClose, btnAdd, btnDelete, btnEdit, btnRegister, btnExport;
    private ResourceBundle resourceBundle;
    private List<Committee> committeeList;

    public CommitteeController() {
        propCommitteeDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
//        dpDate.setValue(LocalDate.now());
        propCommitteeDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
        setupTable();
        loadData();
        btnAdd.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CommitteeAddEdit", null, this, resourceBundle.getString("committee"));
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnEdit.setOnAction(e -> {
            Committee dto = propCommitteeDto.get();
            if (dto != null)
                editCommittee(dto);
        });
        btnExport.setOnAction(event -> {
            exportExcel(tableCommittee.getItems());
        });
        tableCommittee.setRowFactory(tv -> {
            TableRow<Committee> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Committee data = row.getItem();
                    editCommittee(data);
                }
            });
            return row;
        });

        tableCommittee.setOnKeyPressed(event -> {
            Committee dto = tableCommittee.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case DELETE:
                    dto = propCommitteeDto.get();
                    if (dto != null)
                        deleteData();
                    break;
                case ENTER:
                    editCommittee(dto);
                    break;
            }
        });
//
//        btnRegister.setOnAction(e -> {
//            validateAndGenerateReport();
//        });
    }

    private void editCommittee(Committee committee) {
        if (committee != null)
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "CommitteeAddEdit", committee, this, resourceBundle.getString("committee"));
    }

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_date", dpDate.getValue());
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.COMMITTEE_REGISTER, params);
        JasperViewer.viewReport(print, false);
    }

    private void exportExcel(List<Committee> list) {
        if (list == null || list.isEmpty()) {
            new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committee"), "No data available to export").createAlert();
            return;
        }

        boolean exported = true;
        boolean cancelled = false;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Committees");
            fileDialog.setInitialFileName("Committees_List.xls");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.getStage());

            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Committees");

                List<String> headers = Arrays.asList("Code", "Name", "Name Local", "Election Date", "Formation Date", "Year", "Members Count");

                // Create header row
                HSSFRow headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.size(); i++) {
                    HSSFCell cell = headerRow.createCell(i);
                    cell.setCellValue(headers.get(i));
                }

                // Fill data rows
                int rowCnt = 1;
                for (Committee item : list) {
                    HSSFRow row = sheet.createRow(rowCnt++);
                    for (int i = 0; i < headers.size(); i++) {
                        HSSFCell cell = row.createCell(i);
                        switch (headers.get(i)) {
                            case "Code":
                                cell.setCellValue(item.getCode() != null ? item.getCode() : "");
                                break;
                            case "Name":
                                cell.setCellValue(item.getName() != null ? item.getName() : "");
                                break;
                            case "Name Local":
                                cell.setCellValue(item.getNameLocal() != null ? item.getNameLocal() : "");
                                break;
                            case "Election Date":
                                cell.setCellValue(item.getElectionDate() != null ? item.getElectionDate().toString() : "");
                                break;
                            case "Formation Date":
                                cell.setCellValue(item.getFormationDate() != null ? item.getFormationDate().toString() : "");
                                break;
                            case "Year":
                                cell.setCellValue(item.getYear() != null ? item.getYear() : "");
                                break;
                            case "Members Count":
                                cell.setCellValue(item.getMembers() != null ? item.getMembers().size() : 0);
                                break;
                        }
                    }
                }

                // Write to file
                try (FileOutputStream out = new FileOutputStream(file)) {
                    wb.write(out);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    exported = false;
                } finally {
                    try {
                        wb.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                cancelled = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exported = false;
        }

        showExportStatus(exported, cancelled);
    }

    private void showExportStatus(boolean exported, boolean cancelled) {
        MyAlert alert;
        String title = resourceBundle.getString("committee");
        if (cancelled) {
            alert = new InformationAlert(MainApp.getStage(), title,
                    resourceBundle.containsKey("export.cancelled") ? resourceBundle.getString("export.cancelled") : "Export Cancelled");
        } else if (exported) {
            alert = new InformationAlert(MainApp.getStage(), title,
                    resourceBundle.containsKey("successful") ? resourceBundle.getString("successful") : "Export Successful");
        } else {
            alert = new ErrorAlert(MainApp.getStage(), title,
                    resourceBundle.containsKey("error.occurred") ? resourceBundle.getString("error.occurred") : "Error occurred during export");
        }
        alert.createAlert();
    }

    @Override
    public void setupTable() {
        try {
            colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            colNameLocal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNameLocal()));
            colElectionDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getElectionDate().toString()));
            colFormationDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormationDate().toString()));
            colCommitteeMembmers.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getMembers() != null ? data.getValue().getMembers().size() : 0)));
            propCommitteeDto.bind(tableCommittee.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("CommiteMembers setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        CommitteeLoadTask task = new CommitteeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Committee> list = task.get();

                tableCommittee.setItems(FXCollections.observableList(list));

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("committee"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Committee dto = propCommitteeDto.get();
            if (dto != null) {
                var task = new CommitteeDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("committee"),
                                    resourceBundle.getString("error.occurred"));
                            alert1.createAlert();
                            return;
                        }
                        reloadData(true);
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
