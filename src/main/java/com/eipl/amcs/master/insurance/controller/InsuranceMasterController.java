package com.eipl.amcs.master.insurance.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.alert.WarningAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.insurance.dto.InsuranceDetail;
import com.eipl.amcs.master.insurance.dto.InsuranceDetailSummary;
import com.eipl.amcs.master.insurance.dto.InsuranceMaster;
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

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InsuranceMasterController implements MyInitialization, PopupCallback {

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
    private E_Button btnView, btnFinalize;

    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private final ObjectProperty<InsuranceMaster> propInsuranceMasterDto;
    private String name;
    private List<InsuranceMaster> insuranceMasterList;
    private List<InsuranceDetail> insuranceDetailList;
    private InsuranceDetailSummary insuranceDetailSummary;
    private InsuranceMaster insuranceMaster;
    private List<Member> members;
    private Map<String, InsuranceDetail> mapDetails = new HashMap<>();

    public InsuranceMasterController() {
        propInsuranceMasterDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        setupTable();
    }

    private void finalizeInsurance() {
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

//      Added on 03/06/2025 by Nimit Shah - Added for Aadhaar validations (number length 12, No special character or space and no duplicate)
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

//          Added on 03/06/2025 by Nimit Shah - Added for Aadhaar validations (number length 12, No special character or space and no duplicate)
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
                String errorMsg3 = new StringBuilder()
                        .append(resourceBundle.getString("invalid.members")).append("\n\n")
                        .append(invalidNameMembers)
                        .toString();
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
            String errorMsg1 = new StringBuilder()
                    .append(resourceBundle.getString("invalid.members")).append("\n\n")
                    .append(invalidMembers)
                    .toString();
            MyAlert alert = new WarningAlert(MainApp.getStage(),
                    resourceBundle.getString("members.not.available"),
                    errorMsg1);
            alert.createAlert();
            return;
        }
        if (invalidAgeMembers.length() > 0) {
            String errorMsg2 = new StringBuilder()
                    .append(resourceBundle.getString("invalid.age.members"))
                    .append(" (").append(minAge).append(" - ").append(maxAge).append("):\n\n")
                    .append(invalidAgeMembers)
                    .toString();
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
    }

    public static int calculateAge(LocalDate birthDate) {
        return (birthDate != null) ? Period.between(birthDate, LocalDate.now()).getYears() : 0;
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

    private void loadDetails(InsuranceMaster master) {
        if (master == null) return;

        var task = new InsuranceDetailLoadTask(master.getInsuranceMasterCode());
        task.setOnSucceeded(e -> {
            try {
                List<InsuranceDetail> list = task.get();
                mapDetails.clear();
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
                        Optional<InsuranceDetail> partial_finalize = insuranceDetailList.stream()
                                .filter(d -> "PARTIAL_FINALIZE".equalsIgnoreCase(d.getStatus()))
                                .findAny();
                        boolean isPartial = partial_finalize.isPresent();
                        btnView.setDisable(isPartial);
                        btnFinalize.setDisable(isPartial);
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
}

