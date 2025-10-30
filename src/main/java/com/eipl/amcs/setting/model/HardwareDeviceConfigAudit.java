package com.eipl.amcs.setting.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.HardwareDevice;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hardware_device_configs_audit")
public class HardwareDeviceConfigAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String commPort;
    private Short connType;
    private String deviceType;
    private Short sequenceNo;
    private String unionCode;
    private Integer analyserModeType; // 0 - Seq, 1 - Milk Type Wise
    private Integer analyserMilkType; // 1 - Cow, 2 - Buff
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hardware_device_code", foreignKey = @ForeignKey(name = "fk_hardware_device_configs_hardware_device_code"))
    private HardwareDevice hardwareDevice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_hardware_device_configs_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dock_no", foreignKey = @ForeignKey(name = "fk_hardware_device_configs_dock_code"))
    @JsonIgnoreProperties(value = {"society"})
    private Dock dock;

    @Override
    public String getTableName() {
        return "hardware_device_configs_audit";
    }
}
