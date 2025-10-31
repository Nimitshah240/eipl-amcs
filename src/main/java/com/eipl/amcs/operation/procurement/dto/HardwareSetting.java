package com.eipl.amcs.operation.procurement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HardwareSetting implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean autoTare;
    private boolean autoQuality;
    private boolean autoQuantity;
}
