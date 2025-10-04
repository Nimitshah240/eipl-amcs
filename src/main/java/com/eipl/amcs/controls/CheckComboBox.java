package com.eipl.amcs.controls;

import javafx.beans.DefaultProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

@DefaultProperty("items")
public class CheckComboBox<T> extends Control {

    /**************************************************************************
     *
     * Private fields
     *
     **************************************************************************/

    private final ObservableList<T> items;
    private final Map<T, BooleanProperty> itemBooleanMap;
    private CheckComboBoxSkin<T> checkComboBoxSkin;


    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/
    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    // --- Check Model
    private final ObjectProperty<IndexedCheckModel<T>> checkModel =
            new SimpleObjectProperty<>(this, "checkModel"); //$NON-NLS-1$
    // --- converter
    private final ObjectProperty<StringConverter<T>> converter =
            new SimpleObjectProperty<StringConverter<T>>(this, "converter");


    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/
    // --- title
    private final StringProperty title = new SimpleStringProperty(null);
    // --- show how many items are checked over total
    private final BooleanProperty showCheckedCount = new SimpleBooleanProperty(false);

    /**
     * Creates a new CheckComboBox instance with an empty list of choices.
     */
    public CheckComboBox() {
        this(null);
    }


    /**
     * Creates a new CheckComboBox instance with the given items available as
     * choices.
     *
     * @param items The items to display within the CheckComboBox.
     */
    public CheckComboBox(final ObservableList<T> items) {
        final int initialSize = items == null ? 32 : items.size();

        this.itemBooleanMap = new HashMap<>(initialSize);
        this.items = items == null ? FXCollections.observableArrayList() : items;
        setCheckModel(new CheckComboBoxBitSetCheckModel<>(this.items, itemBooleanMap));
    }

    /**
     * Represents the list of choices available to the user, from which they can
     * select zero or more items.
     */
    public ObservableList<T> getItems() {
        return items;
    }

    /**
     * Returns the {@link BooleanProperty} for a given item index in the
     * CheckComboBox. This is useful if you want to bind to the property.
     */
    public BooleanProperty getItemBooleanProperty(int index) {
        if (index < 0 || index >= items.size()) return null;
        return getItemBooleanProperty(getItems().get(index));
    }

    /**
     * Returns the {@link BooleanProperty} for a given item in the
     * CheckComboBox. This is useful if you want to bind to the property.
     */
    public BooleanProperty getItemBooleanProperty(T item) {
        return itemBooleanMap.get(item);
    }

    /**
     * Returns the currently installed check model.
     */
    public final IndexedCheckModel<T> getCheckModel() {
        return checkModel == null ? null : checkModel.get();
    }

    /**
     * Sets the 'check model' to be used in the CheckComboBox - this is the
     * code that is responsible for representing the selected state of each
     * {@link CheckBox} - that is, whether each {@link CheckBox} is checked or
     * not (and not to be confused with the
     * selection model concept, which is used in the ComboBox control to
     * represent the selection state of each row)..
     */
    public final void setCheckModel(IndexedCheckModel<T> value) {
        checkModelProperty().set(value);
    }

    /**
     * The check model provides the API through which it is possible
     * to check single or multiple items within a CheckComboBox, as  well as inspect
     * which items have been checked by the user. Note that it has a generic
     * type that must match the type of the CheckComboBox itself.
     */
    public final ObjectProperty<IndexedCheckModel<T>> checkModelProperty() {
        return checkModel;
    }

    /**
     * A {@link StringConverter} that, given an object of type T, will
     * return a String that can be used to represent the object visually.
     */
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    /**
     * A {@link StringConverter} that, given an object of type T, will
     * return a String that can be used to represent the object visually.
     */
    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }

    /**
     * Sets the {@link StringConverter} to be used in the control.
     *
     * @param value A {@link StringConverter} that, given an object of type T, will
     *              return a String that can be used to represent the object visually.
     */
    public final void setConverter(StringConverter<T> value) {
        converterProperty().set(value);
    }

    /**
     * The title to use for this control. If a non null value is explicitly
     * set by the client, then that string will be used, otherwise a title
     * will be constructed concatenating the selected items
     */
    public final StringProperty titleProperty() {
        return title;
    }

    /**
     * The title set for this control, if it has been set explicitly by the client.
     *
     * @return the title if it has been set, null otherwise
     */
    public final String getTitle() {
        return title.getValue();
    }

    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    /**
     * Sets the title to use. If it is not null it will be used as title,
     * otherwise title will be constructed by the skin
     *
     * @param value the string to use as title
     */
    public final void setTitle(String value) {
        title.setValue(value);
    }

    /**
     * Requests that the ComboBox display the popup aspect of the user interface.
     */
    public void show() {
        if (checkComboBoxSkin != null) {
            checkComboBoxSkin.show();
        }
    }

    /***************************************************************************
     *                                                                         *
     * Stylesheet Handling                                                     *
     *                                                                         *
     **************************************************************************/

    /**
     * Closes the popup / dialog that was shown when {@link #show()} was called.
     */
    public void hide() {
        if (checkComboBoxSkin != null) {
            checkComboBoxSkin.hide();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Skin<?> createDefaultSkin() {
        checkComboBoxSkin = new CheckComboBoxSkin<>(this);
        return checkComboBoxSkin;
    }

    /**
     * A boolean to decide if the information of how many items are checked
     * should be shown beside the fixed title.
     * If a {@link #titleProperty()} has been set and this property is set to true
     * then a string like (3/10) would be shown when 3 items out of 10 are
     * checked.<br>
     * This property has effect only if a fixed title has been set (see {@link #titleProperty()}),
     * otherwise the title is constructed with a concatenation of the selected items.
     *
     * @return if the count should be shown
     */
    public final BooleanProperty showCheckedCountProperty() {
        return showCheckedCount;
    }

    /**
     * @return whether the checked items count is set to be shown beside a fixed title
     */
    public final boolean isShowCheckedCount() {
        return showCheckedCount.getValue();
    }

    /**
     * Sets the value to use to decide whether the checked items count should be
     * shown or not
     *
     * @param value the value to set
     */
    public final void setShowCheckedCount(boolean value) {
        showCheckedCount.setValue(value);
    }

    /**************************************************************************
     *
     * Support classes
     *
     **************************************************************************/

    private static class CheckComboBoxBitSetCheckModel<T> extends CheckBitSetModelBase<T> {

        /***********************************************************************
         *                                                                     *
         * Internal properties                                                 *
         *                                                                     *
         **********************************************************************/

        private final ObservableList<T> items;


        /***********************************************************************
         *                                                                     *
         * Constructors                                                        *
         *                                                                     *
         **********************************************************************/

        CheckComboBoxBitSetCheckModel(final ObservableList<T> items, final Map<T, BooleanProperty> itemBooleanMap) {
            super(itemBooleanMap);

            this.items = items;
            this.items.addListener((ListChangeListener<T>) c -> updateMap());

            updateMap();
        }


        /***********************************************************************
         *                                                                     *
         * Implementing abstract API                                           *
         *                                                                     *
         **********************************************************************/

        @Override
        public T getItem(int index) {
            return items.get(index);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemIndex(T item) {
            return items.indexOf(item);
        }
    }
}