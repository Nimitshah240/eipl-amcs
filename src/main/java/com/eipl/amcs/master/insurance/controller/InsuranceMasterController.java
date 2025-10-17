package com.eipl.amcs.master.insurance.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.alert.*;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.model.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.model.InsuranceMaster;
import com.eipl.amcs.master.insurance.task.*;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.EncryptionUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InsuranceMasterController implements MyInitialization, PopupCallback {

    private final ObjectProperty<InsuranceMaster> propInsuranceMasterDto;
    private final StringBuilder errorMsg = null;
    private final Map<String, InsuranceDetail> mapDetails = new HashMap<>();
    @FXML
    private StackPane root;
    @FXML
    private TableView<InsuranceMaster> tableInsuranceMaster;
    @FXML
    private TableColumn<InsuranceMaster, String> colInsuranceDescription;
    @FXML
    private TableColumn<InsuranceMaster, LocalDate> colInsuranceEndDate, colInsuranceStartDate, colDcsEditEndDate;
    @FXML
    private TableColumn<InsuranceMaster, Integer> colInsuranceMasterCode, colMinAge, colMaxAge;
    @FXML
    private E_Button btnView, btnFinalize, btnExport;
    private ResourceBundle resourceBundle;
    private String name;
    private List<InsuranceMaster> insuranceMasterList;
    private List<InsuranceDetail> insuranceDetailList;
    private InsuranceDetailSummary insuranceDetailSummary;
    private InsuranceMaster insuranceMaster;
    private List<Member> members;

    public InsuranceMasterController() {
        propInsuranceMasterDto = new SimpleObjectProperty<>();
    }

    public static int calculateAge(LocalDate birthDate) {
        return (birthDate != null) ? Period.between(birthDate, LocalDate.now()).getYears() : 0;
    }

    @Override
    public Node getRoot() {
        return root;
    }

    /**
     * @updatedBy Nimit Shah
     * @updatedOn - 09-09-2025
     * @update - added try catch and btnExport to export excel of insurance detail.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadMember();
            loadData();
            this.resourceBundle = resourceBundle;

            propInsuranceMasterDto.addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    loadDetails(newVal);
                    loadInsuranceDetailSummary(newVal.getInsuranceMasterCode());
                }
            });

            btnView.setOnAction(e -> {
                if (propInsuranceMasterDto.get() != null) {
                    InsuranceDetailController controller = (InsuranceDetailController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/master/insurance/InsuranceDetail.fxml"));
                    controller.setInsuranceMaster(propInsuranceMasterDto.get());
                    MainApp.getContentPane().setCenter(controller.getRoot());
                }
            });

            btnFinalize.setOnAction(e -> {
                MyAlert alert = new ConfirmationAlert(MainApp.getStage(), CommonUtils.getResourceString(resourceBundle, "insurance"),
                        CommonUtils.getResourceString(resourceBundle, "insurance.bill.finalize.confirmation"));
                Optional<ButtonType> resp = alert.createConfirmationAlert();
                if (resp.isPresent() && resp.get() == ButtonType.OK) {
                    finalizeInsurance();
                }
            });

            btnExport.setOnAction(event -> {
                if (propInsuranceMasterDto.get() != null) {
                    insuranceMaster = propInsuranceMasterDto.get();
                    MyAlert alert = null;
                    List<InsuranceDetail> list;
                    if (insuranceDetailList != null && !insuranceDetailList.isEmpty()) {
                        list = insuranceDetailList.stream()
                                .filter(detail -> insuranceMaster.getInsuranceMasterCode().equals(detail.getInsuranceMasterCode()))
                                .collect(Collectors.toList());

                        loadDeletedDetails(deletedlist -> {
                            list.addAll(deletedlist);
                        });
                    } else {
                        list = null;
                        alert = new InformationAlert(MainApp.stage,
                                resourceBundle.getString("insurance"),
                                resourceBundle.getString("no.data"));
                        alert.createAlert();
                    }
                    if (list == null && list.isEmpty()) {
                        alert = new InformationAlert(MainApp.stage,
                                resourceBundle.getString("insurance"),
                                resourceBundle.getString("no.data"));
                        alert.createAlert();
                    } else
                        exportExcel(list);
                }
            });
            setupTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void finalizeInsurance() {
        System.out.println("Finalizing insurance triggered...");
        StringBuilder errorMsg = new StringBuilder();

        if (propInsuranceMasterDto.get() == null) {
            MyAlert alert = new WarningAlert(MainApp.getStage(),
                    resourceBundle.getString("insurance.master.title"),
                    resourceBundle.getString("insurance.master.null"));
            alert.createAlert();
            return;
        }

        if (insuranceDetailList == null || insuranceDetailList.isEmpty()) {
            MyAlert alert = new WarningAlert(MainApp.getStage(),
                    resourceBundle.getString("insurance.details.title"),
                    resourceBundle.getString("insurance.details.null"));
            alert.createAlert();
            return;
        } else {
            Optional<InsuranceDetail> partialFinalize = insuranceDetailList.stream()
                    .filter(detail -> "PARTIAL_FINALIZE".equalsIgnoreCase(detail.getStatus()))
                    .findAny();
            if (partialFinalize.isPresent()) {
                MyAlert alert = new WarningAlert(MainApp.getStage(),
                        resourceBundle.getString("insurance.status.title"),
                        resourceBundle.getString("insurance.already.finalized"));
                alert.createAlert();
                return;
            }
        }

        if (members == null || members.isEmpty()) {
            MyAlert alert = new WarningAlert(MainApp.getStage(),
                    resourceBundle.getString("members.title"),
                    resourceBundle.getString("members.not.available"));
            alert.createAlert();
            return;
        }

        InsuranceMaster selectedMaster = propInsuranceMasterDto.get();
        int minAge = selectedMaster.getMemberMinAge() != null ? selectedMaster.getMemberMinAge() : 0;
        int maxAge = selectedMaster.getMemberMaxAge() != null ? selectedMaster.getMemberMaxAge() : 999;

        Set<String> validMemberCodes = members.stream()
                .map(m -> m.getCode().substring(m.getCode().length() - 4))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        StringBuilder invalidMembers = new StringBuilder();
        StringBuilder invalidAgeMembers = new StringBuilder();
        StringBuilder invalidNameMembers = new StringBuilder();
        Pattern namePattern = Pattern.compile("^[A-Za-z ]+$");

        Pattern aadharNumberPattern = Pattern.compile("^\\d{12}$");
        List<String> aadhaarList = new ArrayList<>();

        for (InsuranceDetail detail : insuranceDetailList) {
            String rawMemberCode = detail.getMemberCode();
            if (rawMemberCode == null || !rawMemberCode.matches("\\d+")) {
                invalidMembers.append("• ").append(detail.getMemberName() != null ? detail.getMemberName() : resourceBundle.getString("member.unknown"))
                        .append(" (").append(resourceBundle.getString("code.invalid")).append(": ").append(rawMemberCode).append(")\n");
                continue;
            }

            String memberName = detail.getMemberName();
            String nomineeName = detail.getNomineeMemberName();

            String memberAadhaarNumber = EncryptionUtil.decrypt(detail.getAdharNo());
            String nomineeAadhaarNumber = EncryptionUtil.decrypt(detail.getNomineeAdharNo());

            if (memberAadhaarNumber == null || memberAadhaarNumber.isEmpty()) {
                invalidNameMembers.append("• ").append(detail.getMemberCode()).append(" - ").append(resourceBundle.getString("member.aadhaar.empty")).append("\n");
            } else if (!aadharNumberPattern.matcher(memberAadhaarNumber).matches()) {
                invalidNameMembers.append("• ").append(detail.getMemberCode()).append(" - ").append(resourceBundle.getString("member.aadhaar.invalid")).append("\n");
            }

            if (nomineeAadhaarNumber == null || nomineeAadhaarNumber.isEmpty()) {
                invalidNameMembers.append("• ").append(detail.getMemberCode()).append(" - ").append(resourceBundle.getString("nominee.aadhaar.empty")).append("\n");
            } else if (!aadharNumberPattern.matcher(nomineeAadhaarNumber).matches()) {
                invalidNameMembers.append("• ").append(detail.getMemberCode()).append(" - ").append(resourceBundle.getString("nominee.aadhaar.invalid")).append("\n");
            }

            if (aadhaarList.contains(memberAadhaarNumber)) {
                invalidNameMembers.append("• ").append(detail.getMemberCode()).append(" - ").append(resourceBundle.getString("member.duplicate.aadhaar")).append("\n");
            }
//            else if (aadhaarList.contains(nomineeAadhaarNumber)) {
//                invalidNameMembers.append("• ").append(detail.getMemberCode()).append(" - ").append(resourceBundle.getString("nominee.duplicate.aadhaar")).append("\n");
//            }
            else {
//                aadhaarList.add(nomineeAadhaarNumber);
                aadhaarList.add(memberAadhaarNumber);
            }

            if (memberName == null || memberName.trim().isEmpty()) {
                invalidNameMembers.append("• ")
                        .append(detail.getMemberCode())
                        .append(" - ")
                        .append(resourceBundle.getString("member.name.empty")).append("\n");
            } else if (!namePattern.matcher(memberName.trim()).matches()) {
                invalidNameMembers.append("• ")
                        .append(detail.getMemberCode())
                        .append(" - ")
                        .append(resourceBundle.getString("member.name.invalid"))
                        .append(": ").append(memberName).append("\n");
            }
            if (nomineeName == null || nomineeName.trim().isEmpty()) {
                invalidNameMembers.append("• ")
                        .append(detail.getMemberCode())
                        .append(" - ")
                        .append(resourceBundle.getString("nominee.name.empty")).append("\n");
            } else if (!namePattern.matcher(nomineeName.trim()).matches()) {
                invalidNameMembers.append("• ")
                        .append(detail.getMemberCode())
                        .append(" - ")
                        .append(resourceBundle.getString("nominee.name.invalid"))
                        .append(": ").append(nomineeName).append("\n");
            }
            if (invalidNameMembers.length() > 0) {
                String errorMsg3 = resourceBundle.getString("invalid.members") + "\n\n" +
                        invalidNameMembers;
                MyAlert alert = new WarningAlert(MainApp.getStage(),
                        resourceBundle.getString("members.name.criteria"),
                        errorMsg3);
                alert.createAlert();
                return;
            }

            String memberCode = rawMemberCode.substring(rawMemberCode.length() - 4);
            if (!validMemberCodes.contains(memberCode)) {
                invalidMembers.append("• ").append(detail.getMemberName() != null ? detail.getMemberName() : resourceBundle.getString("member.unknown"))
                        .append(" (").append(resourceBundle.getString("code")).append(": ").append(memberCode).append(")\n");
                continue;
            }

            Integer memberAge = detail.getAge();
            if (memberAge == null && detail.getDob() != null && !detail.getDob().isEmpty()) {
                try {
                    memberAge = calculateAge(LocalDate.parse(detail.getDob()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
            }

            if (memberAge != null && (memberAge <= minAge || memberAge >= maxAge)) {
                invalidAgeMembers.append("• ").append(rawMemberCode + " ").append(detail.getMemberName())
                        .append(" (").append(resourceBundle.getString("age")).append(": ").append(memberAge).append(")\n");
            }
        }

        if (invalidMembers.length() > 0) {
            String errorMsg1 = resourceBundle.getString("invalid.members") + "\n\n" +
                    invalidMembers;
            MyAlert alert = new WarningAlert(MainApp.getStage(),
                    resourceBundle.getString("members.not.available"),
                    errorMsg1);
            alert.createAlert();
            return;
        }
        if (invalidAgeMembers.length() > 0) {
            String errorMsg2 = resourceBundle.getString("invalid.age.members") +
                    " (" + minAge + " - " + maxAge + "):\n\n" +
                    invalidAgeMembers;
            MyAlert alert = new WarningAlert(MainApp.getStage(),
                    resourceBundle.getString("members.age.criteria"),
                    errorMsg2);
            alert.createAlert();
            return;
        }

        for (InsuranceDetail detail : insuranceDetailList) {
            detail.setStatus("PARTIAL_FINALIZE");
            detail.setOriginatingOrgCode(detail.getDcsCode());
            detail.setCreatedBy(detail.getDcsCode());
            InsuranceDetailFinalizeSaveTask saveTask = new InsuranceDetailFinalizeSaveTask(detail, 1);
            saveTask.setOnFailed(e -> {
                System.err.println("Failed to save InsuranceDetail for member: " + detail.getMemberCode());
                saveTask.getException().printStackTrace();
            });
            new Thread(saveTask).start();
        }
        if (insuranceDetailSummary != null) {
            insuranceDetailSummary.setStatus("PARTIAL_FINALIZE");
            insuranceDetailSummary.setOriginatingOrgType("VLC");
            insuranceDetailSummary.setOriginatingOrgCode(insuranceDetailSummary.getDcsCode());
            insuranceDetailSummary.setCreatedBy(insuranceDetailSummary.getDcsCode());
            InsuranceDetailSummarySaveTask summaryTask = new InsuranceDetailSummarySaveTask(insuranceDetailSummary, 1);
            summaryTask.setOnFailed(e -> {
                System.err.println("Failed to save InsuranceDetailSummary.");
                summaryTask.getException().printStackTrace();
            });
            new Thread(summaryTask).start();
        }
        btnView.setDisable(true);
        btnFinalize.setDisable(true);
        MyAlert alert = new InformationAlert(MainApp.getStage(),
                resourceBundle.getString("insurance.finalized.title"),
                resourceBundle.getString("insurance.finalized.success"));
        alert.createAlert();
        System.out.println("Finalization complete.");
    }

    public void setInsuranceMaster(InsuranceMaster insuranceMaster) {
        this.propInsuranceMasterDto.set(insuranceMaster);
    }

    @Override
    public void setupTable() {
        try {
            colInsuranceEndDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getInsuranceEndDate()));
            colInsuranceEndDate.setCellFactory(new LocalDateCellFactory<>());
            colInsuranceStartDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getInsuranceStartDate()));
            colInsuranceStartDate.setCellFactory(new LocalDateCellFactory<>());
            colInsuranceDescription.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getInsuranceDescription()));
//            colStatus.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatus()));
            colMinAge.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMemberMinAge()));
            colMaxAge.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMemberMaxAge()));
            propInsuranceMasterDto.bind(tableInsuranceMaster.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("Insurance setupTable Exception");
            e.printStackTrace();
        }
    }

    private void loadMember() {
        var task2 = new MemberLoadTask();
        task2.setOnSucceeded(e -> {
            try {
                members = task2.get();
                for (Member member : members) {
                    String code = member.getCode();
                    if (code != null && code.length() >= 4) {
                        member.setCode(code.substring(code.length() - 4));
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task2).start();
    }

    /**
     * @updatedBy Nimit Shah
     * @updatedOn - 09-09-2025
     * @update - added condition to check list is null or not
     */
    private void loadDetails(InsuranceMaster master) {
        if (master == null) return;

        var task = new InsuranceDetailLoadTask(master.getInsuranceMasterCode());
        task.setOnSucceeded(e -> {
            try {
                List<InsuranceDetail> list = task.get();
                mapDetails.clear();
                if (list != null)
                    for (InsuranceDetail detail : list) {
                        if (detail.getInsuranceDetailCode() != null) {
                            mapDetails.put(detail.getInsuranceDetailCode(), detail);
                        }
                    }
                insuranceDetailList = list;
                InsuranceDetailSummaryLoadTask summaryTask = new InsuranceDetailSummaryLoadTask(master.getInsuranceMasterCode());
                summaryTask.setOnSucceeded(summaryEvent -> {
                    InsuranceDetailSummary summary = summaryTask.getValue();
                    if (summary != null && "PUBLISH".equalsIgnoreCase(summary.getStatus())) {
                        for (InsuranceDetail detail : insuranceDetailList) {
                            if ("PARTIAL_FINALIZE".equalsIgnoreCase(detail.getStatus())) {
                                detail.setStatus("PUBLISH");
                            }
                        }

                        btnView.setDisable(false);
                        btnFinalize.setDisable(false);
                    } else {
                        if (insuranceDetailList != null) {
                            Optional<InsuranceDetail> partial_finalize = insuranceDetailList.stream()
                                    .filter(d -> "PARTIAL_FINALIZE".equalsIgnoreCase(d.getStatus()))
                                    .findAny();

                            boolean isPartial = partial_finalize.isPresent();
                            btnView.setDisable(isPartial);
                            btnFinalize.setDisable(isPartial);
                        }
                    }
                });
                summaryTask.setOnFailed(summaryEvent -> {
                    System.err.println("Failed to load InsuranceDetailSummary.");
                    summaryTask.getException().printStackTrace();
                });

                new Thread(summaryTask).start();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadInsuranceDetailSummary(Integer insuranceMasterCode) {
        if (insuranceMasterCode == null) return;

        InsuranceDetailSummaryLoadTask task = new InsuranceDetailSummaryLoadTask(insuranceMasterCode);
        task.setOnSucceeded(e -> insuranceDetailSummary = task.getValue());
        task.setOnFailed(e -> {
            System.err.println("Failed to load InsuranceDetailSummary.");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @Override
    public void loadData() {
        tableInsuranceMaster.setItems(null);
        InsuranceMasterLoadTask task = new InsuranceMasterLoadTask();
        task.setOnSucceeded(e -> {
            try {
                insuranceMasterList = task.get();
                if (insuranceMasterList != null) {
                    tableInsuranceMaster.setItems(FXCollections.observableList(insuranceMasterList));
                }
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

    /**
     * This method help to get deleted insurance details.
     *
     * @param callback
     * @author Nimit Shah
     * @createdOn 09-09-2025
     */
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


    /**
     * This method help to create excel sheet of insurance details
     *
     * @param list
     * @author Nimit Shah
     * @createdOn 09-09-2025
     */
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
                List<String> strColumns = Arrays.asList("Sr No", "Member Id", "Member Code", "Member Name",
                        "Aadhar No", "Birth Date", "Age", "Nominee Aadhar No", "Nominee Member Name", "Joining Date");
                CellStyle style;
                DataFormat format = wb.createDataFormat();
                style = wb.createCellStyle();
                style.setDataFormat(format.getFormat("0.00"));
                List<String> finalResultToDisplay = strColumns.stream().collect(Collectors.toList());
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
                        switch (columnTitle) {
                            case "Sr No":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getSrNo());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
                            case "Member Id":
                                cell = row.createCell(cellValueHeading++);
                                cell.setCellValue(item.getMemberId());
                                sheet.autoSizeColumn(cellValueHeading);
                                break;
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
}