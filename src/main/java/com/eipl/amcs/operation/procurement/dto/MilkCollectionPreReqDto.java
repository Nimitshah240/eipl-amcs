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

//    public MilkCollectionPreReqDto() {
//    }
//
//    public SocietyPaymentCycle getPaymentCycle() {
//        return paymentCycle;
//    }
//
//    public void setPaymentCycle(SocietyPaymentCycle paymentCycle) {
//        this.paymentCycle = paymentCycle;
//    }
//
//    public String getMemberRate() {
//        return memberRate;
//    }
//
//    public void setMemberRate(String memberRate) {
//        this.memberRate = memberRate;
//    }
//
//    public String getSocietyRate() {
//        return societyRate;
//    }
//
//    public void setSocietyRate(String societyRate) {
//        this.societyRate = societyRate;
//    }
//
//    public List<HardwareDeviceConfig> getHardwareConfigList() {
//        return hardwareConfigList;
//    }
//
//    public void setHardwareConfigList(List<HardwareDeviceConfig> hardwareConfigList) {
//        this.hardwareConfigList = hardwareConfigList;
//    }
}