package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hardware_devices")
public class HardwareDevice extends BaseModel {

    @Id
    @Size(max = 10)
    private String code;
    @Size(max = 100)
    private String deviceName;
    private Short deviceType; //0-WS, 1-Analyzer, 2-Display, 3-Splitter
    private Integer baudRate;
    private Short bitRate;
    private Short length;
    private Short parity;
    private Short stopBit;
    private Short readingType;
    @Size(max = 50)
    private String discardChars;
    @Size(max = 5)
    private String tareChar;
    @Size(max = 255)
    private String regEx;
    @Size(max = 15)
    private String splitChars;
    @Size(max = 5)
    private String startChar;
    @Size(max = 5)
    private String endChar;
    private Short incomingDataType;
    @Column(name = "is_snf")
    private Boolean isSnf;
    private String unionCode;

    @Override
    public String toString() {
        return deviceName;
    }

    @Override
    public String getTableName() {
        return "hardware_devices";
    }
}
