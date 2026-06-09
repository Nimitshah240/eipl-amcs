package com.eipl.amcs.master.org.controller;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.MyInitialization;
import com.eipl.amcs.base.PopupCallback;
import com.eipl.amcs.controls.CheckComboBox;
import com.eipl.amcs.controls.E_Button;
import com.eipl.amcs.controls.alert.ErrorAlert;
import com.eipl.amcs.controls.alert.InformationAlert;
import com.eipl.amcs.controls.alert.MyAlert;
import com.eipl.amcs.exception.error.ApiError;
import com.eipl.amcs.exception.error.ApiValidationError;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.task.MilkTypeLoadTask;
import com.eipl.amcs.master.org.convertor.SocietyConvertor;
import com.eipl.amcs.master.org.dto.DockMilkTypeDto;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.task.DockNumberLoadTask;
import com.eipl.amcs.master.org.task.DockSaveTask;
import com.eipl.amcs.master.org.task.SocietyLoadTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class DockAddEditController implements MyInitialization {
    @FXML
    private StackPane root;
    @FXML
    private E_Button btnClose, btnSaveUpdate;
    @FXML
    private ComboBox<Society> cboxSociety;
    @FXML
    private TextField txtDockNo;
    @FXML
    private GridPane grid;
    @FXML
    private CheckBox chkIsDefault, chkIsActive;

    private CheckComboBox<MilkType> cboxMilkType;

    private Stage stage;
    private PopupCallback callback;
    private ResourceBundle resourceBundle;
    private StringBuilder errorMsg = null;
    private DockMilkTypeDto dto = null;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }

    public void setDockMilkTypeDto(DockMilkTypeDto dto) {
        if (dto != null) {
            this.dto = dto;
            btnSaveUpdate.setText(resourceBundle.getString("update"));
        }
        loadSociety();
        loadMilkTypes();
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;

        setupComboBox();

        cboxSociety.setOnAction(e -> {
            if (dto == null)
                getNextDockNumber(cboxSociety.getValue());
        });
        btnClose.setOnAction(e -> this.stage.close());
        btnSaveUpdate.setOnAction(e -> validateAndSave());
    }

    private void validateAndSave() {
        errorMsg = new StringBuilder();
        if (!validate()) {
            MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("dock"),
                    errorMsg.toString());
            alert.createAlert();
            return;
        }
        if (btnSaveUpdate.getText().equals(resourceBundle.getString("update"))) {
            dto = setValuesInObjectUpdate();
            updateData();
        } else {
            dto = setValuesInObject();
            saveData();
        }
    }

    private DockMilkTypeDto setValuesInObjectUpdate() {
        dto.setMilkTypes(new ArrayList<>(cboxMilkType.getCheckModel().getCheckedItems()));
        dto.getDock().setIsDefault(chkIsDefault.isSelected() ? AppConstant.ONE : AppConstant.ZERO);
        dto.getDock().setSociety(cboxSociety.getValue());
        dto.getDock().setActive(chkIsActive.isSelected());
        return dto;
    }

    private DockMilkTypeDto setValuesInObject() {
        Dock dock = new Dock();
        dock.setDockNo(txtDockNo.getText());
        dock.setIsDefault(chkIsDefault.isSelected() ? AppConstant.ONE : AppConstant.ZERO);
        dock.setActive(chkIsActive.isSelected());
        dock.setSociety(cboxSociety.getValue());
        dock.setUnionCode(cboxSociety.getValue().getUnion().getCode());

        DockMilkTypeDto dto = new DockMilkTypeDto();
        dto.setDock(dock);
        dto.setMilkTypes(new ArrayList<>(cboxMilkType.getCheckModel().getCheckedItems()));

        return dto;
    }

    private boolean validate() {
        if (cboxSociety.getValue() == null)
            errorMsg.append(resourceBundle.getString("societynullerror") + "\n");
        if (cboxMilkType.getCheckModel().getCheckedItems() == null || cboxMilkType.getCheckModel().getCheckedItems().isEmpty())
            errorMsg.append(resourceBundle.getString("milktypenullerror") + "\n");
        if (txtDockNo.getText() == null || txtDockNo.getText().isEmpty())
            errorMsg.append(resourceBundle.getString("docknullerror") + "\n");
        return errorMsg.length() == 0;
    }

    @Override
    public void saveData() {
        var task = new DockSaveTask(dto, (short) 0);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();
                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("dock"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("dock"),
                        resourceBundle.getString("dock.insert.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void updateData() {
        var task = new DockSaveTask(dto, (short) 1);
        task.setOnSucceeded(e -> {
            try {
                Object obj = task.get();
                if (obj instanceof ApiError) {
                    ApiError error = (ApiError) obj;
                    StringBuilder sb = new StringBuilder();

                    for (ApiValidationError subError : error.getSubErrors()) {
                        sb.append(subError.getField() + " " + resourceBundle.getString(subError.getMessage()) + "\n");
                    }
                    MyAlert alert = new ErrorAlert(MainApp.getStage(), resourceBundle.getString("dock"),
                            sb.toString());
                    alert.createAlert();
                    return;
                }
                MyAlert alert = new InformationAlert(MainApp.getStage(), resourceBundle.getString("dock"),
                        resourceBundle.getString("dock.update.successful"));
                alert.createAlert();
                this.callback.reloadData(true);
                this.stage.close();

            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    @Override
    public void setupComboBox() {
        cboxSociety.setConverter(new SocietyConvertor(cboxSociety));
    }

    private void getNextDockNumber(Society society) {
        if (society == null)
            return;
        var task = new DockNumberLoadTask(society.getCode());
        task.setOnSucceeded(e -> {
            try {
                String nextCode = task.get();
                if (nextCode == null || nextCode.isEmpty())
                    return;
                txtDockNo.setText(nextCode);
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadMilkTypes() {
        var task = new MilkTypeLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<MilkType> list = task.get();
                if (list != null) {
                    cboxMilkType = new CheckComboBox<>(FXCollections.observableList(list));
                    cboxMilkType.setMaxWidth(Double.MAX_VALUE);
                    grid.add(cboxMilkType, 2, 1);

                    if (dto != null) {
                        for (MilkType milkType : list) {
                            MilkType mt = dto.getMilkTypes().stream().filter(p -> p.getCode() == milkType.getCode()).findAny().orElse(null);
                            if (mt != null)
                                cboxMilkType.getCheckModel().check(milkType.getCode() - 1);
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }

    private void loadSociety() {
        var task = new SocietyLoadTask();
        task.setOnSucceeded(e -> {
            try {
                List<Society> list = task.get();
                if (list != null) {
                    cboxSociety.setItems(FXCollections.observableList(list));
                    if (dto != null) {
                        chkIsDefault.setSelected(dto.getDock().getIsDefault() != 0);
                        chkIsActive.setSelected(dto.getDock().isActive());
                        cboxSociety.setValue(dto.getDock().getSociety());
                        txtDockNo.setText(dto.getDock().getDockNo());
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });
        new Thread(task).start();
    }
}
