package com.eipl.amcs.utils;

import com.eipl.amcs.MainApp;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

import static com.eipl.amcs.MainApp.getCurrentLocale;
import static com.eipl.amcs.utils.AppConstant.DATE_FORMATTER_LOCALE;
import static com.eipl.amcs.utils.AppConstant.DATE_TIME_FORMATTER_LOCALE;

public class FormatterFactory {
    public static final String LOCALE_GU = "gu";
    public static final String LOCALE_HI = "hi";
    public static final String LOCALE_MR = "mr";
    public static final Locale currentLocale = getCurrentLocale();

    public static TextFormatter<Number> createNumericFormatter() {
        NumberFormat numberFormat = NumberFormat.getInstance(currentLocale);
        numberFormat.setGroupingUsed(false);
        StringConverter<Number> localizedConverter = new StringConverter<>() {
            @Override
            public String toString(Number number) {
                if (number == null)
                    return "";
                String formatted = numberFormat.format(number);
                return convertEnglishToLocalizedDigits(formatted);
            }

            @Override
            public Number fromString(String string) {
                try {
                    if (string == null || string.trim().isEmpty())
                        return 0;

                    String cleanInput = convertEnglishToLocalizedDigits(string.trim());
                    return numberFormat.parse(cleanInput);

                } catch (ParseException e) {
                    return 0;
                }
            }
        };

        return new TextFormatter<>(localizedConverter, 0, change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String typedText = change.getText();
            if (typedText.isEmpty()) {
                return change;
            }
            if (!typedText.matches("[0-9.૦-૯०-९]*")) {
                return null;
            }
            change.setText(convertEnglishToLocalizedDigits(typedText));
            return change;
        });
    }

    public static String formatNumber(Number number) {
        if (number == null)
            return "";

        NumberFormat numberFormat = NumberFormat.getInstance(currentLocale);
        numberFormat.setGroupingUsed(false);
        String formatted = numberFormat.format(number);
        return convertEnglishToLocalizedDigits(formatted);
    }

    public static String formatNumber(String number) {
        if (number == null)
            return "";

        if (number.matches(".*[a-zA-Z/\\-_].*")) {
            return convertEnglishToLocalizedDigits(number);
        }
        NumberFormat numberFormat = NumberFormat.getInstance(currentLocale);
        numberFormat.setGroupingUsed(false);
        String formatted = null;
        try {
            formatted = numberFormat.format(numberFormat.parse(number));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertEnglishToLocalizedDigits(formatted);
    }

    public static String formatDate(LocalDate date) {
        if (date == null)
            return "";
        String formattedDate = date.format(DATE_FORMATTER_LOCALE);
        return convertEnglishToLocalizedDigits(Objects.requireNonNull(formattedDate));
    }

    public static String formatDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty())
            return "";
        try {
            return convertEnglishToLocalizedDigits(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return "";
        String formattedDateTime = dateTime.format(DATE_TIME_FORMATTER_LOCALE);
        return convertEnglishToLocalizedDigits(Objects.requireNonNull(formattedDateTime));
    }

    /**
     * Converts English digits to Gujarati or Devanagari (Hindi/Marathi) scripts dynamically
     */
    public static String convertEnglishToLocalizedDigits(String input) {
        if (input == null) return "";

        int offset = 0;
        String langCode = MainApp.getLocale();
        if (LOCALE_GU.equals(langCode)) {
            offset = 0x0AE6 - '0';
        } else if (LOCALE_HI.equals(langCode) || LOCALE_MR.equals(langCode)) {
            offset = 0x0966 - '0';
        } else {
            return input;
        }

        StringBuilder builder = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                builder.append((char) (ch + offset));
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    /**
     * Reverts localized script characters (Gujarati/Devanagari) back to standard
     * English numbers safely before parsing or validating.
     */
    public static String convertLocalizedToEnglishDigits(String input) {
        if (input == null) return "";
        StringBuilder builder = new StringBuilder();
        for (char ch : input.toCharArray()) {
            if ("gu".equals(MainApp.locale) && ch >= '૦' && ch <= '૯') {
                builder.append((char) (ch - (0x0AE6 - '0')));
            } else if (("hi".equals(MainApp.locale) || "mr".equals(MainApp.locale)) && ch >= '०' && ch <= '९') {
                builder.append((char) (ch - (0x0966 - '0')));
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }
}