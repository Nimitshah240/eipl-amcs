package com.eipl.amcs.operation.share.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.apierror.ApiError;
import com.eipl.amcs.exception.apierror.ApiValidationError;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.operation.share.task.ShareDividendAllDeleteTask;
import com.eipl.amcs.operation.share.task.ShareDividendListDeleteTask;
import com.eipl.amcs.operation.share.task.ShareDividendListSaveTask;
import com.eipl.amcs.operation.share.task.ShareDividendLoadTask;
import com.eipl.amcs.utils.FocusUtils;
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
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ShareDividendListController implements MyInitialization, PopupCallback {

    private final ObjectProperty<ShareDividend> propShareIssue;
    ShareDividend shareDividend;
    @FXML
    private StackPane root;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private TableView<ShareDividend> tableShareDividend;
    @FXML
    private TableColumn<ShareDividend, String> colVoucherNo, colConsumerName, colNoOfShare, colMemberCode, colMemberName, colDividend;
    @FXML
    private TableColumn<ShareDividend, LocalDate> colDate;
    @FXML
    private TableColumn<ShareDividend, BigDecimal> colAmount;
    @FXML
    private Button btnClose, btnSearch, btnDelete, btnDeleteAll;
    private ResourceBundle resourceBundle;
    private PopupCallback callback;
    private Stage stage;
    private String name;
    private List<Member> listMembers;
    private final List<Share> shareList = new ArrayList<>();
    private final List<ShareDividend> shareDividendList = new ArrayList<>();

    public ShareDividendListController() {
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
        FocusUtils.requestFocus(dpFromDate);
        dpFromDate.setConverter(new LocalDateConvertor());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setConverter(new LocalDateConvertor());
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
        setValuesInObject();
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
        propShareIssue.addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
        });
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/operation/share/ShareDividend.fxml")));
        });
        btnSearch.setOnAction(e -> {
            loadData();
        });
        btnDelete.setOnAction(e -> {
            deleteData();
        });
        btnDeleteAll.setOnAction(e -> {
            deleteAllData();
        });
        List<String> list = new ArrayList<>();
        list.add("Percentage");
        list.add("Rs");


    }

    @Override
    public void loadData() {
        tableShareDividend.setItems(null);
        ShareDividendLoadTask task = new ShareDividendLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<ShareDividend> list = task.get();
                if (list != null)
                    tableShareDividend.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("sharedividend"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            ShareDividend dto = propShareIssue.get();
            if (dto != null) {
                var task = new ShareDividendListDeleteTask(dto.getCode());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharedividend"),
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


    public void deleteAllData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("sharedividend"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {

            var task = new ShareDividendAllDeleteTask(dpFromDate.getValue(), dpToDate.getValue());
            task.setOnSucceeded(e -> {
                try {
                    Boolean respDelete = task.get();
                    if (respDelete == null || !respDelete.booleanValue()) {
                        MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("sharedividend"),
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

    private void setValuesInObject() {
        shareDividendList.clear();
        for (Share share : shareList) {
            shareDividend = new ShareDividend();
            shareDividend.setDisbursementDate(LocalDate.now());

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
//        for (Share share : shareList) {
//            if (cboxType.getSelectionModel().getSelectedIndex() == 0) {
//                share.setXcol4(share.getShareAmount().multiply(new BigDecimal(txtValue.getText()).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN).toString());
//            } else {
//                share.setXcol4(String.valueOf(share.getNoOfShare() * Double.parseDouble(txtValue.getText())));
//
//            }
//        }
//        tableShareDividend.setItems(FXCollections.observableList(shareList));
//        setupTable();
//        tableShareDividend.refresh();
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
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDisbursementDate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShareAmount()));
//        colMemberName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMemberName()));
            colMemberCode.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMember().getCode()));
            colNoOfShare.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getNoOfShare().toString()));
            colDividend.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDividendAmount().toString()));
            colMemberName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMember().toMemberName()));


            propShareIssue.bind(tableShareDividend.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("ShareDividend setuptable Exception");
            e.printStackTrace();
        }
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
