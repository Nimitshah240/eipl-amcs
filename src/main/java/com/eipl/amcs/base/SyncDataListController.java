package com.eipl.amcs.base;

import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.utils.task.BroadcastedGroupDataTask;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class SyncDataListController implements MyInitialization {

    @FXML
    private StackPane root;

    @FXML
    private TableView<TableData> tablePendingSync;

    @FXML
    private TableColumn<TableData, String> colTableName;

    @FXML
    private E_Button btnClose;
    @FXML
    private TableColumn<TableData, Integer> colPendingData;
    private Stage stage;
    private PopupCallback callback;
    private final ObservableList<TableData> tableDataList = FXCollections.observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Node getRoot() {
        return root;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadData();
        btnClose.setOnAction(e -> {
            stage.close();
        });
    }

    @Override
    public void setupTable() {
        colTableName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTableName()));
        colPendingData.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPendingCount()).asObject());
        tablePendingSync.setItems(tableDataList);
    }

    @Override
    public void loadData() {
        tableDataList.clear();

        Task<Map<String, Integer>> task = new BroadcastedGroupDataTask();
        task.setOnSucceeded(e -> {
            Map<String, Integer> result = task.getValue();
            if (result != null) {
                for (Map.Entry<String, Integer> entry : result.entrySet()) {
                    tableDataList.add(new TableData(entry.getKey(), entry.getValue()));
                }
            }
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
        });

        new Thread(task).start();
    }

    public static class TableData {
        private final String tableName;
        private final int pendingCount;

        public TableData(String tableName, int pendingCount) {
            this.tableName = tableName;
            this.pendingCount = pendingCount;
        }

        public String getTableName() {
            return tableName;
        }

        public int getPendingCount() {
            return pendingCount;
        }
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

}
