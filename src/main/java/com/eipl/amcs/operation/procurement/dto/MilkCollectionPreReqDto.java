package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.setting.model.HardwareDeviceConfig;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class MilkCollectionPreReqDto {
    private SocietyPaymentCycle paymentCycle;
    private String memberRate;
    private String societyRate;
    private List<HardwareDeviceConfig> hardwareConfigList;
}