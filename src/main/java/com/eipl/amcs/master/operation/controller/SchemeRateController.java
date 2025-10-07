package com.eipl.amcs.master.operation.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.master.operation.model.SchemeRateApplicability;
import com.eipl.amcs.master.operation.repository.SchemeRateApplicabilityRepository;
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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static com.eipl.amcs.MainApp.context;

public class SchemeRateController implements MyInitialization, PopupCallback {

    @FXML
    StackPane root;
    @FXML
    TableView<SchemeRateApplicability> tableSchemeRateApplicability;
    @FXML
    TableColumn<SchemeRateApplicability, String> colRateAppCode, colDescription, colRtpl;
    @FXML
    TableColumn<SchemeRateApplicability, Integer> colFromShift, colToShift;
    @FXML
    TableColumn<SchemeRateApplicability, LocalDate> colFromDate, colToDate;

    @FXML
    Button btnClose;

    private ResourceBundle resourceBundle;

    private SchemeRateApplicabilityRepository schemeRateApplicabilityRepository;

    public SchemeRateController() {
        schemeRateApplicabilityRepository = context.getBean(SchemeRateApplicabilityRepository.class);
    }

    @Override
    public Node getRoot() {
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
        loadData();
        setupTable();
    }

    @Override
    public void setupTable() {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            colRateAppCode.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getSchemeRateAppCode()));
            colFromDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFromDate().toLocalDate()));
            colFromShift.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getFromShift() == 1 ? resourceBundle.getString("Morning") : resourceBundle.getString("Evening")));
            colToShift.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getToShift() == 1 ? resourceBundle.getString("Morning") : resourceBundle.getString("Evening")));
            colToDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getToDate().toLocalDate()));
            colRtpl.setCellValueFactory(data -> new SimpleObjectProperty(df.format(data.getValue().getRtpl())));
            colDescription.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        try {
            tableSchemeRateApplicability.setItems(null);
            List<SchemeRateApplicability> list = schemeRateApplicabilityRepository.findByIsActiveTrue();
            if (list != null)
                tableSchemeRateApplicability.setItems(FXCollections.observableList(list));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}