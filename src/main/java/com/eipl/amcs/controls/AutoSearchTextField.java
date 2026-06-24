package com.eipl.amcs.controls;

import com.ibm.icu.text.Transliterator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An optimized generic auto-searching autocomplete text field with dynamic transliteration context support.
 *
 * @param <T> The data type of the object being listed/searched.
 */
public class AutoSearchTextField<T> extends TextField {

    private final ObservableList<T> masterList;
    private final Function<T, String> textExtractor;
    private Function<T, String> subTextExtractor;

    private Consumer<T> onItemSelected;
    private final Popup popup;
    private final ListView<T> listView;

    private boolean suppressFilter = false;
    private T selectedItem = null;
    private Integer selectedIndex = -1;

    private static final int MAX_ROWS = 6;
    private Transliterator transliterator;
    private final StringBuilder currentWord = new StringBuilder();
    private int previousGujaratiLength = 0;
    private String language = "English";

    //NIMIT | 03.06.2026 | Tracks objects selected before items are loaded
    private T deferredSelectedItem = null;
    private boolean openPopup = true;

    /**
     * FXML Default Constructor. Required by FXMLLoader.
     */
    public AutoSearchTextField() {
        this.masterList = FXCollections.observableArrayList();
        this.textExtractor = item -> (item != null) ? item.toString() : "";
        this.subTextExtractor = null;

        this.listView = new ListView<>();
        this.popup = new Popup();

        applyBaseStyles();
        initializeControlNodes();
        setupLocalTransliteration();

    }

    /**
     * Single-line mode constructor.
     */
    public AutoSearchTextField(List<T> items, Function<T, String> textExtractor) {
        this(items, textExtractor, null);
        applyBaseStyles();
        setupLocalTransliteration();
        initializeControlNodes();
    }

    /**
     * Two-line detailed mode constructor.
     */
    public AutoSearchTextField(List<T> items, Function<T, String> textExtractor, Function<T, String> subTextExtractor) {
        this.masterList = FXCollections.observableArrayList(items);
        this.textExtractor = textExtractor;
        this.subTextExtractor = subTextExtractor;

        this.listView = new ListView<>();
        this.popup = new Popup();

        applyBaseStyles();
        initializeControlNodes();
        setupLocalTransliteration();
    }

    private void applyBaseStyles() {
        this.setPrefHeight(28);
        this.getStyleClass().add("auto-search-text-field");

        var imageResource = getClass().getResource("/com/eipl/amcs/view/images/search.png");
        String imagePathString = "";

        if (imageResource != null) {
            imagePathString = imageResource.toExternalForm();
        }

        this.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 6 36 6 12; " +
                        "-fx-background-color: white; " +
                        "-fx-border-color: #aab7c4; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 4, 0, 0, 1); " +
                        "-fx-background-image: url('" + imagePathString + "'); " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-position: right 12px center; " +
                        "-fx-background-size: 16px 16px;"
        );
    }


    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0      Set design and initialize different methods
     * 08/06/2026    Nimit             1.0.1      Added focus method call - to show popup on focus
     */
    private void initializeControlNodes() {
        this.listView.setFocusTraversable(false);
        this.listView.setStyle("-fx-background-color: white; -fx-background-insets: 0; -fx-padding: 0;");
        this.listView.setCellFactory(lv -> new DynamicCell());

        VBox popupContainer = new VBox(listView);
        popupContainer.setStyle("-fx-background-color: white; -fx-border-color: #c0c7d0; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.14), 12, 0, 0, 4);");
        popupContainer.setPadding(new Insets(4, 0, 4, 0));

        this.popup.setAutoHide(true);
        this.popup.setAutoFix(true);
        this.popup.setConsumeAutoHidingEvents(false);
        this.popup.getContent().add(popupContainer);

        wireTextChanges();
        wireKeyNavigation();
        wireMouseSelection();
        wireFocusLost();
        focusedOnTextField();
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 08/06/2026    Nimit             1.0.0       To show popup on getting focus on textfield, and select item if any selected.
     */
    private void focusedOnTextField() {
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (this.openPopup) {
                    showPopup(masterList);
                }
                if (selectedItem != null)
                    listView.getSelectionModel().select(selectedItem);
            } else {
                hidePopup();
            }
        });
    }

    public void setupLocalTransliteration() {
        if (language.equalsIgnoreCase("English")) {
            return;
        }
        this.transliterator = createTransliterator();
        textFieldLocal();
    }

    //    -------------------- CONTROLS FOR TEXTFIELD AND POPUP -----------------------
    private void wireTextChanges() {
        textProperty().addListener((obs, oldText, newText) -> {
            if (suppressFilter) return;
            selectedItem = null;
            if (newText == null || newText.isBlank()) {
                hidePopup();
                return;
            }
            filterAndShow(newText.trim());
        });
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       To manage different key events
     * 08/06/2026    Nimit             1.0.1       Solved bug of pre-selected value and
     * just click enter without changing data then it will take the pre-selected value.
     */
    private void wireKeyNavigation() {
        addEventFilter(KeyEvent.KEY_RELEASED, event -> {

//          Nimit | 03.06.2026 : WHEN POPUP IS NOT VISIBLE - KEY ACTIONS.
            if (!popup.isShowing()) {
                this.openPopup = true;
                switch (event.getCode()) {
                    case ENTER:
                        if (selectedItem != null) {
                            selectItem(selectedItem);
                            this.fireEvent(new javafx.event.ActionEvent(this, null));
                        }

                        Platform.runLater(() -> {
                            this.fireEvent(new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "",
                                    KeyCode.TAB, false, false, false, false
                            ));
                        });

                        event.consume();
                        return;
                    case RIGHT:
                        if (event.isControlDown()) {

                            this.fireEvent(new javafx.event.ActionEvent(this, null));
                            Platform.runLater(() -> {
                                this.fireEvent(new KeyEvent(
                                        KeyEvent.KEY_PRESSED, "", "",
                                        KeyCode.TAB, false, false, false, false
                                ));
                            });
                            event.consume();
                        }
                        break;
                    case LEFT:
                        if (event.isControlDown()) {

                            this.fireEvent(new javafx.event.ActionEvent(this, null));
                            Platform.runLater(() -> {
                                this.fireEvent(new KeyEvent(
                                        KeyEvent.KEY_PRESSED, "", "",
                                        KeyCode.TAB, true, false, false, false
                                ));
                            });
                            event.consume();
                        }
                        break;
                }
            }


            int size = listView.getItems().size();
            if (size == 0) return;
            int current = listView.getSelectionModel().getSelectedIndex();

//          Nimit | 03.06.2026 : WHEN POPUP IS VISIBLE - KEY ACTIONS.
            switch (event.getCode()) {
                case DOWN:
                    int next = (current + 1) % size;
                    listView.getSelectionModel().select(next);
                    listView.scrollTo(next);
                    event.consume();
                    break;
                case UP:
                    int prev = (current <= 0) ? size - 1 : current - 1;
                    listView.getSelectionModel().select(prev);
                    listView.scrollTo(prev);
                    event.consume();
                    break;
                case ENTER:
                    T highlighted = listView.getSelectionModel().getSelectedItem();
                    if (highlighted != null) {
                        selectItem(highlighted);
                        this.fireEvent(new javafx.event.ActionEvent(this, null));
                    }
                    this.fireEvent(new KeyEvent(
                            KeyEvent.KEY_PRESSED, "", "",
                            KeyCode.TAB, false, false, false, false
                    ));
                    event.consume();
                    break;
                case ESCAPE:
                    hidePopup();
                    event.consume();
                    break;
                case RIGHT:
                    if (event.isControlDown()) {

                        this.fireEvent(new javafx.event.ActionEvent(this, null));
                        Platform.runLater(() -> {
                            this.fireEvent(new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "",
                                    KeyCode.TAB, false, false, false, false
                            ));
                        });
                        event.consume();
                    }
                    break;
                case LEFT:
                    if (event.isControlDown()) {

                        this.fireEvent(new javafx.event.ActionEvent(this, null));
                        Platform.runLater(() -> {
                            this.fireEvent(new KeyEvent(
                                    KeyEvent.KEY_PRESSED, "", "",
                                    KeyCode.TAB, true, false, false, false
                            ));
                        });
                        event.consume();
                    }
                default:
                    break;
            }
        });
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       To handle mouse click selection
     * 24/06/2026    Nimit             1.0.1       Change focus on mouse click on popup and also setOnAction work.
     */
    private void wireMouseSelection() {
        listView.setOnMouseClicked(event -> {
            T clicked = listView.getSelectionModel().getSelectedItem();
            if (clicked != null) {
                selectItem(clicked);
                this.fireEvent(new javafx.event.ActionEvent(this, null));
            }
        });
        this.fireEvent(new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "",
                KeyCode.TAB, false, false, false, false
        ));
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       Close popup on focus lost
     */
    private void wireFocusLost() {
        focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) hidePopup();
        });
    }

//    -------------------- CONTROLS FOR TEXTFIELD AND POPUP -----------------------


    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO FILTER DATA BY SEARCH QUERY
     * 08/06/2026    Nimit             1.0.1       Extract showing popup to different method for multiple use.
     * 24/06/2026    Nimit             1.0.2       Change method to filter data using local language also.
     */
    private void filterAndShow(String query) {
        String lower = query.toLowerCase();

        List<T> results = masterList.stream()
                .filter(item -> {
                    String text = textExtractor.apply(item);
                    if (text == null) return false;

                    String localizedText = com.eipl.amcs.utils.FormatterFactory.convertEnglishToLocalizedDigits(text);
                    return text.toLowerCase().contains(lower) ||
                            (localizedText != null && localizedText.toLowerCase().contains(lower));
                })
                .limit(50)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            hidePopup();
            return;
        }
        showPopup(results);
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 08/06/2026    Nimit             1.0.0       TO SHOW POPUP OF DATA
     */
    private void showPopup(List<T> results) {
        listView.setItems(FXCollections.observableArrayList(results));
        listView.getSelectionModel().selectFirst();

        int staticCellHeight = subTextExtractor != null ? 60 : 40;
        int visibleRows = Math.min(results.size(), MAX_ROWS);
        listView.setPrefHeight((visibleRows * staticCellHeight) + 8);
        listView.setPrefWidth(ListView.USE_COMPUTED_SIZE);
        showPopupBelow();
    }


    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO SHOW POPUP OF FILTER DATA
     */
    private void showPopupBelow() {
        if (getScene() == null || getScene().getWindow() == null) return;
        var bounds = localToScreen(getBoundsInLocal());
        if (bounds == null) return;

        listView.setPrefWidth(ListView.USE_COMPUTED_SIZE);
        if (!popup.isShowing()) {
            popup.show(getScene().getWindow(), bounds.getMinX(), bounds.getMaxY() + 2);
        }
    }


    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO HIDE POPUP
     */
    private void hidePopup() {
        popup.hide();
    }

    public void setOnUserSelected(Consumer<T> callback) {
        this.onItemSelected = callback;
    }

    public T getValue() {
        return selectedItem;
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO SELECT ITEM PROVIDED AND AVAILABLE IN THE LIST
     * 08/06/2026    Nimit             1.0.1       Solved bug - To get index of the selected item from the list.
     * And clear string which is written by user for searching
     * 24/06/2026    Nimit             1.0.2       On selection number will also convert to local language.
     */
    private void selectItem(T item) {
        currentWord.setLength(0);
        previousGujaratiLength = 0;
        selectedItem = item;
        if (masterList != null && item != null) {
            selectedIndex = masterList.indexOf(item);
        }
        suppressFilter = true;
        String displayStr = textExtractor.apply(item);
        String localizedStr = com.eipl.amcs.utils.FormatterFactory.convertEnglishToLocalizedDigits(displayStr);
        setText(localizedStr);
        positionCaret(displayStr.length());
        suppressFilter = false;
        hidePopup();
        if (onItemSelected != null) {
            onItemSelected.accept(item);
        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO SELECT ITEM PROVIDED AND AVAILABLE IN THE LIST
     * 08/06/2026    Nimit             1.0.1       Solved bug - To get index of the selected item from the list.
     */
    public void setValue(T item) {
        if (item == null) {
            return;
        }
        if (masterList != null) {
            selectedIndex = masterList.indexOf(item);
        }
        this.selectedItem = item;
        this.suppressFilter = true;
        String displayStr = textExtractor.apply(item);
        this.setText(displayStr);
        this.positionCaret(displayStr.length());
        this.suppressFilter = false;
        hidePopup();
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO SET LIST OF ITEMS
     */
    public void setItems(ObservableList<T> newItems) {
        if (newItems == null) {
            this.masterList.clear();
        } else {
            this.masterList.setAll(newItems);
            if (!newItems.isEmpty()) {
                String sampleText = textExtractor.apply(newItems.get(0));
                detectLanguage(sampleText);
                setupLocalTransliteration();
                listView.setItems(newItems);

            }
            if (selectedIndex > -1 && selectedIndex < masterList.size()) {
                setValue(masterList.get(selectedIndex));
                return;
            }

            if (deferredSelectedItem != null) {
                if (masterList.contains(deferredSelectedItem)) {
                    setValue(deferredSelectedItem);
                } else {
                    setValue(deferredSelectedItem);
                }
                deferredSelectedItem = null;
                return;
            }

            if (selectedItem != null) {
                setValue(selectedItem);
            }

        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 08/06/2026    Nimit             1.0.0       To set subtext in the list
     */
    public void setItems(ObservableList<T> newItems, Function<T, String> subTextExtractor) {
        if (newItems == null) {
            this.masterList.clear();
        } else {
            this.subTextExtractor = subTextExtractor;
            this.masterList.setAll(newItems);
            if (!newItems.isEmpty()) {
                String sampleText = textExtractor.apply(newItems.get(0));
                detectLanguage(sampleText);
                setupLocalTransliteration();
                listView.setItems(newItems);

            }
            if (selectedIndex > -1 && selectedIndex < masterList.size()) {
                setValue(masterList.get(selectedIndex));
                return;
            }

            if (deferredSelectedItem != null) {
                if (masterList.contains(deferredSelectedItem)) {
                    setValue(deferredSelectedItem);
                } else {
                    setValue(deferredSelectedItem);
                }
                deferredSelectedItem = null;
                return;
            }

            if (selectedItem != null) {
                setValue(selectedItem);
            }

        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO SET LIST OF ITEMS
     * 08/06/2026    Nimit             1.0.1       Solved bug- It will select value if already pre-selected before setting list
     */
    public void setItems(List<T> newItems) {
        clearSelection();
        if (newItems == null) {
            this.masterList.clear();
        } else {
            this.masterList.setAll(newItems);
            if (!newItems.isEmpty()) {
                String sampleText = textExtractor.apply(newItems.get(0));
                detectLanguage(sampleText);
                setupLocalTransliteration();
                listView.setItems(FXCollections.observableArrayList(newItems));

            }
            if (selectedIndex > -1 && selectedIndex < masterList.size()) {
                setValue(masterList.get(selectedIndex));
                return;
            }

            if (deferredSelectedItem != null) {
                if (masterList.contains(deferredSelectedItem)) {
                    setValue(deferredSelectedItem);
                }
                deferredSelectedItem = null;
                return;
            }

            if (selectedItem != null) {
                setValue(selectedItem);
            }
        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 08/06/2026    Nimit             1.0.0       To set sub text in the list.
     */
    public void setItems(List<T> newItems, Function<T, String> subTextExtractor) {
        clearSelection();
        this.subTextExtractor = subTextExtractor;
        if (newItems == null) {
            this.masterList.clear();
        } else {
            this.masterList.setAll(newItems);
            if (!newItems.isEmpty()) {
                String sampleText = textExtractor.apply(newItems.get(0));
                detectLanguage(sampleText);
                setupLocalTransliteration();
                listView.setItems(FXCollections.observableArrayList(newItems));

            }
            if (selectedIndex > -1 && selectedIndex < masterList.size()) {
                setValue(masterList.get(selectedIndex));
                return;
            }

            if (deferredSelectedItem != null) {
                if (masterList.contains(deferredSelectedItem)) {
                    setValue(deferredSelectedItem);
                }
                deferredSelectedItem = null;
                return;
            }

            if (selectedItem != null) {
                setValue(selectedItem);
            }
        }
    }

    public ObservableList<T> getItems() {
        return this.masterList;
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO CLEAR SELECTED ITEM
     */
    public void clearSelection() {
        selectedItem = null;
        suppressFilter = true;
        clear();
        currentWord.setLength(0);
        previousGujaratiLength = 0;
        suppressFilter = false;
        hidePopup();
    }


    //                                      ~~~ FAKE COMBOX METHOD ~~~
//----------------------------------------------------------------------------------------------------------------------
    public FakeSelectionModel getSelectionModel() {
        return new FakeSelectionModel();
    }

    public class FakeSelectionModel {


        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 03/06/2026    Nimit             1.0.0     IT WILL ADD ITEMS IN THE MASTER LIST WHATEVER PASSED.
         * ("A", "B", "C")
         * 08/06/2026    Nimit             1.0.1     Solved bug - It will select pre-selected value.
         */
        @SafeVarargs
        public final void addAll(T... items) {
            if (items != null && items.length > 0) {
                masterList.addAll(items);

                if (masterList.size() == items.length) {
                    String sampleText = textExtractor.apply(items[0]);
                    detectLanguage(sampleText);
                    setupLocalTransliteration();
                }

                if (selectedIndex > -1 && selectedIndex < masterList.size()) {
                    setValue(masterList.get(selectedIndex));
                    return;
                }

                if (deferredSelectedItem != null) {
                    if (masterList.contains(deferredSelectedItem)) {
                        setValue(deferredSelectedItem);
                    }
                    deferredSelectedItem = null;
                    return;
                }

                if (selectedItem != null) {
                    setValue(selectedItem);
                }
            }
        }

        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 03/06/2026    Nimit             1.0.0     IT WILL ADD LIST OF ITEMS IN THE MASTER LIST.
         * 08/06/2026    Nimit             1.0.1     Solved bug - It will select pre-selected value.
         */
        public void addAll(List<T> items) {
            if (items != null && !items.isEmpty()) {
                masterList.addAll(items);
                if (masterList.size() == items.size()) {
                    String sampleText = textExtractor.apply(items.get(0));
                    detectLanguage(sampleText);
                    setupLocalTransliteration();
                }

                if (selectedIndex > -1 && selectedIndex < masterList.size()) {
                    setValue(masterList.get(selectedIndex));
                    return;
                }

                if (deferredSelectedItem != null) {
                    if (masterList.contains(deferredSelectedItem)) {
                        setValue(deferredSelectedItem);
                    }
                    deferredSelectedItem = null;
                    return;
                }

                if (selectedItem != null) {
                    setValue(selectedItem);
                }
            }
        }

        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 03/06/2026    Nimit             1.0.0     IT RETURN WHATEVER INDEX OF THE SELECTED ITEM.
         * 08/06/2026    Nimit             1.0.1     Solved bug - Return direct selected index.
         */
        public int getSelectedIndex() {
            return selectedIndex;
        }


        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 03/06/2026    Nimit             1.0.0     IT RETURN SELECTED ITEM.
         */
        public T getSelectedItem() {
            return getValue();
        }

        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 03/06/2026    Nimit             1.0.0     IT SELECT THE PASSED PARAM ITEM.
         * 03/06/2026    Nimit             1.0.1     Solved bug - Get selected index of the selected item.
         */
        public void select(T item) {
            if (item == null) {
                clearSelection();
                deferredSelectedItem = null;
                return;
            }
            if (masterList != null) {
                selectedIndex = masterList.indexOf(item);
            }
            listView.getSelectionModel().select(item);
            deferredSelectedItem = item;
            selectedItem = item;
            setValue(item);
        }

        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 03/06/2026    Nimit             1.0.0     IT SELECT ITEM ON THE PASSED PARAM INDEX.
         * 08/06/2026    Nimit             1.0.1     Solved bug - Get item which selected using index.
         */
        public void select(Integer pos) {
            selectedIndex = pos;
            if (masterList == null || masterList.isEmpty())
                return;
            T item = masterList.get(pos);
            listView.getSelectionModel().select(item);
            deferredSelectedItem = item;
            selectedItem = item;
            setValue(item);
        }

        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 03/06/2026    Nimit             1.0.0     IT CLEAR/ UNSELECT SELECTED ITEM.
         */
        public void clearSelection() {
            selectedItem = null;
            suppressFilter = true;
            clear();
            currentWord.setLength(0);
            previousGujaratiLength = 0;
            suppressFilter = false;
            hidePopup();
        }

    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO DETECT LANGUAGE OF THE ITEM SO THAT SET IN WHICH LANGUAGE TO SEARCH
     */
    public void detectLanguage(String text) {
        if (text == null || text.trim().isEmpty()) {
            language = "English";
        }

        // Strip out numbers, spaces, and standard punctuation to avoid false positives
        String cleanText = text.replaceAll("[\\d\\s\\p{Punct}]", "");
        if (cleanText.isEmpty() || cleanText.isBlank()) {
            language = "English";
            return;
        }

        // Inspect the very first clean alphabetic character
        char firstChar = cleanText.charAt(0);
        Character.UnicodeBlock block = Character.UnicodeBlock.of(firstChar);


        if (block == Character.UnicodeBlock.GUJARATI) {
            language = "Gujarati";
        } else if (block == Character.UnicodeBlock.DEVANAGARI) {
            language = "Hindi"; // Covers Hindi, Marathi, Nepali, etc.
        } else if (block == Character.UnicodeBlock.BENGALI) {
            language = "Bengali";
        } else if (block == Character.UnicodeBlock.BASIC_LATIN) {
            language = "English";
        } else {
            language = "English";
        }
    }


//                                      ~~~ TRANSLATION PROCESS ~~~
//----------------------------------------------------------------------------------------------------------------------

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0      IF OTHER LANGUAGE OF TEXTFIELD THEN THIS METHOD WILL HELP
     * TO TRANSLATE THE WRITTEN ENGLISH WORD
     */
    private void textFieldLocal() {
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                event.consume();
                handleBackspace();
            }
        });

        addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String ch = event.getCharacter();
            if (ch.isEmpty() || ch.charAt(0) < 32) return;

            event.consume();

            if (ch.equals(" ")) {
                flushCurrentWord();
                insertText(getCaretPosition(), " ");
            } else {
                currentWord.append(ch);
                String existing = getText();
                int caret = getCaretPosition();

                if (existing != null && existing.length() >= previousGujaratiLength) {
                    setText(existing.substring(0, existing.length() - previousGujaratiLength));
                }

                String preview = transliteratePreservingDigits(currentWord.toString());
                appendText(preview);
                previousGujaratiLength = preview.length();
                positionCaret(getText().length());
            }
        });
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0     IT TAKE CHAR COMBINE WITH OTHER WORD, PREPROCESS IT, THEN TRANSLATE THE WORD
     */
    private void flushCurrentWord() {
        if (currentWord.length() > 0) {
            String existing = getText();
            if (existing.length() >= previousGujaratiLength) {
                setText(existing.substring(0, existing.length() - previousGujaratiLength));
            }
            String finalWord = transliterator.transliterate(preprocess(currentWord.toString()));
            appendText(finalWord);
            positionCaret(getText().length());
            currentWord.setLength(0);
            previousGujaratiLength = 0;
        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0       TO MANAGE BACKSPACE EVENT, BECAUSE TRANSLATION NEED TO PROPERLY CHECK AND
     * THEN REMOVE LAST CHAR
     */
    private void handleBackspace() {
        if (currentWord.length() > 0) {
            currentWord.deleteCharAt(currentWord.length() - 1);
            String existing = getText();
            if (existing != null && existing.length() >= previousGujaratiLength) {
                setText(existing.substring(0, existing.length() - previousGujaratiLength));
            }

            if (currentWord.length() > 0) {
                String preview = transliteratePreservingDigits(currentWord.toString());
                appendText(preview);
                previousGujaratiLength = preview.length();
            } else {
                previousGujaratiLength = 0;
            }
            positionCaret(getText().length());
        } else {
            if (getText().length() > 0) {
                setText(getText().substring(0, getText().length() - 1));
                positionCaret(getText().length());
            }
        }
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0      OTHER LANGUAGE REQUIRED SPECIAL ENGLISH CHAR TO GET SPECIFIC LANGUAGE WORD
     * SO THIS METHOD WILL PREPROCESS IT.
     */
    private String preprocess(String text) {
        return text
                .replace("aa", "ā")
                .replace("ii", "ī")
                .replace("ee", "ī")
                .replace("uu", "ū")
                .replace("oo", "ū")
                .replace("tt", "ṭ")
                .replace("dd", "ḍ")
                .replace("nn", "ṇ")
                .replace("ll", "ḷ")
                .replace("sh", "ś")
                .replace("shh", "ṣ")
                .replace("chh", "ch")
                .replace("rr", "ṛ");
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0      THIS METHOD WILL SET TEXTFIELD LANGUAGE.
     */
    private Transliterator createTransliterator() {
        String targetScript;

        switch (language.substring(0, 2).toLowerCase()) {
            case "hi":
            case "mr":
            case "ne":
                targetScript = "Devanagari";
                break;
            case "gu":
                targetScript = "Gujarati";
                break;
            case "bn":
                targetScript = "Bengali";
                break;
            case "ta":
                targetScript = "Tamil";
                break;
            case "te":
                targetScript = "Telugu";
                break;
            case "kn":
                targetScript = "Kannada";
                break;
            case "ml":
                targetScript = "Malayalam";
                break;
            case "pa":
                targetScript = "Gurmukhi";
                break;
            case "or":
                targetScript = "Oriya";
                break;
            default:
                targetScript = "Gujarati";
        }

        return Transliterator.getInstance("Latin-" + targetScript);
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0      METHOD HELPS TO SKIP DIGITS TO TRANSLATE
     * 24/06/2026    Nimit             1.0.1      Now number will also translate to local language.
     */
    private String transliteratePreservingDigits(String input) {
        String preprocessed = preprocess(input);
        StringBuilder result = new StringBuilder();
        StringBuilder segment = new StringBuilder();

        for (int i = 0; i < preprocessed.length(); i++) {
            char c = preprocessed.charAt(i);
//            if (Character.isDigit(c) || c == '.') {
//                if (segment.length() > 0) {
//                    result.append(transliterator.transliterate(segment.toString()));
//                    segment.setLength(0);
//                }
//                result.append(c);
//            } else {
            segment.append(c);
//            }
        }
        if (segment.length() > 0) {
            result.append(transliterator.transliterate(segment.toString()));
        }
        return result.toString();
    }
    //                                      ~~~ TRANSLATION PROCESS ~~~
//----------------------------------------------------------------------------------------------------------------------


    // ── CUSTOM CELL RENDERER ────────────────────────────────────────────────
    private class DynamicCell extends ListCell<T> {
        private final Label mainLabel = new Label();
        private final Label subLabel = new Label();
        private final VBox cellLayout = new VBox(mainLabel, subLabel);

        DynamicCell() {
            mainLabel.setFont(Font.font("System", javafx.scene.text.FontWeight.BOLD, 13));
            mainLabel.setTextFill(Color.web("#2c3e50"));
            subLabel.setFont(Font.font("System", 11));
            subLabel.setTextFill(Color.web("#7f8c8d"));

            hoverProperty().addListener((obs, w, isHovered) -> updateBackground());
            selectedProperty().addListener((obs, w, isSelected) -> updateBackground());
        }

        private void updateBackground() {
            setStyle(isSelected() ? "-fx-background-color: #d6eaf8;" : "-fx-background-color: transparent;");
        }

        /**
         * Change History:
         * Date          Author           Version     Description
         * -----------   --------------   ---------   ---------------------------------
         * 24/06/2026    Nimit             1.0.1      Now number will also translate to local language.
         */
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setStyle("-fx-background-color: transparent;");
            } else {
                String rawMainText = textExtractor.apply(item);
                String rawSubText = (subTextExtractor != null) ? subTextExtractor.apply(item) : null;
                mainLabel.setText(com.eipl.amcs.utils.FormatterFactory.convertEnglishToLocalizedDigits(rawMainText));
                if (rawSubText != null && !rawSubText.trim().isEmpty()) {
                    subLabel.setText(com.eipl.amcs.utils.FormatterFactory.convertEnglishToLocalizedDigits(rawSubText));
                    subLabel.setVisible(true);
                    subLabel.setManaged(true);
                    cellLayout.setSpacing(2);
                    cellLayout.setPadding(new Insets(6, 12, 6, 12));
                } else {
                    subLabel.setVisible(false);
                    subLabel.setManaged(false);
                    cellLayout.setSpacing(0);
                    cellLayout.setPadding(new Insets(8, 12, 8, 12));
                }
                setGraphic(cellLayout);
                updateBackground();
            }
        }
    }

    // ── COMBOBOX COMPATIBILITY LAYER (PROPERTY BINDINGS) ──────────────────

    /**
     * Mimics ComboBox.valueProperty() to allow direct property manipulation and binding.
     */
    public javafx.beans.property.ObjectProperty<T> valueProperty() {
        javafx.beans.property.ObjectProperty<T> proxyProperty = new javafx.beans.property.SimpleObjectProperty<>(getValue());

        proxyProperty.addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                clearSelection();
            } else {
                setValue(newVal);
            }
        });

        return proxyProperty;
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 03/06/2026    Nimit             1.0.0      TO GET TEXT OF SELECTED VALUE
     */
    public String getFinalText() {
        T selected = getValue();
        String editorText = getText();
        if (selected != null && selected.toString().equals(editorText)) {
            return selected.toString();
        }
        return editorText != null ? editorText : "";
    }

    public void setOpenPopupOnFocus(boolean openPopup) {
        this.openPopup = openPopup;
    }
}