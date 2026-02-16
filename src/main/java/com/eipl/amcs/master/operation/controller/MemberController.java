package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.GenderLoadTask;
import com.eipl.amcs.master.global.task.MemberTypeLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.task.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
import java.util.stream.Collectors;

import static com.eipl.amcs.utils.CommonUtils.getMemberShortCode;

public class MemberController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Member> propMember;
    private final Map<String, MemberDetail> mapDetails = new HashMap<>();
    public List<Member> memberList = new ArrayList<>();
    public PopupCallback callback;
    @FXML
    AnchorPane root;
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnImport, btnExport, btnClear;
    @FXML
    private TableView<Member> tableMember;
    @FXML
    private TextField txtCode;
    @FXML
    private Label lblStatus;
    @FXML
    private TableColumn<Member, String> colCode, colFirstName, colLocalName, colMobileNo, colIsActive, colAccountNo;
    @FXML
    private TableColumn<Member, MilkType> colMilkType;
    @FXML
    private TableColumn<Member, MemberType> colMemberType;
    private ResourceBundle resourceBundle;
    private List<Gender> genderList;
    private List<MilkType> milkTypeList;
    private List<MemberType> memberTypeList;
    private List<Bank> bankList;
    private List<Member> listMember;
    private Stage stage;
    private List<MemberDto> listMemberDto;

    public MemberController() {
        propMember = new SimpleObjectProperty<>();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadDetails();
        loadData();
        btnClose.setOnAction(e ->
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        txtCode.textProperty().addListener((observable, oldValue, newValue) -> {
            search(oldValue, newValue);
        });

        btnClear.setOnAction(e -> {
                    tableMember.setItems(FXCollections.observableList(memberList));
                    txtCode.setText("");
                }
        );

        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_ADD"))
                throw new UnAuthorizedAccessException();

            MemberAddEditController controller = (MemberAddEditController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/operation/MemberAddEdit.fxml"));
            controller.setMember(null);
            MainApp.getContentPane().setCenter(controller.getRoot());

//            TODO - FOR PASSWORD SYSTEM, DO NOT REMOVE COMMENT BELOW
//            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MemberEditPopup", null, this);
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_EDIT"))
                throw new UnAuthorizedAccessException();

            if (propMember.get() != null) {
                MemberAddEditController controller = (MemberAddEditController) MainApp.getFxmlLoaderUtil()
                        .loadAndSet(MainApp.class.getResource("view/master/operation/MemberAddEdit.fxml"));
                controller.setMember(propMember.get());
                MainApp.getContentPane().setCenter((controller).getRoot());
            }

//            TODO - FOR PASSWORD SYSTEM, DO NOT REMOVE COMMENT BELOW
//            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MemberEditPopup", propMember.get(), this);
        });
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnImport.setOnAction(e -> {
            loadImportPreReq();

        });
        btnExport.setOnAction(event -> {
            loadDetails();
            List<Member> list = listMember.stream().collect(Collectors.toList());
            exportExcel(list);
        });


        propMember.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

    }

    public void search(String oldVal, String newVal) {
        if (!newVal.equalsIgnoreCase("")) {
            tableMember.setItems(FXCollections.observableList(memberList.stream().
                    filter(
                            e1 ->
                                    e1.getCodeEx().toLowerCase().contains(newVal.toLowerCase()) ||
                                            e1.getFirstName().toLowerCase().contains(newVal.toLowerCase()) ||
                                            (e1.getMiddleName() != null && e1.getMiddleName().toLowerCase().contains(newVal.toLowerCase())) ||
                                            (e1.getLastName() != null && e1.getLastName().toLowerCase().contains(newVal.toLowerCase())) ||
                                            (e1.getFirstNameLocal() != null && e1.getFirstNameLocal().toLowerCase().contains(newVal.toLowerCase())) ||
                                            (e1.getMiddleNameLocal() != null && e1.getMiddleNameLocal().toLowerCase().contains(newVal.toLowerCase())) ||
                                            (e1.getLastNameLocal() != null && e1.getLastNameLocal().toLowerCase().contains(newVal.toLowerCase()))
                    ).collect(Collectors.toList())));
        }
    }

    private void exportExcel(List<Member> list) {
        boolean exported = true;
        boolean cancelled = false;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Members");
            fileDialog.setInitialFileName(" Members" + "-" + "List" + ".xls");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);


            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("Code", "First Name", "Middle Name", "Last Name",
                        "First name (local)", "Middle name (local)", "Last name (local)", "Member Type", "MilkType", "Mobile no", "Gender",
                        "Account No", "Bank", "Ifsc");
                List<String> strColumnTodisplay = null;
                List<String> items = null;
                strColumnTodisplay = new ArrayList<>(strColumns);
                List<String> finalResultToDisplay = strColumnTodisplay.stream().collect(Collectors.toList());
                // Create header column
                HSSFRow row = sheet.createRow(0);
                HSSFCell cell = null;
                int cellValueHeading = 0;
                for (String columnTitle : finalResultToDisplay) {
                    cell = row.createCell(cellValueHeading);
                    cell.setCellValue(columnTitle);
                    cellValueHeading++;
                }
                int rowCnt = 1;
                for (Member item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        MemberDetail dtl = mapDetails.get(item.getCode());

                        switch (columnTitle) {
                            case "Code":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getCode());
                                break;
                            case "First Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getFirstName());
                                break;
                            case "Middle Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMiddleName());
                                break;
                            case "Last Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getLastName());
                                break;
                            case "First name (local)":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getFirstNameLocal());
                                break;
                            case "Middle name (local)":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMiddleNameLocal());
                                break;
                            case "Last name (local)":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getLastNameLocal());
                                break;
                            case "Member Type":
                                cell = row.createCell(cellValueHeading++);
                                try {
                                    cell.setCellValue(String.valueOf(item.getMemberType().getName()));
                                } catch (Exception e) {
                                    System.out.println("Null");
                                    System.out.println(e);
                                }
                                break;
                            case "MilkType":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getMilkType().getName()));
                                break;
                            case "Mobile no":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getMobileNo()));
                                break;
                            case "Gender":
                                cell = row.createCell(cellValueHeading++);
                                if (dtl != null)
                                    cell.setCellValue(dtl.getGender() != null ? dtl.getGender().getName() : "");
                                break;
                            case "Account No":
                                cell = row.createCell(cellValueHeading++);
                                if (dtl != null)
                                    cell.setCellValue(dtl.getAccountNo() != null ? dtl.getAccountNo() : "");
                                break;
                            case "Bank":
                                cell = row.createCell(cellValueHeading++);
                                if (dtl != null)
                                    cell.setCellValue(dtl.getBank() != null ? dtl.getBank().getName() : "");
                                break;
                            case "Ifsc":
                                cell = row.createCell(cellValueHeading++);
                                if (dtl != null)
                                    cell.setCellValue(dtl.getIfsc() != null ? dtl.getIfsc() : "");
                                break;
                            default:
                                break;
                        }
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
            } else {
                cancelled = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            exported = false;
        }
        MyAlert alert;
        if (cancelled) {
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("member"),
                    resourceBundle.getString("export.cancelled"));
        } else if (exported) {
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("member"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("member"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    private void loadDetails() {
        var task = new AllMemberDetailsLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MemberDetail> list = task.get();
                for (MemberDetail memberDetail : list) {
                    mapDetails.put(memberDetail.getCode(), memberDetail);
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

    private void loadImportPreReq() {
        var task = new GenderLoadTask();
        task.setOnSucceeded(e -> {
            try {
                genderList = task.get();
                var task1 = new MilkTypeLoadTask();
                task1.setOnSucceeded(ew -> {
                    try {
                        milkTypeList = task1.get();
                        var task2 = new MemberTypeLoadTask();
                        task2.setOnSucceeded(eee -> {
                            try {
                                memberTypeList = task2.get();
                                var task3 = new BankLoadTask();
                                task3.setOnSucceeded(ee -> {
                                    try {
                                        bankList = task3.get();


                                        File file = CommonUtils.openExcelFileDialog(resourceBundle.getString("member"));
                                        if (file == null) {
                                            MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("member"),
                                                    resourceBundle.getString("select.file"));
                                            alert.createAlert();
                                            return;
                                        }
                                        MainApp.paneDrop.setVisible(true);
                                        startImport(file);
                                    } catch (InterruptedException | ExecutionException ex) {
                                        ex.printStackTrace();
                                    }
                                });
                                new Thread(task3).start();
                            } catch (InterruptedException | ExecutionException ex) {
                                ex.printStackTrace();
                            }
                        });
                        new Thread(task2).start();
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


    }

    private void startImport(File file) {
        var task = new MemberImportTask(file, milkTypeList, genderList, memberTypeList, bankList);
        task.setOnSucceeded(e -> {
            try {
                listMemberDto = task.get();
                if (listMemberDto == null || listMemberDto.isEmpty()) {
                    MainApp.paneDrop.setVisible(false);
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("member"),
                            resourceBundle.getString("invalid.file"));
                    alert.createAlert();
                    return;
                }
                MainApp.lblMessage.setText("Importing Members...");
                startImportProcess();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private boolean validate() {
        return true;
    }

    private void startImportProcess() {
        Set<MemberDto> set = new HashSet<>(listMemberDto);
        List<MemberDto> filteredList = new ArrayList<>(set);
        var task = new MemberListSaveTask(filteredList, true);

        task.setOnSucceeded(e -> {
            try {
                MainApp.paneDrop.setVisible(false);
                List<MemberImportDto> list = task.get();
                if (list == null || list.isEmpty()) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("member"),
                            resourceBundle.getString("error.occurred"));
                    alert.createAlert();
                    return;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("Import success: ");
                builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("success")).count());
                builder.append("\n");
                builder.append("Import fail: ");
                lblStatus.textProperty().unbind();
                lblStatus.setText("");
                builder.append(list.stream().filter(p -> p.getStatus().equalsIgnoreCase("error")).count());
                builder.append("\n");

                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        builder.toString());
                alert.createAlert();
                loadData();
                loadDetails();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        lblStatus.textProperty().bind(task.messageProperty());
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(getMemberShortCode(data.getValue().getCode())));
            colFirstName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName() + " " +
                    data.getValue().getMiddleName() + " " + data.getValue().getLastName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstNameLocal() != null ?
                    data.getValue().getFirstNameLocal() : " " +
                    data.getValue().getMiddleNameLocal() != null ? data.getValue().getMiddleNameLocal() : " " + data.getValue().getLastNameLocal()
                    != null ? data.getValue().getLastNameLocal() : ""
            ));
            colAccountNo.setCellValueFactory(data -> new SimpleStringProperty(
                    mapDetails.get(data.getValue().getCode()) != null ?
                            mapDetails.get(data.getValue().getCode()).getAccountNo() != null ? mapDetails.get(data.getValue().getCode()).getAccountNo() : "" : ""));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMobileNo()));
            colMemberType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMemberType()));
            colIsActive.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isActive() ? "Active" : "Inactive"));
            propMember.bind(tableMember.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("BillCriteria setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableMember.setItems(null);
        MemberLoadTask task = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                memberList = task.get();
                if (memberList != null) {
                    listMember = memberList;
                    tableMember.setItems(FXCollections.observableList(memberList));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Member dto = propMember.get();
            if (dto != null) {
                var task = new MemberDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("member"),
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
