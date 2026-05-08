package com.eipl.amcs.controls.table;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class DynamicGroupedTable extends Application {

    @Override
    public void start(Stage stage) {
        TableView<TableRowModel> table = new TableView<>();

        // 1. Define Dynamic Columns (Matches headers in image_d08ebd.jpg)
        List<String> columnNames = Arrays.asList("Ledger", "Amount");

        for (String name : columnNames) {
            TableColumn<TableRowModel, String> col = new TableColumn<>(name);
            col.setCellValueFactory(data -> data.getValue().columnProperty(name));
            col.setPrefWidth(120);
            table.getColumns().add(col);
        }

        // 2. Load Dummy Data with Groups
        table.setItems(generateData());

        // 3. Row Factory for Dynamic Styling
        table.setRowFactory(tv -> new TableRow<TableRowModel>() {
            @Override
            protected void updateItem(TableRowModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item instanceof SummaryRow) {
                    // Apply bold/background for summary rows like in the image
                    setStyle("-fx-background-color: #e8f4ff; -fx-font-weight: bold;");
                } else {
                    setStyle("");
                }
            }
        });

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 800, 400);
        stage.setTitle("Dynamic Grouped Report");
        stage.setScene(scene);
        stage.show();
    }

    private ObservableList<TableRowModel> generateData() {
        ObservableList<TableRowModel> data = FXCollections.observableArrayList();

        // GROUP 1
        SummaryRow sum1 = new SummaryRow();
        sum1.setColumnValue("Name", "Total");
        sum1.setColumnValue("Amount", "5000.00");
        data.add(sum1);

        DataEntryRow row1 = new DataEntryRow();
        row1.setColumnValue("Index", "1");
        row1.setColumnValue("Code", "0001");
        row1.setColumnValue("Name", "Member Alpha");
        row1.setColumnValue("Liters", "150.00");
        row1.setColumnValue("Amount", "5000.00");
        data.add(row1);


        // GROUP 2
        SummaryRow sum2 = new SummaryRow();
        sum2.setColumnValue("Name", "Total");
        sum2.setColumnValue("Amount", "1000.00");
        data.add(sum2);

        DataEntryRow row2 = new DataEntryRow();
        row2.setColumnValue("Index", "2");
        row2.setColumnValue("Code", "0002");
        row2.setColumnValue("Name", "Member Beta");
        row2.setColumnValue("Liters", "20.00");
        row2.setColumnValue("Amount", "1000.00");
        data.add(row2);

        DataEntryRow row3 = new DataEntryRow();
//        row3.setColumnValue("Index", "2");
        row3.setColumnValue("Code", "0002");
        row3.setColumnValue("Name", "Member Beta");
        row3.setColumnValue("Liters", "20.00");
        row3.setColumnValue("Amount", "1000.00");
        data.add(row3);


        return data;
    }

    public static void main(String[] args) {
        launch(args);
    }
}