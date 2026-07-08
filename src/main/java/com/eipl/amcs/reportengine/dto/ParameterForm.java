package com.eipl.amcs.reportengine.dto;

import com.eipl.amcs.controls.AutoSearchTextField;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;

public class ParameterForm {

    private BorderPane root;

    private Map<String, Control> controls = new HashMap<>();

    public BorderPane getRoot() {
        return root;
    }

    public void setRoot(BorderPane root) {
        this.root = root;
    }

    public Map<String, Control> getControls() {
        return controls;
    }

    public void setControls(Map<String, Control> controls) {
        this.controls = controls;
    }

    public void addControl(String parameterName, Control control) {
        controls.put(parameterName, control);
    }

    public Map<String, Object> getValues() {

        Map<String, Object> values = new HashMap<>();

        controls.forEach((name, control) -> {

            if (control instanceof AutoSearchTextField<?>) {

                values.put(name, ((AutoSearchTextField<?>) control).getSelectionModel().getSelectedItem());

            } else if (control instanceof DatePicker) {

                values.put(name, ((DatePicker) control).getValue());

            } else if (control instanceof TextField) {
                values.put(name, ((TextField) control).getText());
            }
        });

        return values;
    }
}