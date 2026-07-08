package com.eipl.amcs.reportengine.popup;

import com.eipl.amcs.controls.AutoSearchTextField;
import com.eipl.amcs.controls.E_DatePicker;
import com.eipl.amcs.controls.E_NumericField;
import com.eipl.amcs.controls.E_TextField;
import com.eipl.amcs.reportengine.dto.StaticLookupItem;
import com.eipl.amcs.reportengine.model.RptLookup;
import com.eipl.amcs.reportengine.model.RptParameterMaster;
import com.eipl.amcs.reportengine.model.RptReportParameter;
import com.eipl.amcs.reportengine.service.LookupService;
import com.eipl.amcs.reportengine.util.ReflectionUtil;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

                return createAutoSearchControl(rp);

            case "TEXT":

                E_TextField txt = new E_TextField();
                if (rp.getDefaultValue() != null) {
                    txt.setText(rp.getDefaultValue());
                }
                return txt;
            case "Numeric":

                E_NumericField numericField = new E_NumericField();
                if (rp.getDefaultValue() != null) {
                    numericField.setText(rp.getDefaultValue());
                }
                return numericField;
            default:

                return new TextField();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> AutoSearchTextField<T> createAutoSearchControl(RptReportParameter rp) {

        AutoSearchTextField<T> control = new AutoSearchTextField<>();

        RptParameterMaster pm = rp.getParameterMaster();

        List<T> items = Collections.emptyList();

        if (pm.getLookup() != null) {
            items = lookupService.load(pm.getLookup().getLookupCode());
            if (Boolean.TRUE.equals(rp.getRequiredAll()) && !items.isEmpty()) {
                T allItem = createAllItem(items.get(0), pm.getLookup());
                items.add(0, allItem);
            }
            control.getItems().setAll(items);
        }

        if (rp.getDefaultValue() != null && !rp.getDefaultValue().isBlank() && pm.getLookup() != null) {
            T selectedItem = findItemByValue(items, pm.getLookup().getValueField(), rp.getDefaultValue());
            if (selectedItem != null) {
                control.setValue(selectedItem);
            }
        }

        return control;
    }

    @SuppressWarnings("unchecked")
    private <T> T createAllItem(T sample, RptLookup lookup) {

        try {

            Class<?> clazz = sample.getClass();

            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            Object instance = constructor.newInstance();

            ReflectionUtil.setFieldValue(instance, lookup.getValueField(), "0");
            ReflectionUtil.setFieldValue(instance, lookup.getDisplayField(), "All");

            return (T) instance;

        } catch (Exception e) {
            throw new RuntimeException("Unable to create 'All' item for " + sample.getClass().getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T findItemByValue(List<T> items,
                                  String valueField,
                                  String defaultValue) {

        if (items == null || items.isEmpty()) {
            return null;
        }

        for (T item : items) {

            Object value;

            if (item instanceof StaticLookupItem) {

                value = ((StaticLookupItem) item).getValue();

            } else {

                value = ReflectionUtil.getFieldValue(item, valueField);
            }

            if (value != null &&
                    String.valueOf(value).equalsIgnoreCase(defaultValue)) {

                return item;
            }
        }

        return null;
    }
}