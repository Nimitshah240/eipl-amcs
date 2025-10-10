package com.eipl.amcs.master.procurement.converter;

import com.eipl.amcs.master.procurement.model.HardwareDevice;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class HardwareDeviceConvertor extends StringConverter<HardwareDevice> {

    private final ComboBox<HardwareDevice> cboxHardwareDevice;

    public HardwareDeviceConvertor(ComboBox<HardwareDevice> cboxHardwareDevice) {
        this.cboxHardwareDevice = cboxHardwareDevice;
    }

    @Override
    public HardwareDevice fromString(String string) {
        return cboxHardwareDevice.getItems().stream()
                .filter(p -> string.equals(p.toString())).findFirst().orElse(null);
    }

    @Override
    public String toString(HardwareDevice object) {
        if (object == null)
            return null;
        return object.toString();
    }

}