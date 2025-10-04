package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FinalizeDto {
    private SocietyPaymentCycle paymentCycle;
    private List<String> memberCodeList;

//    public FinalizeDto(SocietyPaymentCycle paymentCycle, List<String> memberCodeList) {
//        this.paymentCycle = paymentCycle;
//        this.memberCodeList = memberCodeList;
//    }
//
//    public FinalizeDto() {
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
//    public List<String> getMemberCodeList() {
//        return memberCodeList;
//    }
//
//    public void setMemberCodeList(List<String> memberCodeList) {
//        this.memberCodeList = memberCodeList;
//    }
}
