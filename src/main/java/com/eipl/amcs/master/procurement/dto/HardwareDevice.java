package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.base.model.BaseModel;

public class HardwareDevice extends BaseModel {
    private String code;
    private String deviceName;
    private Short deviceType;
    private Integer baudRate;
    private Short bitRate;
    private Short length;
    private Short parity;
    private Short stopBit;
    private Short readingType;
    private String discardChars;
    private String tareChar;
    private String regEx;
    private String splitChars;
    private String startChar;
    private String endChar;
    private Short incomingDataType;
    private Boolean isSnf;
    private String unionCode;

    public HardwareDevice() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Short getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Short deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(Integer baudRate) {
        this.baudRate = baudRate;
    }

    public Short getBitRate() {
        return bitRate;
    }

    public void setBitRate(Short bitRate) {
        this.bitRate = bitRate;
    }

    public Short getLength() {
        return length;
    }

    public void setLength(Short length) {
        this.length = length;
    }

    public Short getParity() {
        return parity;
    }

    public void setParity(Short parity) {
        this.parity = parity;
    }

    public Short getStopBit() {
        return stopBit;
    }

    public void setStopBit(Short stopBit) {
        this.stopBit = stopBit;
    }

    public Short getReadingType() {
        return readingType;
    }

    public void setReadingType(Short readingType) {
        this.readingType = readingType;
    }

    public String getDiscardChars() {
        return discardChars;
    }

    public void setDiscardChars(String discardChars) {
        this.discardChars = discardChars;
    }

    public String getTareChar() {
        return tareChar;
    }

    public void setTareChar(String tareChar) {
        this.tareChar = tareChar;
    }

    public String getRegEx() {
        return regEx;
    }

    public void setRegEx(String regEx) {
        this.regEx = regEx;
    }

    public String getSplitChars() {
        return splitChars;
    }

    public void setSplitChars(String splitChars) {
        this.splitChars = splitChars;
    }

    public String getStartChar() {
        return startChar;
    }

    public void setStartChar(String startChar) {
        this.startChar = startChar;
    }

    public String getEndChar() {
        return endChar;
    }

    public void setEndChar(String endChar) {
        this.endChar = endChar;
    }

    public Short getIncomingDataType() {
        return incomingDataType;
    }

    public void setIncomingDataType(Short incomingDataType) {
        this.incomingDataType = incomingDataType;
    }

    public Boolean getSnf() {
        return isSnf;
    }

    public void setSnf(Boolean snf) {
        isSnf = snf;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    @Override
    public String toString() {
        return deviceName;
    }
}