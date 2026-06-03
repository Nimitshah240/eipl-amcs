package com.eipl.amcs.controls;

import com.eipl.amcs.MainApp;
import com.ibm.icu.text.Transliterator;
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
    private final Function<T, String> subTextExtractor;

    private Consumer<T> onItemSelected;
    private final Popup popup;
    private final ListView<T> listView;

    private boolean suppressFilter = false;
    private T selectedItem = null;

    private static final int MAX_ROWS = 6;
    private Transliterator transliterator;
    private final StringBuilder currentWord = new StringBuilder();
    private int previousGujaratiLength = 0;

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
        this.setPrefHeight(38);
        this.getStyleClass().add("auto-search-text-field");

        // 1. Safely resolve the absolute URL path to your asset file
        var imageResource = getClass().getResource("/com/eipl/amcs/view/images/search.png");
        String imagePathString = "";

        if (imageResource != null) {
            imagePathString = imageResource.toExternalForm();
        }

        // 2. Apply the clean CSS styling block
        this.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 6 36 6 12; " + // 36px right padding keeps typed text from overlapping your icon
                        "-fx-background-color: white; " +
                        "-fx-border-color: #aab7c4; " +
                        "-fx-border-radius: 6; " +
                        "-fx-background-radius: 6; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 4, 0, 0, 1); " +
                        "-fx-background-image: url('" + imagePathString + "'); " + // Loads your custom downloaded icon
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-position: right 12px center; " + // Locks icon 12px from the right edge
                        "-fx-background-size: 16px 16px;" // Since your icon is slightly big, this forces it to a clean 16x16px size
        );
    }

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
    }

    public void setupLocalTransliteration() {
        if (MainApp.getLocale().equalsIgnoreCase("en")) {
            return;
        }
        this.transliterator = createTransliterator();
        textFieldLocal();
    }

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

    private void wireKeyNavigation() {
        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (!popup.isShowing()) return;

            int size = listView.getItems().size();
            if (size == 0) return;

            int current = listView.getSelectionModel().getSelectedIndex();

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
                default:
                    break;
            }
        });
    }

    private void wireMouseSelection() {
        listView.setOnMouseClicked(event -> {
            T clicked = listView.getSelectionModel().getSelectedItem();
            if (clicked != null) {
                selectItem(clicked);
            }
        });
    }

    private void wireFocusLost() {
        focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) hidePopup();
        });
    }

    private void filterAndShow(String query) {
        String lower = query.toLowerCase();

        List<T> results = masterList.stream()
                .filter(item -> {
                    String text = textExtractor.apply(item);
                    return text != null && text.toLowerCase().contains(lower);
                })
                .limit(50) // Performance optimization guardrail
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            hidePopup();
            return;
        }

        listView.setItems(FXCollections.observableArrayList(results));
        listView.getSelectionModel().selectFirst();

        int staticCellHeight = subTextExtractor != null ? 60 : 40;
        int visibleRows = Math.min(results.size(), MAX_ROWS);
        listView.setPrefHeight((visibleRows * staticCellHeight) + 8);
        // ── CHANGED: Remove fixed width constraint ──
        // Do not call: listView.setPrefWidth(this.getWidth());
        // Instead, clear any previous explicit preferred width so it wraps text sizes naturally:
        listView.setPrefWidth(ListView.USE_COMPUTED_SIZE);
        showPopupBelow();
    }

    private void showPopupBelow() {
        if (getScene() == null || getScene().getWindow() == null) return;
        var bounds = localToScreen(getBoundsInLocal());
        if (bounds == null) return;

        listView.setPrefWidth(ListView.USE_COMPUTED_SIZE);
        if (!popup.isShowing()) {
            popup.show(getScene().getWindow(), bounds.getMinX(), bounds.getMaxY() + 2);
        }
    }

    private void hidePopup() {
        popup.hide();
    }

    private void selectItem(T item) {
        selectedItem = item;
        suppressFilter = true;
        String displayStr = textExtractor.apply(item);
        setText(displayStr);
        positionCaret(displayStr.length());
        suppressFilter = false;
        hidePopup();
        if (onItemSelected != null) {
            onItemSelected.accept(item);
        }
    }

    public void setOnUserSelected(Consumer<T> callback) {
        this.onItemSelected = callback;
    }

    public T getValue() {
        return selectedItem;
    }

    public void setValue(T item) {
        if (item == null) {
            clearSelection();
            return;
        }
        this.selectedItem = item;
        this.suppressFilter = true;
        String displayStr = textExtractor.apply(item);
        this.setText(displayStr);
        this.positionCaret(displayStr.length());
        this.suppressFilter = false;
        hidePopup();
    }

    public void setItems(ObservableList<T> newItems) {
        clearSelection();
        if (newItems == null) {
            this.masterList.clear();
        } else {
            this.masterList.setAll(newItems);
        }
    }

    public void setItems(List<T> newItems) {
        clearSelection();
        if (newItems == null) {
            this.masterList.clear();
        } else {
            this.masterList.setAll(newItems);
        }
    }

    public ObservableList<T> getItems() {
        return this.masterList;
    }

    public void clearSelection() {
        selectedItem = null;
        suppressFilter = true;
        clear();
        currentWord.setLength(0);
        previousGujaratiLength = 0;
        suppressFilter = false;
        hidePopup();
    }

    // ── TRANSLITERATION ENGINE ENGINE ───────────────────────────────────────

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

    private Transliterator createTransliterator() {
        String language = MainApp.getLocale();
        String targetScript;

        switch (language) {
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

    private String transliteratePreservingDigits(String input) {
        String preprocessed = preprocess(input);
        StringBuilder result = new StringBuilder();
        StringBuilder segment = new StringBuilder();

        for (int i = 0; i < preprocessed.length(); i++) {
            char c = preprocessed.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                if (segment.length() > 0) {
                    result.append(transliterator.transliterate(segment.toString()));
                    segment.setLength(0);
                }
                result.append(c);
            } else {
                segment.append(c);
            }
        }
        if (segment.length() > 0) {
            result.append(transliterator.transliterate(segment.toString()));
        }
        return result.toString();
    }

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

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setStyle("-fx-background-color: transparent;");
            } else {
                mainLabel.setText(textExtractor.apply(item));
                if (subTextExtractor != null) {
                    subLabel.setText(subTextExtractor.apply(item));
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
    // ── COMBOBOX COMPATIBILITY LAYER ──────────────────────────────────────

    /**
     * Fake selection model to allow drop-in replacement for standard ComboBox code.
     */
    public FakeSelectionModel getSelectionModel() {
        return new FakeSelectionModel();
    }

    public class FakeSelectionModel {
        /**
         * Mimics ComboBox.getSelectionModel().getSelectedItem()
         *
         * @return The currently selected object of type T, or null.
         */
        public T getSelectedItem() {
            return getValue();
        }

        /**
         * Mimics ComboBox.getSelectionModel().select(item)
         * Programmatically sets and pre-selects a value safely.
         */
        public void select(T item) {
            setValue(item);
        }

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

    // ── COMBOBOX COMPATIBILITY LAYER (PROPERTY BINDINGS) ──────────────────

    /**
     * Mimics ComboBox.valueProperty() to allow direct property manipulation and binding.
     */
    public javafx.beans.property.ObjectProperty<T> valueProperty() {
        // Create a proxy property that syncs directly with our internal selection state
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
}