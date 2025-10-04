package com.eipl.amcs.operation.administartion.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.model.Notification;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NotificationController implements MyInitialization {
    @FXML
    StackPane root;
    @FXML
    TableView<Notification> tableData;
    @FXML
    ComboBox<String> cbox;
    @FXML
    TableColumn<Notification, String> colTitle;
    @FXML
    TableColumn<Notification, Notification> col;
    @FXML
    TableColumn<Notification, String> colMessage, colPeriod;
    @FXML
    Button btnClose;
    private ObjectProperty<Notification> propData;

    @Override
    public Node getRoot() {
        return root;
    }

    public NotificationController() {
        propData = new SimpleObjectProperty<>();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTable();
        btnClose.setOnAction(e -> {
            MainApp.getContentPane().setCenter(MainApp.getFxmlLoaderUtil().load(MainApp.class.getResource("view/dashboard/Dashboard.fxml")));
        });
//        propData.addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                try {
//                    System.out.println("Opening Link");
//                    Desktop.getDesktop().browse(new URI(newValue.getFilePath()));
//                } catch (IOException | URISyntaxException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                System.out.println("Nothing Here Dude");
//            }
//        });

        //<option value="1">Alert</option>
        //<option value="2">Priptra</option>
        //<option value="3">Special Message</option>
        //<option value="4">Milk Bill</option>
        //<option value="5">Bacteria Test</option>
        //<option value="6">Eipl Bill</option>
        cbox.getItems().addAll("All", "Alert", "Paripatra", "Special Message", "Milk Bill", "Bacteria Test", "EVEREST Bill");
        cbox.getSelectionModel().select(0);
        cbox.setOnAction(e -> {
            loadData();
        });
        loadData();
    }

    @Override
    public void setupTable() {
        try {
            colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
            colMessage.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMessage()));
            colPeriod.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFromDate() != null ? data.getValue().getFromDate().
                    format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "-" + data.getValue().getToDate().format
                    (DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : ""));
            col.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
            col.setCellFactory(new Callback<>() {
                @Override
                public TableCell<Notification, Notification> call(TableColumn<Notification, Notification> param) {
                    return new TableCell<>() {
                        @Override
                        protected void updateItem(Notification item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty || item.getFilePath() == null || item.getFilePath().equalsIgnoreCase("")) {
                                setGraphic(null);
                            } else {
                                setGraphic(createNode(item));
                            }
                        }
                    };
                }
            });
            propData.bind(tableData.getSelectionModel().selectedItemProperty());
        } catch (Exception e) {
            System.out.println("Notification setuptable Exception");
            e.printStackTrace();
        }
    }

    @Override
    public void loadData() {
        if (cbox.getSelectionModel().getSelectedIndex() == 0)
            tableData.setItems(FXCollections.observableList(MainApp.notificationList));
        else if (cbox.getSelectionModel().getSelectedIndex() == 1)
            tableData.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 1).collect(Collectors.toList())));
        else if (cbox.getSelectionModel().getSelectedIndex() == 2)
            tableData.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 2).collect(Collectors.toList())));
        else if (cbox.getSelectionModel().getSelectedIndex() == 3)
            tableData.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 3).collect(Collectors.toList())));
        else if (cbox.getSelectionModel().getSelectedIndex() == 4)
            tableData.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 4).collect(Collectors.toList())));
        else if (cbox.getSelectionModel().getSelectedIndex() == 5)
            tableData.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 5).collect(Collectors.toList())));
        else if (cbox.getSelectionModel().getSelectedIndex() == 6)
            tableData.setItems(FXCollections.observableList(MainApp.notificationList.stream().filter(e -> e.getNotificationType() == 6).collect(Collectors.toList())));
        else
            tableData.setItems(FXCollections.observableList(MainApp.notificationList));
    }

    private Node createNode(Notification item) {
        Hyperlink button = new Hyperlink("Attachment");
        button.setOnMouseClicked(e -> {
            try {
                Desktop.getDesktop().browse(new URI(item.getFilePath()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return button;
    }
}
