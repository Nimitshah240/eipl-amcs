package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.master.account.model.Events;
import com.eipl.amcs.master.account.task.EventsLoadTask;
import com.eipl.amcs.setting.model.AccountPosting;
import com.eipl.amcs.setting.task.AccountPostingLoadTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public class AccountPostingController implements MyInitialization {

    @FXML
    Button btnClose, btnAdd, btnView, btnSearch;
    @FXML
    TableColumn<AccountPosting, String> colFromShift, colToShift, colPostingType, colStatus, colEvent;
    @FXML
    TableColumn<AccountPosting, LocalDate> colFromDate, colToDate;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    TableView<AccountPosting> tblAccountPosting;
    private final ObjectProperty<AccountPosting> propMilkCollectionAccountPostingDto;
    private ResourceBundle resourceBundle;
    Map<Integer, Events> eventMap = new HashMap<>();

    public AccountPostingController() {
        this.propMilkCollectionAccountPostingDto = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadData();
        setupTable();
        loadControls();
        this.resourceBundle = resourceBundle;


    }

    @Override
    public void loadControls() {
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnSearch.setOnAction(e -> {
            loadMilkCollectionAccountPosting();
        });

        btnView.setOnAction(e -> {
            if (propMilkCollectionAccountPostingDto.get() != null) {
                AccountPosting dto = propMilkCollectionAccountPostingDto.get();
                if (dto != null) {
                    editAccountPosting(dto);
                }
            }
        });

        btnAdd.setOnAction(e -> {
            boolean oneDraft = tblAccountPosting.getItems()
                    .stream()
                    .anyMatch(ap -> ap.getStatus() == 1);

            if (!oneDraft)
                MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/AccountPostingAddEdit.fxml")));
        });

        propMilkCollectionAccountPostingDto.addListener((observable, oldValue, newValue) -> {
            btnView.setDisable(newValue == null);
        });

        tblAccountPosting.setRowFactory(tv -> {
            TableRow<AccountPosting> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    AccountPosting data = row.getItem();
                    editAccountPosting(data);
                }
            });
            return row;
        });
    }

    private void editAccountPosting(AccountPosting dto) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/setting/AccountPostingAddEdit.fxml"));
            loader.setResources(this.resourceBundle);
            Parent root = loader.load();
            AccountPostingAddEditController controller = loader.getController();
            controller.setAccountPostingDto(dto);
            MainApp.getContentPane().setCenter(root);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }


    @Override
    public void loadData() {
        dpFromDate.setValue(LocalDate.now().withDayOfMonth(1));
        dpToDate.setValue(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
        loadEvents();
        loadMilkCollectionAccountPosting();
    }

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void setupTable() {
        colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate()));
        colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate()));
        colFromShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromShift().toString()));
        colToShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToShift().toString()));
        colPostingType.setCellValueFactory(data -> new SimpleObjectProperty<>(AppConstant.PostingType.fromValue(data.getValue().getPostingType()).getLabel()));
        colStatus.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStatus() == 1 ? "Draft" : "Posted"));
        colEvent.setCellValueFactory(data -> new SimpleObjectProperty<>(eventMap.get(data.getValue().getEventType()).toString()));
        propMilkCollectionAccountPostingDto.bind(tblAccountPosting.getSelectionModel().selectedItemProperty());
    }

    public void loadMilkCollectionAccountPosting() {
        LocalDate fromDate = dpFromDate.getValue();
        LocalDate toDate = dpToDate.getValue();
        if (fromDate == null && toDate == null)
            return;
        var task = new AccountPostingLoadTask(fromDate, toDate);
        task.setOnSucceeded(e -> {
            try {
                List<AccountPosting> list = task.get();
                if (list != null)
                    tblAccountPosting.setItems(FXCollections.observableList(list));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }

    public void loadEvents() {
        var task = new EventsLoadTask();
        task.setOnSucceeded(e -> {
                    List<Events> eventList = null;
                    try {
                        eventList = task.get();
                        eventMap = eventList.stream()
                                .collect(Collectors.toMap(Events::getCode, eve -> eve));

//                        setupTable();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
        new Thread(task).start();
    }
}