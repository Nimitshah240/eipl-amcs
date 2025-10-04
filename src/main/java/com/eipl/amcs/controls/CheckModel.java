package com.eipl.amcs.controls;

import javafx.collections.ObservableList;

public interface CheckModel<T> {

    /**
     * Returns the count of items in the control.
     */
    int getItemCount();

    /**
     * Returns a read-only list of the currently checked items in the control.
     */
    ObservableList<T> getCheckedItems();

    /**
     * Checks all items in the control
     */
    void checkAll();

    /**
     * Unchecks the given item in the control
     *
     * @param item The item to uncheck.
     */
    void clearCheck(T item);

    /**
     * Unchecks all items in the control
     */
    void clearChecks();

    /**
     * Returns true if there are no checked items in the control.
     */
    boolean isEmpty();

    /**
     * Returns true if the given item is checked in the control.
     *
     * @param item Item whose check property is to be tested.
     */
    boolean isChecked(T item);

    /**
     * Checks the given item in the control.
     *
     * @param item The item to check.
     */
    void check(T item);

    /**
     * Toggles the check state for the given item in the control.
     *
     * @param item The item for which check state needs to be toggled.
     */
    void toggleCheckState(T item);
}