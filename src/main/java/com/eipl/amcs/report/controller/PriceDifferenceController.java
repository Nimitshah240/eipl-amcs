package com.eipl.amcs.report.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.controls.combobox.AutoCompleteComboBoxListener;
import com.eipl.amcs.controls.convertor.LocalDateConvertor;
import com.eipl.amcs.master.global.convertor.MilkTypeConvertor;
import com.eipl.amcs.master.global.convertor.ShiftConvertor;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.global.task.ShiftLoadTask;
import com.eipl.amcs.master.operation.convertor.MemberCellFactory;
import com.eipl.amcs.master.operation.convertor.MemberReportConvertor;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.task.MemberLoadTask;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class PriceDifferenceController implements MyInitialization {

    @FXML
    private StackPane root;
    @FXML
    private Button btnGenerate, btnClose;
    @FXML
    private DatePicker dpFirstDate, dpSecondDate;
    @FXML
    private ComboBox<Shift> cboxFirstShift, cboxSecondShift;
    @FXML
    private ComboBox<MilkType> cboxMilkType;
    private List<MilkType> listMilkType;
    @FXML
    private ComboBox<Member> cboxMemberCode;

    private ResourceBundle resourceBundle;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        dpFirstDate.setValue(LocalDate.now());
        dpSecondDate.setValue(LocalDate.now());
        dpFirstDate.setConverter(new LocalDateConvertor());
        dpFirstDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpFirstDate.setValue(dpFirstDate.getConverter().fromString(dpFirstDate.getEditor().getText()));
            }
        });
        dpSecondDate.setConverter(new LocalDateConvertor());
        dpSecondDate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                dpSecondDate.setValue(dpSecondDate.getConverter().fromString(dpSecondDate.getEditor().getText()));
            }
        });
        loadData();
        loadShift();
        setupComboBox();
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

    @Override
    public void setupComboBox() {
        cboxFirstShift.setConverter(new ShiftConvertor(cboxFirstShift));
        cboxSecondShift.setConverter(new ShiftConvertor(cboxSecondShift));
        cboxMilkType.setConverter(new MilkTypeConvertor(cboxMilkType));
        cboxMemberCode.setConverter(new MemberReportConvertor(cboxMemberCode));
        cboxMemberCode.setCellFactory(new MemberCellFactory());

    }

    private boolean validate() {
        return true;
    }

    private void loadShift() {
        var task = new ShiftLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Shift> list = task.get();
                cboxFirstShift.setItems(FXCollections.observableList(list));
                cboxSecondShift.setItems(FXCollections.observableList(list));
                cboxSecondShift.getSelectionModel().select(1);
                cboxFirstShift.getSelectionModel().select(0);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();

        var task1 = new MilkTypeLoadTask();
        task1.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task1.get();
                if (list != null && !list.isEmpty()) {
                    listMilkType = new ArrayList<>();
                    listMilkType.add(0, new MilkType(0, MainApp.bundle.getString("all")));
                    listMilkType.addAll(list);
                    cboxMilkType.setItems(FXCollections.observableList(listMilkType));
                    cboxMilkType.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task1).start();

        MemberLoadTask task2 = new MemberLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Member> list = task2.get();
                if (list != null) {
                    List<Member> list2 = new ArrayList<>();
                    Member m = new Member();
                    m.setCode("0");
                    m.setCodeEx("0");
                    m.setFirstName("All");
                    list2.add(m);
                    list2.addAll(list);
                    cboxMemberCode.setItems(FXCollections.observableList(list2));
                    new AutoCompleteComboBoxListener<>(cboxMemberCode);
                    cboxMemberCode.getSelectionModel().select(0);
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

}
