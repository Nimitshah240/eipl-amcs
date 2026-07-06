package com.eipl.amcs.reportengine.controller;

import com.eipl.amcs.reportengine.dto.ParameterForm;
import com.eipl.amcs.reportengine.popup.ParameterFormBuilder;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ParameterPopup {

    private final ParameterFormBuilder parameterFormBuilder;

    public Map<String, Object> show(Long reportId) {

        ParameterForm form = parameterFormBuilder.build(reportId);

        Stage stage = new Stage();
        stage.setTitle("Report Parameters");

        Scene scene = new Scene(form.getRoot());

        stage.setScene(scene);
        stage.showAndWait();

        return form.getValues();
    }
}