package com.eipl.amcs.reportengine.popup;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.reportengine.dto.ParameterForm;
import com.eipl.amcs.reportengine.dto.ReportRequest;
import com.eipl.amcs.reportengine.dto.ReportResult;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.model.RptReportParameter;
import com.eipl.amcs.reportengine.service.ReportExecutionService;
import com.eipl.amcs.reportengine.service.ReportParameterService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ParameterFormBuilder {

    private final ReportParameterService reportParameterService;
    private final ParameterControlFactory parameterControlFactory;
    private final ParameterValueExtractor parameterValueExtractor;
//    private final ReportParameterService reportParameterService;

    private final ReportExecutionService reportExecutionService;

    public ParameterForm build(Long reportId) {

        ParameterForm form = new ParameterForm();

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPrefWidth(250);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPrefWidth(250);
        grid.getColumnConstraints().addAll(c1, c2);

        List<RptReportParameter> parameters = reportParameterService.getParameters(reportId);

        for (RptReportParameter parameter : parameters) {
            VBox field = createField(parameter, form);
            grid.add(field, parameter.getColumnNo() - 1, parameter.getRowNo() - 1);
        }

        BorderPane root = new BorderPane();
        root.setCenter(grid);
        HBox buttonBar = new HBox(10);

        E_Button btnGenerate = new E_Button();
        btnGenerate.setText(MainApp.getBundle().getString("generate"));
        btnGenerate.setOnAction(e -> {

            Map<String, Object> values = parameterValueExtractor.extract(form);
            reportParameterService.saveDefaults(reportId, values);
            ReportRequest request = new ReportRequest();
            request.setReportId(reportId);
            request.setParameters(values);
            ReportResult result = reportExecutionService.execute(request);

            ((Stage) btnGenerate.getScene().getWindow()).close();

        });
        E_Button btnCancel = new E_Button();
        btnCancel.setText(MainApp.getBundle().getString("cancel"));
        buttonBar.getChildren().addAll(btnGenerate, btnCancel);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        root.setBottom(buttonBar);
        form.setRoot(root);

        btnCancel.setOnAction(e -> {
            ((Stage) btnCancel.getScene().getWindow()).close();
        });

        return form;
    }

    private VBox createField(RptReportParameter rp, ParameterForm form) {
        RptParameterMaster parameter = rp.getParameterMaster();
        Label label = new Label(rp.getDisplayName());
        label.setStyle("-fx-font-size:13px;");
        Control control = (Control) parameterControlFactory.createControl(rp);
//        control.setPrefWidth(rp.getWidth()
        form.addControl(rp.getParameterMaster().getParameterMasterCode(), control);
        VBox box = new VBox(5);
        box.getChildren().addAll(label, control);
        return box;
    }
}