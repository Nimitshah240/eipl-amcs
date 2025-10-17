package com.eipl.amcs.operation.procurement.dto;

import java.io.Serializable;

public class HardwareSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean autoTare;
    private boolean autoQuality;
    private boolean autoQuantity;

    public HardwareSetting() {

    }

    public boolean isAutoTare() {
        return autoTare;
    }

    public void setAutoTare(boolean autoTare) {
        this.autoTare = autoTare;
    }

    public boolean isAutoQuality() {
        return autoQuality;
    }

    public void setAutoQuality(boolean autoQuality) {
        this.autoQuality = autoQuality;
    }

    public boolean isAutoQuantity() {
        return autoQuantity;
    }

    public void setAutoQuantity(boolean autoQuantity) {
        this.autoQuantity = autoQuantity;
    }
}
