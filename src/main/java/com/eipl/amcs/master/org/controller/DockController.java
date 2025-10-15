package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.base.model.UnAuthorizedAccessException;
import com.eipl.amcs.controls.alert.ConfirmationAlert;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.service.DockService;
import com.eipl.amcs.master.org.task.DockDeleteTask;
import com.eipl.amcs.master.org.task.DockLoadTask;
import com.eipl.amcs.util.CommonUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import static com.eipl.amcs.MainApp.context;

public class DockController implements MyInitialization, PopupCallback {

    @FXML
    TableView<DockMilkTypeDto> tableDock;
    @FXML
    TableColumn<DockMilkTypeDto, String> colDockNo, colIsDefault, colMilkType;
    @FXML
    TableColumn<DockMilkTypeDto, Society> colSociety;
    @FXML
    Button btnClose, btnAdd, btnEdit, btnDelete;
    @FXML
    private StackPane root;
    private ResourceBundle resourceBundle;
    private final ObjectProperty<DockMilkTypeDto> propDockMilkTypeDto;

    public DockController() {
        propDockMilkTypeDto = new SimpleObjectProperty<>();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
        setupTable();
        loadData();

        propDockMilkTypeDto.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                btnEdit.setDisable(true);
                btnDelete.setDisable(true);
            }
        });

        btnAdd.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_DOCK_ADD"))
                throw new UnAuthorizedAccessException();
            MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "DockAddEdit", null, this);
        });
        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_DOCK_EDIT"))
                throw new UnAuthorizedAccessException();
            DockMilkTypeDto dto = propDockMilkTypeDto.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "DockAddEdit", dto, this);
        });
        btnDelete.setOnAction(e ->{
            if (!MainApp.user.getPermissions().contains("ACTION_DOCK_DELETE"))
                throw new UnAuthorizedAccessException();
            deleteData();
        });
        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

    @Override
    public void setupTable() {
        try{
            colIsDefault.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDock().getIsDefault() != 0 ? resourceBundle.getString("yes") : resourceBundle.getString("no")));
            colDockNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDock().getDockNo()));
            colSociety.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDock().getSociety()));
            colMilkType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMilkTypesAsString()));

            propDockMilkTypeDto.bind(tableDock.getSelectionModel().selectedItemProperty());
        }catch (Exception e) {
            System.out.println("Dock setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        var task = new DockLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<DockMilkTypeDto> list = task.get();
                if (list != null)
                    tableDock.setItems(FXCollections.observableList(list));
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void deleteData() {
        MyAlert alert = new ConfirmationAlert(MainApp.getStage(), resourceBundle.getString("dock"),
                resourceBundle.getString("alert.delete"));
        Optional<ButtonType> resp = alert.createConfirmationAlert();
        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            DockMilkTypeDto dto = propDockMilkTypeDto.get();
            if (dto != null) {
                var task = new DockDeleteTask(dto.getDock().getDockNo());
                task.setOnSucceeded(e -> {
                    try {
                        Boolean respDelete = task.get();
                        if (respDelete == null || respDelete.booleanValue() == false) {
                            MyAlert alert1 = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("dock"),
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

    @Override
    public void reloadData(boolean flag) {
        if (flag)
            loadData();
    }
}
