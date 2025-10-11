package com.eipl.amcs.master.procurement.dto;

import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRate;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateApplicability;
import com.eipl.amcs.master.procurement.model.SocietyMilkPurchaseRateBased;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Setter
@Getter
public class SocietyMilkPurchaseRateDto {
    private SocietyMilkPurchaseRate purchaseRate;
    private List<SocietyMilkPurchaseRateApplicability> listApplicability;
    private List<SocietyMilkPurchaseRateBased> listRateBased;
    private List<String> listDetail;

//    public SocietyMilkPurchaseRateDto() {
//    }
//
//    public List<SocietyMilkPurchaseRateBased> getListRateBased() {
//        return listRateBased;
//    }
//
//    public void setListRateBased(List<SocietyMilkPurchaseRateBased> listRateBased) {
//        this.listRateBased = listRateBased;
//    }
//
//    public SocietyMilkPurchaseRate getPurchaseRate() {
//        return purchaseRate;
//    }
//
//    public void setPurchaseRate(SocietyMilkPurchaseRate purchaseRate) {
//        this.purchaseRate = purchaseRate;
//    }
//
//    public List<SocietyMilkPurchaseRateApplicability> getListApplicability() {
//        return listApplicability;
//    }
//
//    public void setListApplicability(List<SocietyMilkPurchaseRateApplicability> listApplicability) {
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
