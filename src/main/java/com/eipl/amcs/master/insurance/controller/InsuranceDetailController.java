package com.eipl.amcs.master.insurance.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.task.InsuranceDetailDeleteTask;
import com.eipl.amcs.master.insurance.task.InsuranceDetailFetchDeletedTask;
import com.eipl.amcs.master.insurance.task.InsuranceDetailLoadTask;
import com.eipl.amcs.master.insurance.task.InsuranceDetailSummaryLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.EncryptionUtil;
import com.eipl.amcs.utils.FocusUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InsuranceDetailController implements MyInitialization, PopupCallback {

    private final ObjectProperty<InsuranceDetail> propInsuranceDetailDto;
    //    private List<InsuranceDetail> insuranceDetailList;
    private final Map<String, InsuranceDetail> mapDetails = new HashMap<>();
    public List<InsuranceDetail> insuranceDetailList = new ArrayList<>();
    public List<Gender> genderList = new ArrayList<>();
    public InsuranceDetailSummary insuranceDetailSummary = null;
    @FXML
    TableColumn<InsuranceDetail, String> colSrNo;
    @FXML
    private StackPane root;
    @FXML
    private TableView<InsuranceDetail> tableInsuranceDetail;
    @FXML
    private TableColumn<InsuranceDetail, String> colMemberCode, colMemberName, colAdharNo, colGender, colNomineeadharno, colNomineemembername, colMemberId;
    @FXML
    private TableColumn<InsuranceDetail, LocalDate> colBirthDate, colDateOfJoiningScheme;
    @FXML
    private TableColumn<InsuranceDetail, Integer> colAge;
    @FXML
    private TextField txtMemberCode;
    @FXML
    private Button btnAdd, btnClose, btnEdit, btnDelete, btnSearch, btnExport, btnReport;
    @FXML
    private Label lblInsurance;
    private ResourceBundle resourceBundle;
    private String name;
    private InsuranceMaster insuranceMaster = null;

    public InsuranceDetailController() {
        propInsuranceDetailDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;


        txtMemberCode.textProperty().addListener((observable, oldValue, newValue) -> {
            search(oldValue, newValue);
        });

        btnSearch.setOnAction(e -> {
                    tableInsuranceDetail.setItems(FXCollections.observableList(insuranceDetailList));
                    txtMemberCode.setText("");
                }
        );
        btnExport.setOnAction(event -> {
            loadDetails();
            List<InsuranceDetail> list = insuranceDetailList.stream().collect(Collectors.toList());
            loadDeletedDetails(deletedlist -> {
                list.addAll(deletedlist);
            });
            exportExcel(list);
        });
        propInsuranceDetailDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });


        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/insurance/Insurance.fxml")));
        });
//        btnReport.setOnAction(e -> {
//            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "InsuranceReport", null, this);
//        });
        btnAdd.setOnAction(e -> {
            if (insuranceDetailSummary != null && LocalDate.now().isAfter(insuranceDetailSummary.getFromDate().minusDays(1)) && LocalDate.now().isBefore(insuranceDetailSummary.getToDate().plusDays(1))) {
                Map<String, Object> map = new HashMap<>();
                map.put("insuranceMaster", insuranceMaster);
                map.put("insuranceDetailSummary", insuranceDetailSummary);
                map.put("insuranceDetailList", insuranceDetailList);
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "InsuranceDetailAddEdit", map, this);
            } else {
                MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("insurance"),
                        resourceBundle.getString("edit.time.error"));
                alert.createAlert();
            }
        });
        btnEdit.setOnAction(e -> {
            if (insuranceDetailSummary != null
                    && LocalDate.now().isAfter(insuranceDetailSummary.getFromDate().minusDays(1))
                    && LocalDate.now().isBefore(insuranceDetailSummary.getToDate().plusDays(1))) {
                InsuranceDetail dto = propInsuranceDetailDto.get();
                if (dto != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("insuranceMaster", insuranceMaster);
                    map.put("insuranceDetailSummary", insuranceDetailSummary);
                    map.put("insuranceDetailList", insuranceDetailList);
                    map.put("insuranceDetail", dto);
                    MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "InsuranceDetailAddEdit", map, this);
                }
            } else {
                MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("insurance"),
                        resourceBundle.getString("edit.time.error"));
                alert.createAlert();
            }
        });
        btnSearch.setOnAction(e -> loadData());
        btnDelete.setOnAction(e -> {
            if (insuranceDetailSummary != null && LocalDate.now().isAfter(insuranceDetailSummary.getFromDate().minusDays(1)) && LocalDate.now().isBefore(insuranceDetailSummary.getToDate().plusDays(1))) {
                deleteData();
            } else {
                MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("insurance"),
                        resourceBundle.getString("edit.time.error"));
                alert.createAlert();
            }
        });
        FocusUtils.requestFocus(btnAdd);
//        loadData();
    }

    public void search(String oldVal, String newVal) {
        if (!newVal.equalsIgnoreCase("")) {
            tableInsuranceDetail.setItems(FXCollections.observableList(insuranceDetailList.stream().
                    filter(
                            e1 ->
                                    e1.getMemberCode().toLowerCase().contains(newVal.toLowerCase()) || e1.getMemberName().toLowerCase().contains(newVal.toLowerCase())).collect(Collectors.toList())));
        } else {
            tableInsuranceDetail.setItems(FXCollections.observableList(insuranceDetailList));
        }
    }

    public void loadRequestData() {
        if (insuranceDetailSummary.getFromDate() != null && insuranceDetailSummary.getToDate() != null) {
            if (MainApp.locale.equalsIgnoreCase("en")) {
                lblInsurance.setText("Insurance Edit Timing :  " + insuranceDetailSummary.getFromDate().format(AppConstant.DATE_FORMATTER) + " - " + insuranceDetailSummary.getToDate().format(AppConstant.DATE_FORMATTER) + "  (" + "Total Member" + " : " + insuranceDetailList.size() + " )");
            } else {
                lblInsurance.setText("વીમા યાદી સુધારણા નો સમય :  " + insuranceDetailSummary.getFromDate().format(AppConstant.DATE_FORMATTER) + " - " + insuranceDetailSummary.getToDate().format(AppConstant.DATE_FORMATTER) + "  (" + "  કુલ સભ્ય" + " : " + insuranceDetailList.size() + " )");
            }
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("InsuranceDetail"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            InsuranceDetail dto = propInsuranceDetailDto.get();
            if (dto != null) {
                var task = new InsuranceDetailDeleteTask(dto);
                task.setOnSucceeded(e -> {
                    try {
//                        Boolean respDelete = task.get();
//                        if (respDelete == null || respDelete.booleanValue() == false) {
//                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("InsuranceDetail"),
//                                    resourceBundle.getString("error.occurred"));
//                            alert1.createAlert();
//                            return;
//                        }
                        loadData();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task).start();
            }
        }
    }

    private void exportExcel(List<InsuranceDetail> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export InsuranceDetail");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);


            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("Member Code", "Member Name",
                        "Aadhar No", "Birth Date", "Age", "Gender Code", "Nominee Aadhar No", "Nominee Member Name", "Joining Date");
                List<String> strColumnTodisplay = null;
                CellStyle style;
                List<String> items = null;
                strColumnTodisplay = new ArrayList<>(strColumns);
                DataFormat format = wb.createDataFormat();
                style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.00"));
                List<String> finalResultToDisplay = strColumnTodisplay.stream().collect(Collectors.toList());
                // Create header column
                HSSFRow row = sheet.createRow(1);
                HSSFCell cell = row.createCell(0);
                cell.setCellValue("Code: ");
                cell = row.createCell(1);
                cell.setCellValue(MainApp.identityDto.getSociety().getCode());
                row = sheet.createRow(2);
                cell = row.createCell(0);
                cell.setCellValue("Name: ");
                cell = row.createCell(1);
                cell.setCellValue(MainApp.identityDto.getSociety().getName());
                row = sheet.createRow(3);
                cell = row.createCell(0);
                cell.setCellValue("Insurance Description: ");
                cell = row.createCell(1);
                cell.setCellValue(insuranceMaster.getInsuranceDescription());
                sheet.autoSizeColumn(0);
//                HSSFCell cell = null;
                row = sheet.createRow(5);
                int cellValueHeading = 0;
                for (String columnTitle : finalResultToDisplay) {
                    cell = row.createCell(cellValueHeading);
                    cell.setCellValue(columnTitle);
                    cellValueHeading++;
                }
                int rowCnt = 6;
                boolean deletedFlag = false;
                for (InsuranceDetail item : list) {

                    if (item.getDelete() && !deletedFlag) {

                        deletedFlag = true;
                        rowCnt += 2;
                        row = sheet.createRow(rowCnt);
                        cell = row.createCell(0);
                        cell.setCellValue("Deleted Insurance Detail(s)");
                        sheet.addMergedRegion(new CellRangeAddress(rowCnt, rowCnt, 0, 8));
                        Font font = wb.createFont();
                        font.setBold(true);
                        font.setFontHeightInPoints((short) 15);
                        font.setColor(IndexedColors.RED.getIndex());
                        CellStyle deletedDetailHeaderStyle = wb.createCellStyle();
                        deletedDetailHeaderStyle.setFont(font);
                        deletedDetailHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
                        deletedDetailHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        cell.setCellStyle(deletedDetailHeaderStyle);

                        rowCnt += 2;
                    }

                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        InsuranceDetail dtl = mapDetails.get(item.getInsuranceDetailCode());

                        switch (columnTitle) {
//                            case "Insurance Description":
//                                cell = row.createCell(cellValueHeading++);
//                                cell.setCellValue(insuranceMaster.getInsuranceDescription());
//                                break;
                            case "Member Code":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMemberCode());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Member Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMemberName());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Aadhar No":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(EncryptionUtil.decrypt(item.getAdharNo()));
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Birth Date":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(EncryptionUtil.decrypt(item.getDob()));
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Age":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getAge());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Nominee Aadhar No":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(EncryptionUtil.decrypt(item.getNomineeAdharNo()));
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Nominee Member Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getNomineeMemberName());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Gender Code":
                                cell = row.createCell(cellValueHeading++);
                                if (dtl != null)
                                    cell.setCellValue(dtl.getGenderCode() != null && !dtl.getGenderCode().equalsIgnoreCase("null") ? genderList.stream().filter(e -> e.getCode() == Integer.parseInt(dtl.getGenderCode())).findFirst().get().getName() : "");
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Joining Date":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getDateOfJoiningScheme()));
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            default:
                                break;
                        }
                        sheet.autoSizeColumn(0);
                        sheet.autoSizeColumn(1);
                        sheet.autoSizeColumn(2);
                        sheet.autoSizeColumn(3);
                        sheet.autoSizeColumn(4);
                        sheet.autoSizeColumn(5);
                        sheet.autoSizeColumn(6);
                        sheet.autoSizeColumn(7);
                        sheet.autoSizeColumn(8);
                    }
                    rowCnt++;
                }
                try {
                    wb.close();
                } catch (IOException e1) {
                    exported = false;
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    wb.write(out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    exported = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exported = false;
        }
        MyAlert alert;
        if (exported) {
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("insurance"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("insurance"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    private void loadDetails() {
        var task = new InsuranceDetailLoadTask(insuranceMaster.getInsuranceMasterCode());
        task.setOnSucceeded(e -> {
            try {
                List<InsuranceDetail> list = task.get();
                for (InsuranceDetail insuranceDetail : list) {
                    mapDetails.put(insuranceDetail.getInsuranceDetailCode(), insuranceDetail);
                }
                setupTable();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        try {
            colAdharNo.setCellValueFactory(data -> {
                String decryptedAdhar = EncryptionUtil.decrypt(data.getValue().getAdharNo());
                String maskedAdhar = maskAadharNumber(decryptedAdhar);
                return new SimpleStringProperty(maskedAdhar);
            });

            colBirthDate.setCellValueFactory(data -> {
                String decryptedDob = EncryptionUtil.decrypt(data.getValue().getDob());
                LocalDate parsedDate = AppConstant.parseDateWithMultipleFormats(decryptedDob);
                return new SimpleObjectProperty<>(parsedDate);
            });
            colBirthDate.setCellFactory(new LocalDateCellFactory<>());

            colGender.setCellValueFactory(data -> new SimpleObjectProperty<>(
                    data.getValue().getGenderCode() != null && !data.getValue().getGenderCode().equalsIgnoreCase("null")
                            ? genderList.stream()
                            .filter(e -> e.getCode() == Integer.parseInt(data.getValue().getGenderCode()))
                            .findFirst()
                            .map(e -> e.getName())
                            .orElse("")
                            : ""
            ));

            colMemberCode.setCellValueFactory(data -> {
                String memberCode = data.getValue().getMemberCode();
                if (memberCode.length() < 4) {
                    memberCode = String.format("%04d", Integer.parseInt(memberCode));
                } else {
                    memberCode = memberCode.substring(memberCode.length() - 4);
                }
                return new SimpleObjectProperty<>(memberCode);
            });

            colMemberName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMemberName()));
            colMemberId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMemberId()));
//            colSrNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSrNo()));
            colSrNo.setCellFactory(column -> new TableCell<InsuranceDetail, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getIndex() >= tableInsuranceDetail.getItems().size()) {
                        setText(null);
                    } else {
                        setText(String.valueOf(getIndex() + 1));
                    }
                }
            });
            colNomineeadharno.setCellValueFactory(data -> {
                String decryptedNomineeAdhar = EncryptionUtil.decrypt(data.getValue().getNomineeAdharNo());
                String maskedNomineeAdhar = maskAadharNumber(decryptedNomineeAdhar);
                return new SimpleStringProperty(maskedNomineeAdhar);
            });

            colNomineemembername.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNomineeMemberName()));
//            colDateOfJoiningScheme.setCellValueFactory(data ->
//                    new SimpleObjectProperty<>(data.getValue().getDateOfJoiningScheme().format(AppConstant.DATE_FORMATTER))
//            );
            colDateOfJoiningScheme.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDateOfJoiningScheme().format(AppConstant.DATE_FORMATTER)));

// Update Age Base On InsuranceMaster StartDate

//            colAge.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAge()));
//            colAge.setCellFactory(column -> new TableCell<InsuranceDetail, Integer>() {
//                @Override
//                protected void updateItem(Integer age, boolean empty) {
//                    super.updateItem(age, empty);
//                    if (empty || age == null) {
//                        setText(null);
//                        setStyle("");
//                    } else {
//                        setText(age.toString());
//
//                        int minAge = insuranceMaster.getMemberMinAge();
//                        int maxAge = insuranceMaster.getMemberMaxAge();
//
//                        if (age < minAge || age > maxAge) {
//                            setStyle("-fx-text-fill: red;");
//                        } else {
//                            setStyle("-fx-text-fill: black;");
//                        }
//                    }
//                }
//            });


            colAge.setCellValueFactory(data -> {
                String decryptedDob = EncryptionUtil.decrypt(data.getValue().getDob());
                LocalDate dob = AppConstant.parseDateWithMultipleFormats(decryptedDob);
                if (dob != null && insuranceMaster.getInsuranceStartDate() != null) {
                    int age = Period.between(dob, insuranceMaster.getInsuranceStartDate()).getYears();
                    data.getValue().setAge(age);
                    return new SimpleObjectProperty<>(age);
                } else {
                    return new SimpleObjectProperty<>(null);
                }
            });

            colAge.setCellFactory(column -> new TableCell<InsuranceDetail, Integer>() {
                @Override
                protected void updateItem(Integer age, boolean empty) {
                    super.updateItem(age, empty);
                    if (empty || age == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(age.toString());

                        int minAge = insuranceMaster.getMemberMinAge();
                        int maxAge = insuranceMaster.getMemberMaxAge();

                        // above and equal age min and max age calculation
                        if (age <= minAge || age >= maxAge) {
                            setStyle("-fx-text-fill: red;");
                        } else {
                            setStyle("-fx-text-fill: black;");
                        }
                    }
                }
            });
            tableInsuranceDetail.setRowFactory(tv -> new TableRow<InsuranceDetail>() {
                @Override
                protected void updateItem(InsuranceDetail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("");
                    } else {
//                        int age = item.getAge();
                        int age = Period.between(LocalDate.parse(EncryptionUtil.decrypt(item.getDob())), insuranceMaster.getInsuranceStartDate()).getYears();
                        int minAge = insuranceMaster.getMemberMinAge();
                        int maxAge = insuranceMaster.getMemberMaxAge();

//                        if (age < minAge || age > maxAge) {
////                            setStyle("-fx-background-color: #ffcccc;");
//                            setStyle("-fx-background-color: #ffcccc; -fx-text-fill: black;");
//                        } else {
//                            setStyle("");
//                        }

                        // above and equal age min and max age calculation
                        if (age <= minAge || age >= maxAge) {
                            if (!isSelected()) {
                                setStyle("-fx-background-color: #ffcccc; -fx-text-fill: black;");
                            } else {
                                setStyle("");
                            }

                            selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                                if (isNowSelected) {
                                    setStyle("");
                                } else {
                                    setStyle("-fx-background-color: #ffcccc; -fx-text-fill: black;");
                                }
                            });

                        } else {
                            setStyle("");
                        }

                    }
                }
            });

            propInsuranceDetailDto.bind(tableInsuranceDetail.getSelectionModel().selectedItemProperty());

        } catch (Exception e) {
            System.out.println("Insurance setuptable Exception");
            e.printStackTrace();
        }
    }

    private String maskAadharNumber(String adharNo) {
        if (adharNo == null || adharNo.length() < 4) {
            return "xxxx-xxxx-****";
        }
        return "xxxx-xxxx-" + adharNo.substring(adharNo.length() - 4);
    }

    @Override
    public void loadData() {
        tableInsuranceDetail.setItems(null);
        InsuranceDetailLoadTask task = new InsuranceDetailLoadTask(insuranceMaster.getInsuranceMasterCode());
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    insuranceDetailList = task.get();
                    tableInsuranceDetail.setItems(FXCollections.observableList(insuranceDetailList));
                }
                InsuranceDetailSummaryLoadTask task1 = new InsuranceDetailSummaryLoadTask(insuranceMaster.getInsuranceMasterCode());
                task1.setOnSucceeded(e1 -> {
                    try {
                        insuranceDetailSummary = task1.get();
                        loadRequestData();
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                    }
                });
                new Thread(task1).start();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();


        GenderLoadTask task2 = new GenderLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                genderList = task2.get();
                setupTable();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    public void setInsuranceMaster(InsuranceMaster insuranceMaster) {
        this.insuranceMaster = insuranceMaster;
        loadData();
    }

    @Override
    public void reloadData(boolean flag) {
        loadData();
    }


    private void loadDeletedDetails(Consumer<List<InsuranceDetail>> callback) {
        var task = new InsuranceDetailFetchDeletedTask(insuranceMaster.getInsuranceMasterCode());
        task.setOnSucceeded(e -> {
            try {
                List<InsuranceDetail> list = task.get();
                callback.accept(list);

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
