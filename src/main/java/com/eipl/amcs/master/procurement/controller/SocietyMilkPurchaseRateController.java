package com.eipl.amcs.master.procurement.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.cellfactory.LocalDateCellFactory;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.procurement.dto.RateViewDto;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.task.SocietyMilkPurchaseRateLoadTask;
import com.eipl.amcs.utils.CommonUtils;
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
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class SocietyMilkPurchaseRateController implements MyInitialization {
    private final ObjectProperty<SocietyMilkPurchaseRate> propRate;
    @FXML
    StackPane root;
    @FXML
    TableView<SocietyMilkPurchaseRate> tableSocietyMilkPurchaseRates;
    @FXML
    TableColumn<SocietyMilkPurchaseRate, String> colCode, colDescription;
    @FXML
    TableColumn<SocietyMilkPurchaseRate, LocalDate> colWefDate;
    @FXML
    TableColumn<SocietyMilkPurchaseRate, RateType> colRateType;
    @FXML
    TableColumn<SocietyMilkPurchaseRate, Shift> colShift, colShiftApplicable;
    @FXML
    TableColumn<SocietyMilkPurchaseRate, String> colRateGenMethodCode;
    @FXML
    E_Button btnAdd, btnClose;
    @FXML
    Button btnView;

    public SocietyMilkPurchaseRateController() {
        propRate = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        loadData();

        propRate.addListener((observable, oldValue, newValue) -> {
            btnView.setDisable(newValue == null);
        });
        btnAdd.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/master/procurement/SocietyMilkPurchaseRateAddEdit.fxml")));
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
        btnView.setOnAction(e -> {
            RateViewDto dto = new RateViewDto();
            dto.setRateType((short) 1);
            dto.setSocietyRate(propRate.get());

            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "RateView", dto, null);
        });
    }

    @Override
    public void setupTable() {
        try {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
            colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
            colRateGenMethodCode.setCellValueFactory(data -> new SimpleStringProperty(CommonUtils.getRateGenerationMethod(data.getValue().getRateGenMethodCode())));
            colWefDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getWefDate().toLocalDate()));
            colShift.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShift()));
            colShiftApplicable.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getShiftApplicable()));
            colRateType.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRateType()));

            propRate.bind(tableSocietyMilkPurchaseRates.getSelectionModel().selectedItemProperty());
            TableLocalizationUtil.localizeTable(tableSocietyMilkPurchaseRates);
        } catch (Exception e) {
            System.out.println("SocietyMilkPurchaseRate setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new SocietyMilkPurchaseRateLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<SocietyMilkPurchaseRate> list = task.get();
                if (list != null)
                    tableSocietyMilkPurchaseRates.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
