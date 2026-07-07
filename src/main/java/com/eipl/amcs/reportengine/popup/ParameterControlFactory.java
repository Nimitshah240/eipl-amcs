package com.eipl.amcs.reportengine.popup;

import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.model.RptReportParameter;
import com.eipl.amcs.reportengine.service.LookupService;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ParameterControlFactory {

    private final LookupService lookupService;

    public Node createControl(RptReportParameter rp) {
        RptParameterMaster pm = rp.getParameterMaster();
        switch (pm.getDataType()) {

            case "DATE_PICKER":

                DatePicker picker = new E_DatePicker();
                if (rp.getDefaultValue() != null) {
                    if ("TODAY".equalsIgnoreCase(rp.getDefaultValue())) {
                        picker.setValue(LocalDate.now());
                    } else {
                        try {
                            picker.setValue(LocalDate.parse(rp.getDefaultValue()));
                        } catch (Exception e) {
                            picker.setValue(LocalDate.now());
                        }
                    }
                }
                return picker;

            case "AUTO_SEARCH":
                AutoSearchTextField<T> control = new AutoSearchTextField<>();

                if (pm.getLookup() != null) {
                    control.getItems().setAll(
                            lookupService.load(pm.getLookup().getLookupCode())
                    );
                }
                if (rp.getDefaultValue() != null) {
                    try {
                        int index = Integer.parseInt(rp.getDefaultValue());
                        control.getSelectionModel().select(index);
                    } catch (NumberFormatException e) {
                        control.getSelectionModel().select(0);
                    }
                }

                return control;

            case "TEXT":

                E_TextField txt = new E_TextField();
                if (rp.getDefaultValue() != null) {
                    txt.setText(rp.getDefaultValue()); // text
                }
                return txt;
            case "Numeric":

                E_NumericField numericField = new E_NumericField();
                if (rp.getDefaultValue() != null) {
                    numericField.setText(rp.getDefaultValue()); // text
                }
                return numericField;
            default:

                return new TextField();
        }

    }

}