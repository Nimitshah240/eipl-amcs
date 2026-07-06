package com.eipl.amcs.reportengine.popup;

import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.model.RptReportParameter;
import com.eipl.amcs.reportengine.service.LookupService;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParameterControlFactory {

    private final LookupService lookupService;

    public Node createControl(RptReportParameter rp) {
        RptParameterMaster pm = rp.getParameterMaster();
        switch (pm.getControlType()) {

            case "DATE_PICKER":

                DatePicker picker = new DatePicker();

                picker.setPrefWidth(rp.getWidth());

                return picker;

            case "AUTO_SEARCH":
                AutoSearchTextField<T> control = new AutoSearchTextField<>();

                control.setPrefWidth(rp.getWidth());

                if (pm.getLookup() != null) {
                    control.getSelectionModel().addAll(lookupService.load(pm.getLookup().getLookupId()));
                }


                return control;

            case "TEXT":

                TextField txt = new TextField();

                txt.setPrefWidth(rp.getWidth());

                return txt;

            default:

                return new TextField();
        }

    }

}