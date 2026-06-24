package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.task.AllMemberDetailsLoadTask;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.task.ShareIssueLoadTask;
import com.eipl.amcs.report.util.ReportGenerate;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.eipl.amcs.utils.CommonUtils.getMemberShortCode;

public class MemberListShareController implements MyInitialization, PopupCallback {

    private static Integer NOOFSHARE = 0;
    private static BigDecimal SHAREAMOUNT = BigDecimal.ZERO;
    private final ObjectProperty<Member> propMember;
    private final Map<String, MemberDetail> mapDetails = new HashMap<>();
    public List<Member> memberList = new ArrayList<>();
    public List<Share> shareList = new ArrayList<>();
    @FXML
    AnchorPane root;
    @FXML
    Button btnClose, btnShareHolder, btnClear, btnReport;
    @FXML
    private TableView<Member> tableMember;
    @FXML
    private TextField txtCode;
    @FXML
    private TableColumn<Member, String> colCode, colFirstName, colNoOfShare, colShareAmount;
    private ResourceBundle resourceBundle;
    private Map<Object, List<Share>> shareMap = new HashMap<>();

    public MemberListShareController() {
        propMember = new SimpleObjectProperty<>();
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
        loadShare();

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnShareHolder.setOnAction(e -> {
            tableMember.setItems(FXCollections.observableList(memberList.stream().filter(e1 -> e1.getxCol1() != null && !e1.getxCol1().equalsIgnoreCase("")).collect(Collectors.toList())));

        });
        btnClear.setOnAction(e -> {
                    tableMember.setItems(FXCollections.observableList(memberList));
                }
        );
        btnReport.setOnAction(e -> validateAndGenerateReport());
        txtCode.textProperty().addListener((observable, oldValue, newValue) -> {
            search(oldValue, newValue);
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

    private void validateAndGenerateReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("p_society_code", MainApp.identityDto.getSociety().getCode());
        params.put("p_locale", MainApp.locale);
        JasperPrint print = null;
        print = ReportGenerate.getReportDataSourceJasperPrint(AppConstant.ReportPath.SHARE_MEMBER, params);
        JasperViewer.viewReport(print, false);
    }


    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(getMemberShortCode(data.getValue().getCode())));
            colFirstName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().toMemberName(MainApp.getLocale())));
            colNoOfShare.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getxCol1()));
            colShareAmount.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getxCol2()));
            propMember.bind(tableMember.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableMember);

        } catch (Exception e) {
            System.out.println("MemberListShare setuptable Exception");
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
                    for (Member member : memberList) {
                        NOOFSHARE = 0;
                        SHAREAMOUNT = BigDecimal.ZERO;
                        if (shareMap.containsKey(member.getCode())) {
                            for (Share share : shareMap.get(member.getCode())) {
                                SHAREAMOUNT = SHAREAMOUNT.add(share.getShareAmount());
                                NOOFSHARE = NOOFSHARE + share.getNoOfShare();
                            }
                            member.setxCol1(NOOFSHARE.toString());
                            member.setxCol2(SHAREAMOUNT.toString());
                        } else {
                            member.setxCol1("");
                            member.setxCol2("");
                        }
                    }
                    tableMember.setItems(FXCollections.observableList(memberList));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    public void loadShare() {
        ShareIssueLoadTask task = new ShareIssueLoadTask();
        task.setOnSucceeded(e -> {
            try {
                shareList = task.get();
                shareMap = shareList.stream().collect(Collectors.groupingBy(w -> w.getMember().getCode()));
                loadData();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
