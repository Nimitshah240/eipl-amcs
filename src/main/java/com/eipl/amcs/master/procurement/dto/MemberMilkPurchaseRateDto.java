package com.eipl.amcs.master.procurement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateApplicability;

import java.util.List;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.MemberMilkPurchaseRateBased;

@NoArgsConstructor
@Setter
@Getter
public class MemberMilkPurchaseRateDto {
    private MemberMilkPurchaseRate purchaseRate;
    private List<MemberMilkPurchaseRateApplicability> listApplicability;
    private List<MemberMilkPurchaseRateBased> listRateBased;
    private List<String> listDetail;

//    public MemberMilkPurchaseRateDto() {
//    }
//
//    public List<MemberMilkPurchaseRateBased> getListRateBased() {
//        return listRateBased;
//    }
//
//    public void setListRateBased(List<MemberMilkPurchaseRateBased> listRateBased) {
//        this.listRateBased = listRateBased;
//    }
//
//    public MemberMilkPurchaseRate getPurchaseRate() {
//        return purchaseRate;
//    }
//
//    public void setPurchaseRate(MemberMilkPurchaseRate purchaseRate) {
//        this.purchaseRate = purchaseRate;
//    }
//
//    public List<MemberMilkPurchaseRateApplicability> getListApplicability() {
//        return listApplicability;
//    }
//
//    public void setListApplicability(List<MemberMilkPurchaseRateApplicability> listApplicability) {
//        this.listApplicability = listApplicability;
//    }
//
//    public List<String> getListDetail() {
//        return listDetail;
//    }
//
//    public void setListDetail(List<String> listDetail) {
//        this.listDetail = listDetail;
//    }
}
