package com.eipl.amcs.setting.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.setting.model.MilkCollectionAccountPosting;
import com.eipl.amcs.setting.task.MilkCollectionAccountPostingLoadTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class MilkCollectionAccountPostingController implements MyInitialization {

    @FXML
    Button btnClose, btnAdd, btnView, btnSearch;
    @FXML
    TableColumn<MilkCollectionAccountPosting, String> colFromShift, colToShift, colPostingType;
    @FXML
    TableColumn<MilkCollectionAccountPosting, LocalDate> colFromDate, colToDate;
    @FXML
    DatePicker dpFromDate, dpToDate;
    @FXML
    TableView<MilkCollectionAccountPosting> tblMilkCollectionAccountPosting;
    private final ObjectProperty<MilkCollectionAccountPosting> propMilkCollectionAccountPostingDto;
    private ResourceBundle resourceBundle;


    public MilkCollectionAccountPostingController() {
        this.propMilkCollectionAccountPostingDto = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadData();
        setupTable();
        this.resourceBundle = resourceBundle;

        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });

        btnSearch.setOnAction(e -> {
            loadMilkCollectionAccountPosting();
        });

        btnView.setOnAction(e -> {
            if (propMilkCollectionAccountPostingDto.get() != null) {
                MilkCollectionAccountPosting dto = propMilkCollectionAccountPostingDto.get();
                if (dto != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/setting/MilkCollectionAccountPostingAddEdit.fxml"));
                        loader.setResources(this.resourceBundle);
                        Parent root = loader.load();
                        MilkCollectionAccountPostingAddEditController controller = loader.getController();
                        controller.setMilkCollectionAccountPostingDto(dto);
                        MainApp.getContentPane().setCenter(root);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                }
            }
        });

        btnAdd.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/setting/MilkCollectionAccountPostingAddEdit.fxml")));
        });

        propMilkCollectionAccountPostingDto.addListener((observable, oldValue, newValue) -> {
            btnView.setDisable(newValue == null);
        });
    }

    @Override
    public void loadData() {
        dpFromDate.setValue(LocalDate.now());
        dpToDate.setValue(LocalDate.now());
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
        colPostingType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPostingType().toString()));
        propMilkCollectionAccountPostingDto.bind(tblMilkCollectionAccountPosting.getSelectionModel().selectedItemProperty());
    }

    public void loadMilkCollectionAccountPosting() {
        LocalDate fromDate = dpFromDate.getValue();
        LocalDate toDate = dpToDate.getValue();
        if (fromDate == null && toDate == null)
            return;
        var task = new MilkCollectionAccountPostingLoadTask(fromDate, toDate);
        task.setOnSucceeded(e -> {
            try {
                List<MilkCollectionAccountPosting> list = task.get();
                if (list != null)
                    tblMilkCollectionAccountPosting.setItems(FXCollections.observableList(list));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        new Thread(task).start();
    }
}