package com.eipl.amcs.controls;

import javafx.collections.ObservableList;

public interface IndexedCheckModel<T> extends CheckModel<T> {

    /**
     * Returns the item in the given index in the control.
     *
     * @param index Index for the item in the control.
     */
    T getItem(int index);

    /**
     * Returns the index of the given item.
     *
     * @param item Item whose index needs to be fetched.
     */
    int getItemIndex(T item);

    /**
     * Returns a read-only list of the currently checked indices in the control.
     */
    ObservableList<Integer> getCheckedIndices();

    /**
     * Checks the given indices in the control
     *
     * @param indices Indices of item to uncheck.
     */
    void checkIndices(int... indices);

    /**
     * Unchecks the given index in the control
     *
     * @param index Index of the item to uncheck.
     */
    void clearCheck(int index);

    /**
     * Returns true if the given index represents an item that is checked in the control.
     *
     * @param index Index of the item to be tested.
     */
    boolean isChecked(int index);

    /**
     * Checks the item in the given index in the control.
     *
     * @param index Index of the item to check.
     */
    void check(int index);

    /**
     * Toggles the check state of the item in the given index of the control.
     *
     * @param index Index of the item whose check state needs to be toggled.
     */
    void toggleCheckState(int index);

}