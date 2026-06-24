package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.CustomerLoadTask;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleDeleteTask;
import com.eipl.amcs.operation.procurement.task.LocalMilkSaleLoadTask;
import com.eipl.amcs.setting.repository.AccountPostingRepository;
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
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class LocalMilkSaleController implements MyInitialization, PopupCallback {

    private final ObjectProperty<LocalMilkSale> propLocalMilkSaleDto;
    @FXML
    private StackPane root;
    @FXML
    private DatePicker dpFromDate, dpToDate;
    @FXML
    private TableView<LocalMilkSale> tableLocalMilkSale;
    @FXML
    private TableColumn<LocalMilkSale, String> colConsumerCode, colConsumerName;
    @FXML
    private TableColumn<LocalMilkSale, LocalDate> colDate;
    @FXML
    private TableColumn<LocalMilkSale, Shift> colShift;
    @FXML
    private TableColumn<LocalMilkSale, String> colConsumerType;
    @FXML
    private TableColumn<LocalMilkSale, MilkType> colMilkType;
    @FXML
    private TableColumn<LocalMilkSale, MilkClass> colClass;
    @FXML
    private TableColumn<LocalMilkSale, BigDecimal> colQuantity, colRate, colAmount, colCouponBalance;
    @FXML
    private Button btnAdd, btnClose, btnEdit, btnDelete, btnSearch;
    private ResourceBundle resourceBundle;
    private String name;
    private List<Member> listMembers;
    private List<Customer> listCustomers;

    public LocalMilkSaleController() {
        propLocalMilkSaleDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadMember();
        loadCustomer();
        dpFromDate.setValue(LocalDate.now());
        dpFromDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFromDate.setValue(dpFromDate.getConverter().fromString(dpFromDate.getEditor().getText()));
            }
        });
        dpToDate.setValue(LocalDate.now());
        dpToDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpToDate.setValue(dpToDate.getConverter().fromString(dpToDate.getEditor().getText()));
            }
        });

        propLocalMilkSaleDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        loadData();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LOCAL_MILK_SALE_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LocalMilkSaleAddEdit", null, this, resourceBundle.getString("localmilksale"));
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LOCAL_MILK_SALE_EDIT"))
                throw new UnAuthorizedAccessException();
            LocalMilkSale dto = propLocalMilkSaleDto.get();
            editLocalMilkSale(dto);
        });
        btnSearch.setOnAction(e -> loadData());
        btnDelete.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_LOCAL_MILK_SALE_DELETE"))
                throw new UnAuthorizedAccessException();
            AccountPostingRepository accountPostingRepository = EmcsAppContext.getContext().getBean(AccountPostingRepository.class);
            LocalMilkSale dto = propLocalMilkSaleDto.get();
            if (dto != null) {
                if (accountPostingRepository.findValidRange(dto.getSaleDate().toLocalDate(), dto.getShift().getCode(), (short) 2, AppConstant.EventCode.LOCAL_MILK_SALE) > 0) {
                    MyAlert alert = new ErrorAlert(MainApp.stage, resourceBundle.getString("localmilksale"), resourceBundle.getString("account.posting.already.done"));
                    alert.createAlert();
                    return;
                }
                deleteData();
            }
        });
        FocusUtils.requestFocus(btnAdd);

        tableLocalMilkSale.setRowFactory(tv -> {
            TableRow<LocalMilkSale> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    LocalMilkSale data = row.getItem();
                    editLocalMilkSale(data);
                }
            });
            return row;
        });

        tableLocalMilkSale.setOnKeyPressed(event -> {
            LocalMilkSale dto = tableLocalMilkSale.getSelectionModel().getSelectedItem();
            if (dto == null)
                return;
            switch (event.getCode()) {
                case ENTER:
                    editLocalMilkSale(dto);
                    break;
                case DELETE:
                    deleteData();
                    break;
            }
        });
    }

    private void editLocalMilkSale(LocalMilkSale localMilkSale) {
        try {
            if (localMilkSale != null) {
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "LocalMilkSaleAddEdit", localMilkSale, this, resourceBundle.getString("localmilksale"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            LocalMilkSale dto = propLocalMilkSaleDto.get();
            if (dto != null) {
                var task = new LocalMilkSaleDeleteTask(dto);
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || !respDelete.booleanValue()) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("localmilksale"),
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

    private void loadCustomer() {
        var task = new CustomerLoadTask();
        task.setOnSucceeded(e -> {
            try {
                listCustomers = task.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupTable() {
        try {
            colConsumerType.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getCustomerTypeStrFromShort(data.getValue().getConsumerType())));
            colConsumerCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getConsumerCode().substring(MainApp.getUser().getSociety().getCode().length())));
            colConsumerName.setCellValueFactory(data -> new SimpleStringProperty(getConsumerName(data.getValue().getConsumerCode(), data.getValue().getConsumerType())));
            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSaleDate().toLocalDate()));
            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));
            colClass.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkClass()));
            colQuantity.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getQuantity()));
            colRate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRate()));
            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));
            //         colCouponBalance.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCoupon()));
            colShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShift()));
            propLocalMilkSaleDto.bind(tableLocalMilkSale.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableLocalMilkSale);
        } catch (Exception e) {
            System.out.println("LocalMilkSake setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        tableLocalMilkSale.setItems(null);
        var task = new LocalMilkSaleLoadTask(dpFromDate.getValue(), dpToDate.getValue());
        task.setOnSucceeded(e -> {
            try {
                List<LocalMilkSale> list = task.get();
                if (list != null)
                    tableLocalMilkSale.setItems(FXCollections.observableList(list));
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

    private String getConsumerName(String code, Short consumerType) {
        if (consumerType < (short) 3 && code != null) {
            if (listMembers != null) {
                for (Member member : listMembers) {
                    if (code.equals(member.getCode())) {
                        this.name = member.getFirstName();
                        break;
                    }
                }
            }
        } else {
            if (listCustomers != null && code != null) {
                for (Customer customer : listCustomers) {
                    if (code.equals(customer.getCode())) {
                        this.name = customer.getName();
                        break;
                    }
                }
            }
        }
        if (this.name != null) {
            return name;
        } else {
            return null;
        }
    }

}
