package com.eipl.amcs.base;

import javafx.fxml.Initializable;
import javafx.scene.Node;

/**
 * Custom initialization interface to provide behavior as per requirement.
 *
 * @author Chirag Patel
 */
public interface MemberDataMigration extends Initializable {

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

    default void readMember() {
    }

    default void readCollectionData() {
    }

}
