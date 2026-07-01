package com.eipl.amcs.utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;

import static com.eipl.amcs.utils.AppConstant.DIGIT_PATTERN;
import static com.eipl.amcs.utils.FormatterFactory.*;

public class TableLocalizationUtil {

    /**
     * Automatically scans and localizes all table columns cleanly.
     * It dynamically attaches a smart formatter to every single column,
     * removing any dependency on explicit column header names or texts.
     *
     * @param tableView The JavaFX TableView instance to be localized
     */
    @SuppressWarnings("unchecked")
    public static void localizeTable(TableView<?> tableView) {
        if (tableView == null) {
            return;
        }

        for (TableColumn<?, ?> column : tableView.getColumns()) {
            // Apply our smart localized cell factory safely across all column types
            ((TableColumn<Object, Object>) column).setCellFactory(c -> createSmartLocalizedCell());
        }
    }

    /**
     * A robust TableCell factory that dynamically reads the runtime data type of the underlying
     * cell item and applies the correct Gujarati, Hindi, Marathi, or English formatting rules.
     * <p>
     * This eliminates silent ClassCastExceptions when processing mix-matched property fields.
     */
    public static <S> TableCell<S, Object> createSmartLocalizedCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                try {
                    if (item instanceof LocalDate) {
                        setText(formatDate((LocalDate) item));
                    } else if (item instanceof LocalDateTime) {
                        setText(formatDateTime((LocalDateTime) item));
                    } else if (item instanceof Number) {
                        setText(formatNumber((Number) item));
                    } else {
                        String stringValue = item.toString().trim();
                        if (stringValue.matches("(?=.*[a-zA-Z])(?=.*\\d).*") || stringValue.contains(" ")) {
                            setText(convertEnglishToLocalizedDigits(stringValue));
                        } else if (stringValue.startsWith("0") && stringValue.length() > 1) {
                            setText(convertEnglishToLocalizedDigits(stringValue));
                        } else {
                            Matcher matcher = DIGIT_PATTERN.matcher(stringValue);
                            if (matcher.find()) {
                                setText(formatNumber(stringValue));
                            } else {
                                setText(stringValue);
                            }
                        }
                    }
                } catch (Exception e) {
                    setText(item.toString());
                    e.printStackTrace();
                }
            }
        };
    }
}