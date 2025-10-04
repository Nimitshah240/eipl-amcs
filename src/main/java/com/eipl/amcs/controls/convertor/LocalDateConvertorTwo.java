package com.eipl.amcs.controls.convertor;

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConvertorTwo extends StringConverter<LocalDate> {
    private final DateTimeFormatter fastFormatter1 = DateTimeFormatter.ofPattern("ddMMyyyy");
    private final DateTimeFormatter fastFormatter2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @Override
    public String toString(LocalDate object) {
        return object.format(defaultFormatter);
    }

    @Override
    public LocalDate fromString(String string) {
        try {
            return LocalDate.parse(string, fastFormatter1);
        } catch (DateTimeParseException ignored) {
        }
        try {
            return LocalDate.parse(string, fastFormatter2);
        } catch (DateTimeParseException ignored) {
        }
        return LocalDate.parse(string, defaultFormatter);
    }
}
