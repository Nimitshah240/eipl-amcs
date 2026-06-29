package com.eipl.amcs.operation.billing.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.task.BankLoadTask;
import com.eipl.amcs.master.procurement.controller.SocietyPaymentCycleEditController;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.task.SocietyPaymentCycleLoadTask;
import com.eipl.amcs.operation.billing.dto.FinalizeDto;
import com.eipl.amcs.operation.billing.model.MemberBill;
import com.eipl.amcs.operation.billing.model.MemberBillSummary;
import com.eipl.amcs.operation.billing.task.CheckMemberBillLoadTask;
import com.eipl.amcs.operation.billing.task.MemberBillDisburseLoadTask;
import com.eipl.amcs.operation.billing.task.MemberBillFinalizeLoadTask;
import com.eipl.amcs.operation.billing.task.MemberBillLoadTask;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MemberBillController extends SocietyPaymentCycleEditController implements MyInitialization, PopupCallback {
    private final ObjectProperty<MemberBill> propMemberBill;
    @FXML
    private StackPane root;
    @FXML
    private AutoSearchTextField<SocietyPaymentCycle> cboxPaymentCycle;
    @FXML
    private AutoSearchTextField<Bank> cboxBank;
    @FXML
    private E_Button btnGenerate, btnDisburse, btnEdit, btnClose, btnFinalize, btnExport, btnReport;
    @FXML
    private E_DatePicker dpDisburseDate, dpDeductionFromDate, dpDeductionToDate;
    @FXML
    private TableView<MemberBill> tableBill;
    @FXML
    private TableColumn<MemberBill, String> colMemberCode, colMemberName, colStatus, colPaymentMode;
    @FXML
    private TableColumn<MemberBill, Number> colMilkQty, colMilkAmount, colProductSale, colLocalSale, colLoan, colOtherAdd,
            colOtherDed, colNetAmount;
    private MemberBillSummary billSummary;
    private List<MemberBill> memberBillList;
    private ResourceBundle resourceBundle;
    List<String> negativeAmountList = new ArrayList<>();

    public MemberBillController() {
        propMemberBill = new SimpleObjectProperty<>();
    }

    public void setBillSummary(MemberBillSummary billSummary) {
        this.billSummary = billSummary;
        loadData();
        if (billSummary != null && billSummary.getPaymentCycle().getLockBillingProcess()) {
            btnGenerate.setDisable(true);
            btnEdit.setDisable(true);
            btnFinalize.setDisable(true);
        }
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        dpDisburseDate.setValue(LocalDate.now());
        dpDeductionFromDate.setValue(LocalDate.now());
        dpDeductionToDate.setValue(LocalDate.now());
        memberBillList = FXCollections.emptyObservableList();
        FocusUtils.requestFocus(cboxPaymentCycle);

        setupTable();
        setupComboBox();

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/billing/MemberBillSummary.fxml"))));
        btnGenerate.setDisable(!(this.billSummary == null || billSummary.getStatus() < (short) 2));
        btnGenerate.setOnAction(e -> {
            LocalDateTime currentDate = LocalDateTime.of(LocalDate.now(), LocalTime.NOON);
            if (currentDate.isAfter(cboxPaymentCycle.getValue().getFromDate()) && currentDate.isBefore(cboxPaymentCycle.getValue().getToDate())) {
                MyAlert alert = new WarningAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                        resourceBundle.getString("billing.not.allowedfor.paymentcycle"));
                alert.createAlert();
                return;
            }

            short generate = 0;
            if (billSummary == null) {
                btnReport.setDisable(true);
                generate = 1;
            } else {
                btnReport.setDisable(false);
                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                        CommonUtils.getResourceString(resourceBundle, "member.bill.generated.confirmation"));
                Optional<ButtonType> resp = alert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    generate = 1;
                }
            }
            loadBillSummary();

            loadData(cboxPaymentCycle.getValue(), MainApp.identityDto.getSociety(), dpDeductionFromDate.getValue(), dpDeductionToDate.getValue(), generate);


        });
        btnEdit.setOnAction(e -> {
            MemberBill dto = propMemberBill.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "MemberBillTransaction", dto, this);
        });

        propMemberBill.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(memberBillList.get(0).getStatus() == 2);
            } else {
                btnEdit.setDisable(true);
            }
        });

        btnFinalize.setOnAction(e -> {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                    CommonUtils.getResourceString(resourceBundle, "member.bill.finalize.confirmation"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                finalizeMemberBill();
                btnGenerate.setDisable(true);
                btnEdit.setDisable(true);
            }
        });

        btnDisburse.setOnAction(e -> {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                    CommonUtils.getResourceString(resourceBundle, "member.bill.disburse.confirmation"));
            Optional<ButtonType> resp = alert.createConfirmationAlert();
            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                disburseMemberBill();
            }
        });

        btnExport.setOnAction(event -> {
            List<MemberBill> list = memberBillList.stream().collect(Collectors.toList());
            exportExcel(list);
        });

        btnReport.setOnAction(e -> {
            if (billSummary != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "GeneralReportPopup", billSummary, this);
            }
        });

    }

    private void exportExcel(List<MemberBill> list) {
        boolean exported = true;
        try {
            FileChooser fileDialog = new FileChooser();
            fileDialog.setTitle("Export Billing Data");
            fileDialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File(2003-2007)", "*.xls"));
            File file = fileDialog.showSaveDialog(MainApp.stage);
            if (file != null) {
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet = wb.createSheet("Sheet-1");
                List<String> strColumns = Arrays.asList("M. Code", "M. Name", "Avg. FAT", "Avg. SNF",
                        "Avg. CLR", "Qty", "Milk Amount", "LS Amount", "PS Amount",
                        "Net payable");
                List<String> strColumnTodisplay = null;
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
                for (MemberBill item : list) {
                    cellValueHeading = 0;
                    row = sheet.createRow(rowCnt);
                    for (String columnTitle : finalResultToDisplay) {
                        switch (columnTitle) {
                            case "M. Code":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMember().getCode());
                                break;
                            case "M. Name":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMember().toMemberName());
                                break;
                            case "Avg. FAT":
                                cell = row.createCell(cellValueHeading++);
                                if (item.getAvgFat() == null) {
                                    cell.setCellValue(0);
                                } else {
                                    cell.setCellValue(String.valueOf(item.getAvgFat()));
                                }
                                break;
                            case "Avg. SNF":
                                cell = row.createCell(cellValueHeading++);
                                if (item.getAvgSnf() == null) {
                                    cell.setCellValue(0);
                                } else {
                                    cell.setCellValue(String.valueOf(item.getAvgSnf()));
                                }
                                break;
                            case "Avg. CLR":
                                cell = row.createCell(cellValueHeading++);
                                if (item.getAvgClr() == null) {
                                    cell.setCellValue(0);
                                } else {
                                    cell.setCellValue(String.valueOf(item.getAvgClr()));
                                }
                                break;
                            case "Qty":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getMilkQty()));
                                break;
                            case "Milk Amount":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getMilkAmount()));
                                break;
                            case "LS Amount":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getLocalSaleAmount()));
                                break;
                            case "PS Amount":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getProductSaleAmount()));
                                break;
                            case "Net Payable":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(String.valueOf(item.getNetAmount()));
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
            alert = new InformationAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("successful"));
        } else {
            alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("error.occurred"));
        }
        alert.createAlert();
    }

    private void finalizeMemberBill() {
        boolean cashNotAllow = "0".equalsIgnoreCase(MainApp.getProperty(AppConstant.Props.ALLOW_CASHPAYMENT, "1"));
        long countCash = memberBillList.stream().filter(p -> p.getPaymnetMode() == 0).count();
        if (cashNotAllow && countCash > 0) {
            MyAlert alert = new WarningAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                    CommonUtils.getResourceString(resourceBundle, "member.bill.cashpayment.notallowed"));
            alert.createAlert();
            return;
        }
        long zeroAmountCount = memberBillList.stream().filter(p -> p.getMilkQty().doubleValue() > 0 && p.getMilkAmount().doubleValue() < 0).count();
        if (zeroAmountCount > 0) {
            MyAlert alert = new WarningAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                    CommonUtils.getResourceString(resourceBundle, "member.bill.negativeamountdisburse.notallowed"));
            alert.createAlert();
//            return;
        }

        zeroAmountCount = memberBillList.stream().filter(p -> p.getNetAmount().doubleValue() < 0).count();
        if (zeroAmountCount > 0) {
            MyAlert alert = new WarningAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                    CommonUtils.getResourceString(resourceBundle, "member.bill.negativeamountdisburse.notallowed"));
            alert.createAlert();
//            return;
        }
        FinalizeDto finalizeDto = new FinalizeDto();
        finalizeDto.setPaymentCycle(cboxPaymentCycle.getValue());
        finalizeDto.setMemberCodeList(memberBillList.stream().map(m -> m.getMember().getCode()).collect(Collectors.toList()));
        finalizeDto.setDeductionFromDate(dpDeductionFromDate.getValue());
        finalizeDto.setDeductionToDate(dpDeductionToDate.getValue());
        saveLockData(finalizeDto, 0);
    }

    private void saveLockData(FinalizeDto finalizeDto, Integer integer) {
        if (integer == 0) {
            var task = new MemberBillFinalizeLoadTask(finalizeDto);
            task.setOnSucceeded(e -> {
                try {
                    reloadData(true);
                    loadData();

                    Object list = task.get();
                    if (list != null) {
                        MyAlert alert = new InformationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                                CommonUtils.getResourceString(resourceBundle, "member.bill.finalize.successful"));
                        alert.createAlert();
                        btnFinalize.setDisable(true);
                    }

                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        } else {
            var task = new MemberBillDisburseLoadTask(finalizeDto, cboxBank.getSelectionModel().getSelectedItem());
            task.setOnSucceeded(e -> {
                try {
                    Object list = task.get();
                    if (list != null) {
                        MyAlert alert = new InformationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                                CommonUtils.getResourceString(resourceBundle, "member.bill.disburse.successful"));
                        alert.createAlert();
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            });
            new Thread(task).start();
        }
    }

    private void disburseMemberBill() {
        try {
            if (cboxPaymentCycle.getValue().getLockBillingProcess()) {
                long zeroAmountCount = memberBillList.stream().filter(p -> p.getMilkQty().doubleValue() > 0 && p.getMilkAmount().doubleValue() < 0).count();
                if (zeroAmountCount > 0) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                            CommonUtils.getResourceString(resourceBundle, "member.bill.negativeamountdisburse.notallowed"));
                    alert.createAlert();
                    return;
                }
                boolean hasPaymentModeOne = memberBillList.stream().anyMatch(p -> p.getPaymentMode() == 1);
                if (hasPaymentModeOne && cboxBank.getSelectionModel().getSelectedItem() == null) {
                    MyAlert alert = new WarningAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "member.bill"),
                            CommonUtils.getResourceString(resourceBundle, "banknullerror"));
                    alert.createAlert();
                    return;
                }
                FinalizeDto finalizeDto = new FinalizeDto();
                finalizeDto.setPaymentCycle(cboxPaymentCycle.getValue());
                finalizeDto.setMemberCodeList(memberBillList.stream().map(m -> m.getMember().getCode()).collect(Collectors.toList()));
                finalizeDto.setDeductionFromDate(dpDeductionFromDate.getValue());
                finalizeDto.setDeductionToDate(dpDeductionToDate.getValue());
                saveLockData(finalizeDto, 1);
            } else {
                MyAlert alert = new InformationAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                        resourceBundle.getString("finalize.first"));
                alert.createAlert();
            }
        } catch (Exception e) {
            MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("member.bill"),
                    resourceBundle.getString("error.occurred"));
            alert.createAlert();
        }
    }

    @Override
    public void loadData() {
        var task = new SocietyPaymentCycleLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SocietyPaymentCycle> list = task.get();
                List<SocietyPaymentCycle> paymentCycleList = new ArrayList<>();
                if (list != null) {
                    for (SocietyPaymentCycle societyPaymentCycle : list) {
//                        if (!societyPaymentCycle.getBilling())
                        paymentCycleList.add(societyPaymentCycle);
                    }

                    cboxPaymentCycle.setItems(FXCollections.observableList(paymentCycleList));

                    SocietyPaymentCycle oldCycle = null;
                    if (this.billSummary != null) {
                        SocietyPaymentCycle cycle = cboxPaymentCycle.getItems().stream()
                                .filter(p -> p.getCode().equals(this.billSummary.getPaymentCycle().getCode()))
                                .findFirst().orElse(null);
                        cboxPaymentCycle.getSelectionModel().select(cycle);
                        loadData(cboxPaymentCycle.getValue(), MainApp.identityDto.getSociety(), dpDeductionFromDate.getValue(), dpDeductionToDate.getValue(), (short) 0);
                    } else {
                        for (SocietyPaymentCycle spc : paymentCycleList) {
                            LocalDate toDate = LocalDate.from(spc.getToDate());
                            LocalDate fromDate = LocalDate.from(spc.getFromDate());
                            LocalDate currentDate = LocalDate.now();
                            if (!currentDate.isBefore(fromDate) && !currentDate.isAfter(toDate)) {
                                if (oldCycle != null)
                                    cboxPaymentCycle.setValue(oldCycle);
                                else
                                    cboxPaymentCycle.setValue(spc);
                                break;
                            }
                            oldCycle = spc;
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
        loadBank();
    }

    private void loadBank() {
        try {
            var task = new BankLoadTask();
            task.setOnSucceeded(e -> {
                try {
                    List<Bank> bankList = task.get();
                    cboxBank.setItems(FXCollections.observableList(bankList));

                    if (this.billSummary != null) {
                        Bank bank = cboxBank.getItems().stream()
                                .filter(p -> p.getCode().equals(this.billSummary.getBank().getCode()))
                                .findFirst().orElse(null);
                        cboxBank.getSelectionModel().select(bank);
                    } else {
                        cboxBank.getSelectionModel().select(0);
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            });
            new Thread(task).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void setupComboBox() {
        dpDisburseDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpDisburseDate.setValue(dpDisburseDate.getConverter().fromString(dpDisburseDate.getEditor().getText()));
            }
        });
    }

    @Override
    public void setupTable() {
        try {
            colMemberCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().getCodeEx()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            colStatus.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getPaymentStatus(data.getValue().getStatus())));
            colMilkQty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkQty()));
            colMilkAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkAmount()));
            colProductSale.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getProductSaleAmount()));
            colLocalSale.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLocalSaleAmount()));
            colLoan.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLoanAmount()));
            colOtherAdd.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getOtherAddAmount()));
            colOtherDed.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getOtherDedAmount()));
            colNetAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNetAmount()));
            colPaymentMode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaymnetMode() == (short) 0 ? "CASH" : "BANK"));
            propMemberBill.bind(tableBill.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableBill);

        } catch (Exception e) {
            System.out.println("MemberBill setuptable Exception");
            e.printStackTrace();
        }
    }

    public void loadData(SocietyPaymentCycle paymentCycle, Society society, LocalDate deductionFromDate, LocalDate
            deductionToDate, short generate) {
        tableBill.setItems(null);
        var task = new MemberBillLoadTask(paymentCycle, society, deductionFromDate, deductionToDate, generate);
        task.setOnSucceeded(e -> {
            try {
                memberBillList = task.get();
                if (memberBillList == null)
                    return;

                negativeAmountList = memberBillList.stream()
                        .filter(item -> item.getNetAmount() != null && item.getNetAmount().compareTo(BigDecimal.ZERO) < 0)
                        .map(item -> item.getMember().getCodeEx() + " - " + item.getNetAmount())
                        .collect(Collectors.toList());

                loadBillSummary();
                tableBill.setItems(FXCollections.observableList(memberBillList));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        task.setOnFailed(e -> {
            Throwable t = task.getException();
            String errorMessage = "error.occurred";
            if (t.getMessage().contains("overlapping")) {
                errorMessage = "overlapping.deduction.date";
            } else if (t.getMessage().contains("previous.bill.not.disbursed")) {
                errorMessage = "previous.bill.not.disbursed";
            }
            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("memberbill"),
                    resourceBundle.getString(errorMessage));
            alert1.createAlert();
        });
        new Thread(task).start();
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData(cboxPaymentCycle.getValue(), MainApp.identityDto.getSociety(), dpDeductionFromDate.getValue(), dpDeductionToDate.getValue(), (short) 0);
    }

    private void loadBillSummary() {
        var task = new CheckMemberBillLoadTask(cboxPaymentCycle.getValue());
        task.setOnSucceeded(exs -> {
            try {
                short generate = 0;
                MemberBillSummary memberBillSummary = task.get();
                billSummary = memberBillSummary;
                if (billSummary != null) {
                    btnReport.setDisable(false);
                    dpDeductionFromDate.setValue(memberBillSummary.getDeductionFromDate());
                    dpDeductionToDate.setValue(memberBillSummary.getDeductionToDate());
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
