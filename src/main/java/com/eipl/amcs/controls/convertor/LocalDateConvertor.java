package com.eipl.amcs.controls.convertor;

import com.eipl.amcs.utils.AppConstant;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.eipl.amcs.utils.FormatterFactory.convertLocalizedToEnglishDigits;

public class LocalDateConvertor extends StringConverter<LocalDate> {
    @Override
    public String toString(LocalDate object) {
        return object != null ? object.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
    }

    /**
     * Change History:
     * Date          Author           Version     Description
     * -----------   --------------   ---------   ---------------------------------
     * 24/06/2026    Nimit             1.0.1      Added line if using this convertor and text is local then convert into english and then process.
     */
    @Override
    public LocalDate fromString(String string) {

        String text = string.trim();
        string = convertLocalizedToEnglishDigits(text);

        if (string == null || string.isEmpty())
            return null;

        try {
            return LocalDate.parse(string, AppConstant.DATE_FORMATTER);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDate.parse(string, AppConstant.Formatter1);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDate.parse(string, AppConstant.Formatter2);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDate.parse(string, AppConstant.Formatter3);
        } catch (DateTimeParseException ignored) {
        }
        return LocalDate.parse(string, AppConstant.DATE_FORMATTER);
    }
}
