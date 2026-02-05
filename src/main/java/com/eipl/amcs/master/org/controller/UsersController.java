package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.auth.model.User;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.exception.UnAuthorizedAccessException;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.task.UserLoadTask;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class UsersController implements MyInitialization, PopupCallback {

    private final ObjectProperty<User> propUser;
    @FXML
    private StackPane root;
    @FXML
    private TableView<User> tableUsers;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colMobileNo;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnClose;

    private ResourceBundle resourceBundle;

    public UsersController() {
        propUser = new SimpleObjectProperty<>();
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

        propUser.addListener((observable, oldValue, newValue) -> {
            btnEdit.setDisable(newValue == null);
        });

        btnEdit.setOnAction(e -> {
            if (!MainApp.user.getPermissions().contains("ACTION_DOCK_EDIT"))
                throw new UnAuthorizedAccessException();
            User dto = propUser.get();
            if (dto != null)
                MainApp.getFxmlLoaderUtil().openMappingPopupStage(MainApp.class.getResource("view/MappingPopUp.fxml"), "UsersAddEdit",dto, this);
        });

        btnClose.setOnAction(e -> MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml"))));
    }

    @Override
    public void setupTable() {
        colUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colMobileNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMobileNo()));
        propUser.bind(tableUsers.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void loadData() {
        var task = new UserLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<User> list = task.get();
                if (list != null) {
                    tableUsers.setItems(FXCollections.observableList(list));
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void reloadData(boolean flag) {
        if (flag) {
            loadData();
        }
    }
}
