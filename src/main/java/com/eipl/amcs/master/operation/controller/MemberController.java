package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.service.GenderService;
import com.eipl.amcs.master.global.service.MemberTypeService;
import com.eipl.amcs.master.global.service.MilkTypeService;
import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.repository.MemberDetailRepository;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.master.operation.task.MemberImportTask;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.service.BankService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.AppConstant;
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
import org.apache.commons.collections4.ListUtils;
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

import static com.eipl.amcs.MainApp.context;
import static com.eipl.amcs.utils.CommonUtils.getMemberShortCode;

public class MemberController implements MyInitialization, PopupCallback {

    @FXML
    AnchorPane root;
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
    @FXML
    Button btnClose, btnAdd, btnDelete, btnEdit, btnImport, btnExport, btnSearch, btnClear, btnOk, btnClose1;
    @FXML
    private DatePicker dpFromDate;

    private ResourceBundle resourceBundle;
    private ObjectProperty<Member> propMember;

    private List<Gender> genderList;
    private List<MilkType> milkTypeList;
    private List<MemberType> memberTypeList;
    private List<Bank> bankList;
    private List<Member> listMember;
    private Map<String, MemberDetail> mapDetails = new HashMap<>();

    public List<Member> memberList = new ArrayList<>();
    private String memberCode;
    private Stage stage;
    public PopupCallback callback;
    private List<MemberDto> listMemberDto;


    private MemberService service;
    private NextCodeService nextCodeService;
    private MemberRepository repository;
    private MemberDetailRepository detailRepository;
    private GenderService genderService;
    private MilkTypeService milkTypeService;
    private BankService bankService;
    private MemberTypeService memberTypeService;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }


    public MemberController() {

        service = context.getBean(MemberService.class);
        nextCodeService = context.getBean(NextCodeService.class);
        repository = context.getBean(MemberRepository.class);
        detailRepository = context.getBean(MemberDetailRepository.class);
        genderService = context.getBean(GenderService.class);
        bankService = context.getBean(BankService.class);
        milkTypeService = context.getBean(MilkTypeService.class);
        memberTypeService = context.getBean(MemberTypeService.class);
        propMember = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        loadDetails();
        loadData();
        setupTable();
        btnClose.setOnAction(e ->
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));

        txtCode.textProperty().addListener((observable, oldValue, newValue) -> {
            search((String) oldValue, (String) newValue);
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
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_MEMBER_EDIT"))
                throw new UnAuthorizedAccessException();
            MemberAddEditController controller = (MemberAddEditController) MainApp.getFxmlLoaderUtil()
                    .loadAndSet(MainApp.class.getResource("view/master/operation/MemberAddEdit.fxml"));
            controller.setMember(propMember.get());
            MainApp.getContentPane().setCenter((controller).getRoot());
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
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Members");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            exported = false;
        }
        MyAlert alert;
        if (exported) {
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("member"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("member"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    private void loadDetails() {
        try {
            List<MemberDetail> list = service.findAllMemberDetails();
            if (!list.isEmpty()) {
                for (MemberDetail memberDetail : list) {
                    mapDetails.put(memberDetail.getCode(), memberDetail);
                }
                setupTable();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadImportPreReq() {
        try {
            genderList = genderService.findAll();
            milkTypeList = milkTypeService.findAll();
            memberTypeList = memberTypeService.findAll();
            bankList = bankService.findAll();

            File file = CommonUtils.openExcelFileDialog(resourceBundle.getString("member"));
            if (file == null) {
                MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("member"),
                        resourceBundle.getString("select.file"));
                alert.createAlert();
                return;
            }
            MainApp.paneDrop.setVisible(true);
            startImport(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    private void startImportProcess() {
        Set<MemberDto> set = new HashSet<>(listMemberDto);
        List<MemberDto> filteredList = new ArrayList<>(set);

        List<MemberImportDto> listRes = new ArrayList<>();
        List<List<MemberDto>> listTemp = ListUtils.partition(filteredList, AppConstant.MIGRATION_LIST_SIZE);
        int current = 1;
        for (List<MemberDto> memberDtos : listTemp) {
            try {
                listRes.addAll(service.importMembers(memberDtos, CommonUtil.setIdentityHeader()));
                current++;
                lblStatus.setText("Processing " + current * AppConstant.MIGRATION_LIST_SIZE + " of " + listTemp.size() * AppConstant.MIGRATION_LIST_SIZE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        MainApp.paneDrop.setVisible(false);
        if (listRes == null || listRes.isEmpty()) {
            MyAlert alert = new WarningAlert(MainApp.getStage(), resourceBundle.getString("member"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Import success: ");
        builder.append(listRes.stream().filter(p -> p.getStatus().equalsIgnoreCase("success")).count());
        builder.append("\n");
        builder.append("Import fail: ");
        lblStatus.textProperty().unbind();
        lblStatus.setText("");
        builder.append(listRes.stream().filter(p -> p.getStatus().equalsIgnoreCase("error")).count());
        builder.append("\n");

        MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                builder.toString());
        alert.createAlert();
        loadData();
        loadDetails();
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(getMemberShortCode(data.getValue().getCode())));
            colFirstName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName() + " " +
                    data.getValue().getMiddleName() + " " + data.getValue().getLastName()));
            colLocalName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstNameLocal() != null ?
                    data.getValue().getFirstNameLocal() : data.getValue().getMiddleNameLocal()
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
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            tableMember.setItems(null);
            memberList = service.findAllBySociety(MainApp.identityDto.getSociety().getCode());
            if (memberList != null) {
                listMember = memberList;
                tableMember.setItems(FXCollections.observableList(memberList));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("member"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            Member dto = propMember.get();
            if (dto != null) {
                service.delete(dto.getCode(), CommonUtil.setIdentityHeader());
                loadData();
            }
        }

    }
}
