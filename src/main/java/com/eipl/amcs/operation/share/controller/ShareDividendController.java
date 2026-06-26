package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.operation.share.task.ShareDividendListSaveTask;
import com.eipl.amcs.operation.share.task.ShareIssueLoadTask;
import com.eipl.amcs.utils.FocusUtils;
import com.eipl.amcs.utils.TableLocalizationUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ShareDividendController implements MyInitialization, PopupCallback {

    private final ObjectProperty<Share> propShareIssue;
    private final List<ShareDividend> shareDividendList = new ArrayList<>();
    ShareDividend shareDividend;
    @FXML
    private StackPane root;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private TableView<Share> tableShareDividend;
    @FXML
    private TableColumn<Share, String> colVoucherNo, colNoOfShare, colMemberCode, colMemberName, colDividend;
    @FXML
    private TableColumn<Share, LocalDate> colDate;
    @FXML
    private TableColumn<Share, BigDecimal> colAmount;
    @FXML
    private AutoSearchTextField cboxType;
    @FXML
    private TextField txtValue;
    @FXML
    private Button btnSave, btnClose, btnGenerate, btnReport, btnView;
    private ResourceBundle resourceBundle;
    private PopupCallback callback;
    private Stage stage;
    private List<Member> listMembers;
    private List<Share> shareList = new ArrayList<>();

    public ShareDividendController() {
        propShareIssue = new SimpleObjectProperty<>();
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
        FocusUtils.requestFocus(txtValue);
      //  dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
     //   dpToDate.setConverter(new LocalDateConvertor());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
        setupTable();
        loadMember();
        loadData();
        dpToDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadData();
            }
        });
        dpFromDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadData();
            }
        });
        propShareIssue.addListener((observable, oldValue, newValue) -> {

        });
        btnReport.setOnAction(e -> {
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "DividendReportPopup", propShareIssue.get(), this);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnGenerate.setOnAction(e -> {
            calculateDividend();
        });
        List<String> list = new ArrayList<>();
        list.add("Percentage");
        list.add("Rs");
        cboxType.setItems(FXCollections.observableList(list));
        cboxType.getSelectionModel().select(0);
        btnSave.setOnAction(e -> {
            setValuesInObject();
            saveData();
        });
        btnView.setOnAction(e -> {
            ShareDividendListController controller = (ShareDividendListController) MainApp.getFxmlLoaderUtil().loadAndSet(MainApp.class.getResource("view/operation/share/ListOfShareDividend.fxml"));
//            controller.setMember(null);
            MainApp.getContentPane().setCenter(controller.getRoot());
        });

    }

    private void setValuesInObject() {
        shareDividendList.clear();
        for (Share share : shareList) {
            shareDividend = new ShareDividend();
            shareDividend.setDisbursementDate(LocalDate.now());
            if (cboxType.getSelectionModel().getSelectedIndex() == 0) {
                shareDividend.setDividendValueType(0);

            } else {
                shareDividend.setDividendValueType(1);

            }
            shareDividend.setDividendValue(new BigDecimal(txtValue.getText()));
            shareDividend.setDividendAmount(new BigDecimal(share.getXcol4()));
            shareDividend.setActive(true);
            shareDividend.setShareCode(share.getShareCode());
            shareDividend.setDisbursed(false);
            shareDividend.setNoOfShare(share.getNoOfShare());
            shareDividend.setShareCode(share.getShareCode());
            shareDividend.setShareAmount(share.getShareAmount());
            shareDividend.setSociety(MainApp.identityDto.getSociety());
            shareDividend.setUnionCode(MainApp.identityDto.getUnion().getCode());
            shareDividend.setFinancialYear(MainApp.getFinancialYear());
            shareDividend.setMember(share.getMember());
            shareDividendList.add(shareDividend);
        }
        System.out.println(shareDividendList);

    }

    private void calculateDividend() {
        for (Share share : shareList) {
            if (cboxType.getSelectionModel().getSelectedIndex() == 0) {
                share.setXcol4(share.getShareAmount().multiply(new BigDecimal(txtValue.getText()).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN).toString());
            } else {
                share.setXcol4(String.valueOf(share.getNoOfShare() * Double.parseDouble(txtValue.getText())));

            }
        }
        tableShareDividend.setItems(FXCollections.observableList(shareList));
        setupTable();
        tableShareDividend.refresh();
        FocusUtils.requestFocus(btnSave);
    }

    private void loadMember() {
        var task = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                listMembers = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void setupTable() {
        try {
            colVoucherNo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCode()));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIssueDate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShareAmount()));
            colMemberCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMember().getCode()));
            colNoOfShare.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNoOfShare().toString()));
            colDividend.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getXcol4()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));
            TableLocalizationUtil.localizeTable(tableShareDividend);

        } catch (Exception e) {
            System.out.println("ShareDividend setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableShareDividend.setItems(null);
        var task = new ShareIssueLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                shareList = task.get();
                if (shareList != null) {
                    shareList = shareList.stream().filter(e1 -> !e1.getCancelled()
                            && !e1.getTransferred()).collect(Collectors.toList());
                    tableShareDividend.setItems(FXCollections.observableList(shareList));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }


    @Override
    public void saveData() {
        ShareDividendListSaveTask task = new ShareDividendListSaveTask(shareDividendList, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(resourceBundle.getString(subError.getMessage()) + "\n");
                    }

                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharedividend"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("sharedividend"),
                        resourceBundle.getString("sharedividend.insert.successful"));
                alert.createAlert();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }
}
