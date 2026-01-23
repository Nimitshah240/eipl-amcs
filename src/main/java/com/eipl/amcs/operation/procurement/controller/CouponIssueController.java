package com.eipl.amcs.operation.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.global.convertor.MilkClassConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkClassLoadTask;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.CustomerByIdLoadTask;
import com.eipl.amcs.master.operation.task.MemberByIdLoadTask;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.task.SocietyLoadTask;
import com.eipl.amcs.operation.procurement.model.CouponIssue;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.task.*;
import com.eipl.amcs.utils.*;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.validation.ConstraintViolation;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class CouponIssueController implements MyInitialization {

    @FXML
    private E_TextField txtName;
    @FXML
    private E_NumericField txtCode, txtAmount, txtIssueNumber;
    @FXML
    private E_DatePicker dpDate;
    private ObservableList<CouponIssue> listCouponIssue;
    @FXML
    private ComboBox<CustomerTypeKeyValDto> cboxType;
    @FXML
    private AnchorPane panAdd;
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    @FXML
    private ComboBox<MilkClass> cboxGread;
    @FXML
    private E_Button btnCancel, btnAddSave, btnEditUpdate, btnDelete;
    @FXML
    private TableView<CouponIssue> tableIssueCoupon;
    private ObservableList<Society> listDCS;
    @FXML
    private ComboBox<Society> cboxDCS;
    @FXML
    private TableColumn<CouponIssue, String> colName, colCode, colConsumerType, colMilkGrade;
    @FXML
    private TableColumn<CouponIssue, MilkType> colMilkType;
    @FXML
    private TableColumn<CouponIssue, Society> colDcsName;
    @FXML
    private TableColumn<CouponIssue, Number> colAmount;
    @FXML
    private TableColumn<CouponIssue, LocalDate> colDate;
    private ObjectProperty<CouponIssue> propertyCouponIssue = new SimpleObjectProperty<>();

    private CouponIssue dto = null;

    private ResourceBundle resourceBundle;
    private boolean flag = true;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        btnCancel.setText(resourceBundle.getString("close"));
        txtAmount.setDisable(true);
        loadCustomerType();

        cboxType.setOnAction(event -> {
            txtCode.setText("");
            txtName.setText("");
        });

        dpDate.setValue(LocalDate.now());

        vbox.getChildren().remove(panAdd);
        FocusUtils.requestFocus(btnAddSave);
        vbox.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE)
                escapeEvent();
            if (event.getCode() == KeyCode.T && event.isAltDown())
                FocusUtils.requestFocus(tableIssueCoupon);
        });

        btnAddSave.setOnAction(event -> {
            //if (! MainApp.user.getPermissions().contains("ROLE_COUPONNISSUE_ADD"))
            //     throw new UnAuthorizedAccessException();
            if (btnAddSave.getText().equals(resourceBundle.getString("add"))) {
                getNextCouponIssue();
                loadDCS();
                clearControls();
                enableControl();

                btnCancel.setText(resourceBundle.getString("cancel"));
                if (vbox.getChildren().size() != 4)
                    vbox.getChildren().add(1, panAdd);
                btnAddSave.setText(resourceBundle.getString("save"));
                Platform.runLater(() -> {
                    if (!cboxMilkType.getItems().isEmpty()) {
                        cboxMilkType.getSelectionModel().select(0);
                    }
                    if (!cboxGread.getItems().isEmpty()) {
                        cboxGread.getSelectionModel().select(0);
                    }
                });
                FocusUtils.requestFocus(dpDate);
                btnEditUpdate.setDisable(true);
                btnDelete.setDisable(true);

                if (flag) {
//                    if (ledgerMappingEventService.checkLedgerEventMappingAccountPosting(2)) {
//                        MyAlert alert = new ConfirmationAlert(Main.primaryStage,
//                                resourceBundle.getString("couponissue.title"), resourceBundle.getString("couponissue.title"));
//                        Optional<ButtonType> res = alert.createConfirmationAlert();
//                        if (res.isPresent() && res.get() == ButtonType.OK) {
//                            Main.contentPane.setCenter(Main.loaderUtil
//                                    .load(Main.class.getResource("view/settings/LedgerMappingEvent.fxml")));
//                        }
//                    }
//                    flag = false;
                }
            } else {
                saveData();
            }
        });

        btnEditUpdate.setOnAction(event -> {

            if (btnEditUpdate.getText().equals(resourceBundle.getString("update"))) {
                try {
                    updateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            CouponIssue selectedIssue = propertyCouponIssue.get();
            if (selectedIssue == null) return;

            CouponIssueFetchLatestDateByMemberTask valTask = new CouponIssueFetchLatestDateByMemberTask(
                    selectedIssue.getConsumerCode(),
                    selectedIssue.getConsumerType(),
                    new CouponIssue()
            );

            valTask.setOnSucceeded(e -> {
                LocalDate date = valTask.getValue();
                if (date == null || selectedIssue.getIssueDate().isEqual(date)) {
                    btnAddSave.setDisable(true);
                    if (vbox.getChildren().size() != 4) {
                        vbox.getChildren().add(1, panAdd);
                    }
                    try {
                        loadControls();
                        enableControl();
                        cboxType.setDisable(true);
                        txtCode.setDisable(true);
                        cboxGread.setDisable(true);
                        cboxMilkType.setDisable(true);

                        btnCancel.setText(resourceBundle.getString("cancel"));
                        btnEditUpdate.setText(resourceBundle.getString("update"));
                        FocusUtils.requestFocus(dpDate);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    new ErrorAlert(MainApp.getStage(),
                            resourceBundle.getString("couponissue.validation.title"),
                            resourceBundle.getString("couponIssue.sale.validation.past.edit")).createAlert();
                }
            });
            valTask.setOnFailed(e -> valTask.getException().printStackTrace());
            Thread thread = new Thread(valTask);
            thread.setDaemon(true);
            thread.start();
        });


        btnDelete.setOnAction(event -> {
            CouponIssue selectedIssue = propertyCouponIssue.get();
            if (selectedIssue == null) return;

            CouponIssueFetchLatestDateByMemberTask valTask = new CouponIssueFetchLatestDateByMemberTask(
                    selectedIssue.getConsumerCode(),
                    selectedIssue.getConsumerType(),
                    new CouponIssue()
            );

            valTask.setOnSucceeded(e -> {
                LocalDate latestDate = valTask.getValue();
                if (latestDate == null || selectedIssue.getIssueDate().isEqual(latestDate)) {

                    CouponUsageTask usageTask = new CouponUsageTask(selectedIssue);
                    usageTask.setOnSucceeded(e2 -> {
                        Double[] results = usageTask.getValue();
                        double countExceptCurrent = results[0];
                        double usedCount = results[1];

                        if (countExceptCurrent < usedCount) {
                            new ErrorAlert(MainApp.getStage(),
                                    resourceBundle.getString("couponissue.validation.title"),
                                    resourceBundle.getString("couponIssue.sale.validation.coupon.used")).createAlert();
                        } else {
                            Optional<ButtonType> resp = new ConfirmationAlert(MainApp.getStage(),
                                    resourceBundle.getString("couponissue.title"),
                                    resourceBundle.getString("delete")).createConfirmationAlert();

                            if (resp.isPresent() && resp.get() == ButtonType.OK) {
                                deleteData();
                            }
                        }
                    });
                    new Thread(usageTask).start();
                } else {
                    new ErrorAlert(MainApp.getStage(),
                            resourceBundle.getString("couponissue.validation.title"),
                            resourceBundle.getString("couponIssue.sale.validation.past.edit")).createAlert();
                }
            });
            valTask.setOnFailed(ev -> valTask.getException().printStackTrace());
            Thread thread = new Thread(valTask);
            thread.setDaemon(true);
            thread.start();
        });


        btnCancel.setOnAction(event -> {
            escapeEvent();
        });

       setupTable();

       setupComboBox();


        propertyCouponIssue.addListener((observable, oldValue, newValue) -> {
            try {

                if (resourceBundle == null) return;

                btnAddSave.setText(resourceBundle.getString("add"));
                btnAddSave.setDisable(false);
                btnEditUpdate.setText(resourceBundle.getString("edit"));

                disbleControl();

                if (newValue == null) {
                    btnEditUpdate.setDisable(true);
                    btnDelete.setDisable(true);
                } else {
                    btnEditUpdate.setDisable(false);
                    btnDelete.setDisable(false);

                    if (vbox != null && vbox.getChildren().size() == 4) {
                        loadControls();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error in Selection Listener: " + e.getMessage());
                e.printStackTrace();
            }
        });


        loadData();
        loadMilkType();
        loadGrade();
        txtCode.focusedProperty().addListener((ob, oldValue, newValue) -> {
            if (!newValue && txtCode.getText().length() > 0) {
                if (cboxType.getValue().getKey() < (short) 3) {
                    String code = generateCode(txtCode.getText().trim());
                    getNameFromMemberCode(code);
                } else {
                    String code = generateCode(txtCode.getText().trim());
                    getNameFromCustomerCode(code);
                }
            }
        });
    }

    public void loadMilkType() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    cboxMilkType.setItems(FXCollections.observableList(list));
                    if (dto != null) {
                        Optional<MilkType> milkType = cboxMilkType.getItems().stream()
                                .filter(p -> p.getCode() == dto.getMilkType().getCode()).findFirst();
                        if (milkType.isPresent())
                            cboxMilkType.getSelectionModel().select(milkType.get());
                    } else {
                        cboxMilkType.getSelectionModel().select(1);
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadGrade() {
        var task = new MilkClassLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkClass> list = task.get();
                if (list != null) {
                    cboxGread.setItems(FXCollections.observableList(list));
                }
                if (dto != null) {
                    Optional<MilkClass> milkClass = cboxGread.getItems().stream()
                            .filter(p -> p.getCode() == dto.getMilkClass().getCode()).findFirst();
                    if (milkClass.isPresent())
                        cboxGread.getSelectionModel().select(milkClass.get());
                } else {
                    cboxGread.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void loadCustomerType() {
        cboxType.setItems(FXCollections.observableList(CommonUtils.getCustomerTypesForLocalMilkSale()));
        cboxType.getSelectionModel().select(0);
        if (dto != null) {
            Optional<CustomerTypeKeyValDto> dd = cboxType.getItems().stream()
                    .filter(p -> p.getKey() == dto.getConsumerType()).findFirst();
            if (dd.isPresent())
                cboxType.getSelectionModel().select(dd.get());
        } else {
            Optional<CustomerTypeKeyValDto> dd = cboxType.getItems().stream()
                    .filter(p -> p.getKey() == 0).findFirst();
            if (dd.isPresent())
                cboxType.getSelectionModel().select(dd.get());
        }
    }

    private void getNextCouponIssue() {
        CouponIssueGetNextCodeLoadTask task = new CouponIssueGetNextCodeLoadTask(
                MainApp.identityDto.getSociety());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.setOnSucceeded(e -> {
            try {
                txtIssueNumber.setText(task.get());
                txtIssueNumber.setDisable(true);
            } catch (InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void loadDCS() {
        var task = new SocietyLoadTask();
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        task.setOnSucceeded(event -> {
            try {
                listDCS = FXCollections.observableArrayList(task.get());
                cboxDCS.setItems(listDCS);
                cboxDCS.getSelectionModel().select(MainApp.identityDto.getSociety());
                cboxDCS.setDisable(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private void getNameFromMemberCode(String code) {
        var task = new MemberByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    Member member = task.get();
                    txtName.setText(member.toMemberName());
                } else {
                    txtName.clear();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("membernotfound"));
                    alert.createAlert();
                    txtName.setText("");
                    FocusUtils.requestFocus(txtName);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void getNameFromCustomerCode(String code) {
        var task = new CustomerByIdLoadTask(code);
        task.setOnSucceeded(e -> {
            try {
                if (task.get() != null) {
                    Customer customer = task.get();
                    txtName.setText(customer.getName());
                } else {
                    txtName.clear();
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("customernotfound"));
                    alert.createAlert();
                    txtCode.setText("");
                    FocusUtils.requestFocus(txtCode);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    public void enableControl() {
        txtAmount.setDisable(false);
        txtCode.setDisable(false);
        cboxGread.setDisable(false);
        cboxMilkType.setDisable(false);
        dpDate.setDisable(false);
        cboxType.setDisable(false);
    }

    private void setValuesInObject(CouponIssue couponIssue) {
        couponIssue.setUnion(MainApp.identityDto.getUnion());
        couponIssue.setSociety(MainApp.identityDto.getSociety());
        couponIssue.setAmount(Double.parseDouble(txtAmount.getText().trim().isEmpty() ? "0" : txtAmount.getText().trim()));
        couponIssue.setConsumerCode(generateCode(txtCode.getText().trim()));
        couponIssue.setConsumerType(cboxType.getSelectionModel().getSelectedIndex() + 1);
        couponIssue.setMilkClass(cboxGread.getSelectionModel().getSelectedItem());
        couponIssue.setMilkType(cboxMilkType.getSelectionModel().getSelectedItem());
        couponIssue.setCouponIssueNo(txtIssueNumber.getText().trim());
        couponIssue.setActive(true);
    }

    //    private String generateCode(String code) {
    //        return MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(code));
    //    }
    private String generateCode(String code) {
        if (code == null || code.length() < 4) {
            return MainApp.identityDto.getSociety().getCode() + String.format("%04d", CommonUtils.strToInteger(code));
        }
        String lastFour = code.substring(code.length() - 4);
        return MainApp.identityDto.getSociety().getCode() + lastFour;
    }


    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void setupTable() {
        try {
            if (tableIssueCoupon == null) return;

            colCode.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getConsumerCode() != null ?
                            data.getValue().getConsumerCode() : ""));

            colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getIssueDate()));
            colDate.setCellFactory(new LocalDateCellFactory<>());

            colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAmount()));

            colConsumerType.setCellValueFactory(data -> new SimpleStringProperty(
                    resourceBundle.getString(data.getValue().getConsumerTypeString().toLowerCase())));

            colMilkGrade.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getMilkClass() != null ?
                            data.getValue().getMilkClass().toString() : ""));

            colMilkType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getMilkType()));

            colName.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().getConsumerName() != null ?
                            data.getValue().getConsumerName() : ""));

            colDcsName.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSociety()));

            if (tableIssueCoupon.getSelectionModel() != null) {
                propertyCouponIssue.bind(tableIssueCoupon.getSelectionModel().selectedItemProperty());
            }

            MyTableUtil.initTableContextMenu(tableIssueCoupon, CouponIssueController.class.getSimpleName());
            applyColumnConfiguration();

        } catch (Exception e) {
            System.out.println("CouponIssue setupTable Exception");
            e.printStackTrace();
        }
    }

    private void applyColumnConfiguration() {
        String screenName = CouponIssueController.class.getSimpleName();
        if (MainApp.tableConfiguration != null && MainApp.tableConfiguration.containsKey(screenName)) {
            tableIssueCoupon.getColumns().clear();
            List<TableColItem> configList = MainApp.tableConfiguration.get(screenName);

            for (TableColItem item : configList) {
                TableColumn<CouponIssue, ?> col = getColumnByName(item.getColName());
                if (col != null) {
                    col.setVisible(item.isVisible());
                    tableIssueCoupon.getColumns().add(col);
                }
            }
        }
    }

    private TableColumn<CouponIssue, ?> getColumnByName(String name) {
        switch (name) {
            case "colDate": return colDate;
            case "colCode": return colCode;
            case "colConsumerType": return colConsumerType;
            case "colName": return colName;
            case "colMilkType": return colMilkType;
            case "colMilkGrade": return colMilkGrade;
            case "colAmount": return colAmount;
            case "colDcsName": return colDcsName;
            default: return null;
        }
    }


    @Override
    public void setupComboBox() {
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxGread.setConverter(new MilkClassConvertor(cboxGread));
        dpDate.setConverter(new LocalDateConvertor());
    }

    @Override
    public void loadData() {
//        if (!Main.user.getRoles().contains("ROLE_COUPONNISSUE_LIST"))
//            throw new UnAuthorizedAccessException();

        CouponIssueLoadTask stateTask = new CouponIssueLoadTask();
        Thread thread = new Thread(stateTask);
        thread.setDaemon(true);
        thread.start();
        if (listCouponIssue != null)
            listCouponIssue.clear();
        tableIssueCoupon.setPlaceholder(new Label("Fetching Data..."));
        stateTask.setOnSucceeded(event -> {
            try {
                if (listCouponIssue == null)
                    listCouponIssue = FXCollections.observableArrayList(stateTask.get());
                else {
                    listCouponIssue.clear();
                    listCouponIssue.addAll(stateTask.get());
                }
                tableIssueCoupon.setItems(listCouponIssue);
                //TableFilter.forTableView(tableIssueCoupon).apply();
                if (listCouponIssue != null && listCouponIssue.isEmpty())
                    tableIssueCoupon.setPlaceholder(new Label("No Data Available."));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void clearControls() {
        txtAmount.clear();
        txtCode.clear();
        txtName.clear();
        txtIssueNumber.clear();
        dpDate.setValue(null);
        cboxMilkType.valueProperty().set(null);
        cboxGread.valueProperty().set(null);
    }

    public void disbleControl() {
        txtAmount.setDisable(true);
        txtCode.setDisable(true);
        cboxGread.setDisable(true);
        cboxMilkType.setDisable(true);
        dpDate.setDisable(true);
        cboxType.setDisable(true);
    }

    @Override
    public void loadControls() throws ExecutionException, InterruptedException {
        CouponIssue couponIssue = propertyCouponIssue.get();
        if (couponIssue != null) {
            cboxDCS.getSelectionModel().select(couponIssue.getSociety());
            cboxDCS.setDisable(true);
            Platform.runLater(() -> {
                if (!cboxMilkType.getItems().isEmpty()) {
                    cboxMilkType.getSelectionModel().select(couponIssue.getMilkType());
                }
                if (!cboxGread.getItems().isEmpty()) {
                    cboxGread.getSelectionModel().select(couponIssue.getMilkClass());
                }
            });
            dpDate.setValue(couponIssue.getIssueDate());
            txtIssueNumber.setText(couponIssue.getCouponIssueNo());
            txtIssueNumber.setDisable(true);
            txtAmount.setText(String.valueOf(couponIssue.getAmount()));
            Platform.runLater(() -> {
                txtCode.setText(couponIssue.getConsumerCode());
            });

            Integer type = couponIssue.getConsumerType();
            cboxType.getSelectionModel().select(type - 1);
            ConsumerNameLoadTask nameTask = new ConsumerNameLoadTask(
                    couponIssue.getConsumerType(),
                    couponIssue.getConsumerCode()
            );
            nameTask.setOnSucceeded(e -> {
                txtName.setText(nameTask.getValue());
            });
            nameTask.setOnFailed(e -> {
                txtName.setText(couponIssue.getConsumerCode());
            });
            new Thread(nameTask).start();

        }
    }


    @Override
    public void saveData() {
        CouponIssue couponIssue = new CouponIssue();
        this.setValuesInObject(couponIssue);
        couponIssue.setIssueDate(dpDate.getValue());
        couponIssue.setCreatedAt(LocalDateTime.now());
        couponIssue.setCreatedBy(MainApp.getUser().getCode());

        if (!validate(couponIssue)) {
            insert(couponIssue);
        }
    }


    private void insert(CouponIssue couponIssue) {

        CouponIssueSaveTask task = new CouponIssueSaveTask(
                couponIssue, true, MainApp.OPERATION_SOURCE, MainApp.SOURCE_RECORD_ORG_TYPE,
                MainApp.OPERATION_CREATE);

        task.setOnSucceeded(event -> {
            Boolean success = task.getValue();
            if (success) {
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                        resourceBundle.getString("alert.insert.success"));
                alert.createAlert();

                loadData();
                clearControls();
                disbleControl();

                btnAddSave.setText(resourceBundle.getString("add"));
                btnCancel.setText(resourceBundle.getString("close"));
                btnAddSave.setDisable(false);
                FocusUtils.requestFocus(btnAddSave);
                vbox.getChildren().remove(panAdd);
            } else {
                MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                        resourceBundle.getString("couponissue.alert.insert.message"));
                alert.createAlert();
            }
        });

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                    "Error: " + ex.getMessage());
            alert.createAlert();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    @Override
    public void updateData() throws ExecutionException, InterruptedException {
        CouponIssue couponIssue = propertyCouponIssue.get();
        if (couponIssue != null) {
            this.setValuesInObject(couponIssue);
            couponIssue.setUpdatedAt(LocalDateTime.now());
            couponIssue.setUpdatedBy(MainApp.getUser().getCode());

            if (!validate(couponIssue)) {
                couponIssue.setIssueDate(dpDate.getValue());
                update(couponIssue);
            }
        }
    }

    private void update(CouponIssue couponIssue) {
        CouponIssueSaveTask saveTask = new CouponIssueSaveTask(couponIssue, false,
                MainApp.OPERATION_SOURCE, MainApp.SOURCE_RECORD_ORG_TYPE, MainApp.OPERATION_UPDATE);

        saveTask.setOnSucceeded(event -> {
            if (saveTask.getValue()) {
                new InformationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                        resourceBundle.getString("alert.update.success")).createAlert();

                loadData();
                clearControls();
                disbleControl();

                btnEditUpdate.setText(resourceBundle.getString("edit"));
                btnCancel.setText(resourceBundle.getString("close"));
                btnAddSave.setDisable(false);
                FocusUtils.requestFocus(btnAddSave);
                vbox.getChildren().remove(panAdd);
            } else {
                new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                        resourceBundle.getString("couponissue.alert.update.message")).createAlert();
            }
        });

        saveTask.setOnFailed(event -> {
            saveTask.getException().printStackTrace();
            new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                    resourceBundle.getString("couponissue.alert.update.message")).createAlert();
        });

        Thread thread = new Thread(saveTask);
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public void deleteData() {
        CouponIssue couponIssue = propertyCouponIssue.get();
        if (couponIssue != null) {
            CouponIssueDeleteTask deleteTask = new CouponIssueDeleteTask(couponIssue, MainApp.OPERATION_SOURCE, MainApp.SOURCE_RECORD_ORG_TYPE, MainApp.OPERATION_DELETE);
            deleteTask.setOnSucceeded(e -> {
                boolean success = deleteTask.getValue();
                if (success) {
                    new InformationAlert(MainApp.getStage(),
                            resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("alert.delete.success")).createAlert();
                    loadData();
                    if (vbox.getChildren().size() == 4) {
                        vbox.getChildren().remove(panAdd);
                        clearControls();
                        FocusUtils.requestFocus(tableIssueCoupon);
                    }
                    btnEditUpdate.setText(resourceBundle.getString("edit"));
                    btnAddSave.setText(resourceBundle.getString("add"));
                    btnCancel.setText(resourceBundle.getString("close"));
                    btnAddSave.setDisable(false);
                } else {
                    new InformationAlert(MainApp.getStage(),
                            resourceBundle.getString("couponissue.title"),
                            resourceBundle.getString("couponissue.alert.delete.message")).createAlert();
                }
            });
            deleteTask.setOnFailed(ev -> {
                deleteTask.getException().printStackTrace();
            });
            Thread thread = new Thread(deleteTask);
            thread.setDaemon(true);
            thread.start();
        }
    }

    @Override
    public void escapeEvent() {
        if (btnCancel.getText().equals(resourceBundle.getString("close"))) {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                    resourceBundle.getString("alert.close"));
            Optional<ButtonType> res = alert.createConfirmationAlert();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
            }
        } else {
            MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("couponissue.title"),
                    resourceBundle.getString("alert.cancel"));
            Optional<ButtonType> res = alert.createConfirmationAlert();
            if (res.isPresent() && res.get() == ButtonType.OK) {
                if (vbox.getChildren().size() == 4)
                    vbox.getChildren().remove(panAdd);
                clearControls();
                btnAddSave.setDisable(false);
                btnAddSave.setText(resourceBundle.getString("add"));
                btnEditUpdate.setText(resourceBundle.getString("edit"));
                btnCancel.setText(resourceBundle.getString("close"));
                FocusUtils.requestFocus(tableIssueCoupon);
            }
        }
    }

    private boolean validate(CouponIssue couponIssue) {
        String message = "";

        if (dpDate.getValue() == null) {
            message = message + resourceBundle.getString("couponIssue.validation.date.empty") + "\n";
        } else if (dpDate.getValue().isAfter(LocalDate.now())) {
            message = message + resourceBundle.getString("couponIssue.validation.date.future") + "\n";
        }

//        if (dpDate.getValue() != null && member != null
//                && member.getCode().get != null) {
//            if (dpDate.getValue() != null
//                    && dpDate.getValue().isBefore(member.getMemberInformationCode().getRegistrationDate())) {
//                message += resources.getString("member.validation.birthdate.after.registration.valid") + "\n";
//            }
//        }

        if (txtCode.getText().trim().equals("") || cboxType.getSelectionModel().getSelectedItem() == null) {
            if (txtCode.getText().trim().equals("")) {
                message = message + resourceBundle.getString("couponIssue.validation.code.empty") + "\n";
            }
            if (cboxType.getSelectionModel().getSelectedItem() == null) {
                message = message + resourceBundle.getString("couponIssue.validation.type.empty") + "\n";
            }
        }
//        else {
//            LocalDate date = couponIssueService.fetchLatestDateByMember(couponIssue.getConsumerCode(),
//                    couponIssue.getConsumerType(), couponIssue);
//
//            if (dpDate.getValue() != null) {
//                if (date != null && dpDate.getValue().isBefore(date)) {
//                    message = message + resources.getString("couponIssue.validation.date.after.lastdate") + " "
//                            + Main.DATE_FORMATTER.format(date) + "\n";
//                }
//            }
//        }
//        constraintViolations = HibernateValidatorUtil.getValidator().validate(couponIssue);
//        if (constraintViolations != null && constraintViolations.size() > 0) {
//            for (ConstraintViolation<CouponIssue> constraintViolation : constraintViolations) {
//                message += resources.getString(constraintViolation.getMessage()) + "\n";
//            }
//        }
//        if (couponIssue.getAmount() <= 0) {
//            message += resources.getString("coupon.issue.validation.amount.empty") + "\n";
//        } else {
//            if ((couponIssueService.fetchAllByMemberExceptCurrent(propertyCouponIssue.get())
//                    + couponIssue.getAmount()) < localMilkSaleService.countCoupon(propertyCouponIssue.get())) {
//                message += resources.getString("coupon.issue.validation.minimum.amount") + " "
//                        + ((localMilkSaleService.countCoupon(propertyCouponIssue.get())
//                        - (couponIssueService.fetchAllByMemberExceptCurrent(propertyCouponIssue.get()))))
//                        + "\n";
//            }
//        }
        if (message != null && message.length() > 0) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("couponissue.validation.title"),
                    message);
            alert.createAlert();
            FocusUtils.requestFocus(dpDate);
            return true;
        }
        return false;
    }
}
