package com.eipl.amcs.setting.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
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
@Table(name = "hardware_device_configs")
public class HardwareDeviceConfig extends BaseModelTxn {

    @Id
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
        return "hardware_device_configs";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        HardwareDeviceConfigAudit audit = new HardwareDeviceConfigAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setCommPort(this.getCommPort());
        audit.setConnType(this.getConnType());
        audit.setDeviceType(this.getDeviceType());
        audit.setSequenceNo(this.getSequenceNo());
        audit.setUnionCode(this.getUnionCode());
        audit.setHardwareDevice(this.getHardwareDevice());
        audit.setSociety(this.getSociety());
        audit.setDock(this.getDock());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        audit.setAnalyserModeType(this.getAnalyserModeType());
        audit.setAnalyserMilkType(this.getAnalyserMilkType());

        return audit;
    }
}
