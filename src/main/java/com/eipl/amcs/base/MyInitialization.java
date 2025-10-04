package com.eipl.amcs.base;

import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.util.concurrent.ExecutionException;

/**
 * Custom initialization interface to provide behavior as per requirement.
 *
 * @author Chirag Patel
 */
public interface MyInitialization extends Initializable {

    /**
     * get root node from fxml file and return to application
     *
     * @return Node
     */
    Node getRoot();

    /**
     * Setup table method is used to setup table column/cell values. Also this
     * method provides column configurations from previously stored configuration
     * and to setup new configuration.
     */
    default void setupTable() {
    }

    /**
     * This method is used for setup combo box cell factory
     */
    default void setupComboBox() {
    }

    /**
     * Load data provides default data set from database corresponding to relevant
     * table.
     */
    default void loadData() {
    }

    /**
     * This method is used for clear input controls.
     */
    default void clearControls() {
    }

    /**
     * This method is used to load previous values in to input controls from table
     * row selection via property object.
     */
    default void loadControls() throws ExecutionException, InterruptedException {
    }

    /**
     * This method is responsible for storing data in to database table.
     */
    default void saveData() {
    }

    /**
     * This method is responsible for update data in to database table.
     */
    default void updateData() throws ExecutionException, InterruptedException {
    }

    /**
     * This method is responsible for delete data from database table.
     */
    default void deleteData() {
    }

    /**
     * This method invoked via escape key press event to reset screen.
     */
    default void escapeEvent() {
    }
}
