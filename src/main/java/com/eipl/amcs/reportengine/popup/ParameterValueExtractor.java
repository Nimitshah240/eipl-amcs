package com.eipl.amcs.reportengine.popup;

import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.reportengine.dto.ParameterForm;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ParameterValueExtractor {

    public Map<String, Object> extract(ParameterForm form) {

        Map<String, Object> values = new LinkedHashMap<>();

        for (Map.Entry<String, Control> entry : form.getControls().entrySet()) {

            String parameterCode = entry.getKey();
            Control control = entry.getValue();

            Object value = extractValue(control);

            values.put(parameterCode, value);
        }

        return values;
    }

    private Object extractValue(Control control) {

        if (control instanceof AutoSearchTextField<?>) {
            return ((AutoSearchTextField<?>) control).getValue();
        }

        if (control instanceof ComboBox<?>) {
            return ((ComboBox<?>) control).getValue();
        }

        if (control instanceof DatePicker) {
            return ((DatePicker) control).getValue();
        }

        if (control instanceof TextField) {
            return ((TextField) control).getText();
        }

        if (control instanceof TextArea) {
            return ((TextArea) control).getText();
        }

        return null;
    }

}