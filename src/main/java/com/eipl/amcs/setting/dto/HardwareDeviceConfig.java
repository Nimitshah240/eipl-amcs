package com.eipl.amcs.setting.dto;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.HardwareDevice;

public class HardwareDeviceConfig extends BaseModelTxn {
    private String code;
    private String commPort;
    private Short connType; // 0 - COMM
    private String deviceType;
    private Short sequenceNo;
    private String unionCode;
    private Integer analyserModeType; // 0 - Seq, 1 - Milk Type Wise
    private Integer analyserMilkType; // 1 - Cow, 2 - Buff

    private HardwareDevice hardwareDevice;
    private Society society;
    private Dock dock;

    public HardwareDeviceConfig() {
        connType = (short) 0;
        sequenceNo = (short) 0;
        analyserModeType = 0;
        unionCode = MainApp.identityDto.getUnion().getCode();
        society = MainApp.identityDto.getSociety();
        dock = MainApp.identityDto.getDock();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCommPort() {
        return commPort;
    }

    public void setCommPort(String commPort) {
        this.commPort = commPort;
    }

    public Short getConnType() {
        return connType;
    }

    public void setConnType(Short connType) {
        this.connType = connType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Short getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Short sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public HardwareDevice getHardwareDevice() {
        return hardwareDevice;
    }

    public void setHardwareDevice(HardwareDevice hardwareDevice) {
        this.hardwareDevice = hardwareDevice;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public Integer getAnalyserModeType() {
        return analyserModeType;
    }

    public void setAnalyserModeType(Integer analyserModeType) {
        this.analyserModeType = analyserModeType;
    }

    public Integer getAnalyserMilkType() {
        return analyserMilkType;
    }

    public void setAnalyserMilkType(Integer analyserMilkType) {
        this.analyserMilkType = analyserMilkType;
    }
}