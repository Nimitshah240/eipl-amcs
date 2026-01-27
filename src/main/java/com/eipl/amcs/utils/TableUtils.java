package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkin;
import javafx.scene.input.MouseEvent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TableUtils {
    public static void addCustomTableMenu(TableView<?> tableView, String key) {
        // enable table menu
        tableView.setTableMenuButtonVisible(true);

        // replace internal mouse listener with custom listener
        setCustomContextMenu(tableView, key);

    }

    private static void setCustomContextMenu(TableView<?> table, String key) {

        TableViewSkin<?> tableSkin = (TableViewSkin<?>) table.getSkin();

        // get all children of the skin
        ObservableList<Node> children = tableSkin.getChildren();

        // find the TableHeaderRow child
        for (int i = 0; i < children.size(); i++) {

            Node node = children.get(i);

            if (node instanceof TableHeaderRow) {

                TableHeaderRow tableHeaderRow = (TableHeaderRow) node;

                double defaultHeight = tableHeaderRow.getHeight();
                tableHeaderRow.setPrefHeight(defaultHeight);

                for (Node child : tableHeaderRow.getChildren()) {

                    // child identified as cornerRegion in TableHeaderRow.java
                    if (child.getStyleClass().contains("show-hide-columns-button")) {

                        // get the context menu
                        ContextMenu columnPopupMenu = createContextMenu(table, key);

                        // replace mouse listener
                        child.setOnMousePressed(me -> {
                            // show a popupMenu which lists all columns
                            columnPopupMenu.show(child, Side.BOTTOM, -20, 0);
                            me.consume();
                        });
                    }
                }

            }
        }
    }

    private static ContextMenu createContextMenu(TableView<?> table, String key) {

        ContextMenu cm = new ContextMenu();

        // create new context menu
        CustomMenuItem cmi;

        // select all item
        Label showAll = new Label("Show All");
        showAll.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                for (Object obj : table.getColumns()) {
                    ((TableColumn<?, ?>) obj).setVisible(true);
                }
            }

        });

        cmi = new CustomMenuItem(showAll);
        cmi.setHideOnClick(false);
        cm.getItems().add(cmi);

        // de select all item
        Label hideAll = new Label("Hide All");
        hideAll.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                for (Object obj : table.getColumns()) {
                    ((TableColumn<?, ?>) obj).setVisible(false);
                }
            }

        });

        cmi = new CustomMenuItem(hideAll);
        cmi.setHideOnClick(false);
        cm.getItems().add(cmi);

        // separator
        cm.getItems().add(new SeparatorMenuItem());

        // menu item for each of the available columns
        for (Object obj : table.getColumns()) {

            TableColumn<?, ?> tableColumn = (TableColumn<?, ?>) obj;

            CheckBox cb = new CheckBox(tableColumn.getText());
            cb.selectedProperty().bindBidirectional(tableColumn.visibleProperty());

            cmi = new CustomMenuItem(cb);
            cmi.setHideOnClick(false);

            cm.getItems().add(cmi);
        }

        // Export Menu Item
        cm.getItems().add(new SeparatorMenuItem());
        Label export = new Label("Save Settings");
        export.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            List<TableColItem> listColumns = new ArrayList<>();
            for (Object obj : table.getColumns()) {
                if (((TableColumn<?, ?>) obj).isVisible()) {
                    listColumns.add(new TableColItem(((TableColumn<?, ?>) obj).getId(), true));
                } else {
                    listColumns.add(new TableColItem(((TableColumn<?, ?>) obj).getId(), false));
                }
            }
            MainApp.tableConfiguration.put(key, listColumns);
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("resources/table.ser"));
                out.writeObject(MainApp.tableConfiguration);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cm.hide();
        });
        cmi = new CustomMenuItem(export);
        cmi.setHideOnClick(false);
        cm.getItems().add(cmi);

        return cm;
    }
}
