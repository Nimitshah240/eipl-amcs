package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class MemberSocietyInfoDto {
    private Member member;
    private List<MilkCollection> memberCollection;
    private List<MilkCollection> prevCollectionData;
    private List<MilkCollection> currentPaymentCycleData;

    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal clr;
    private BigDecimal qty;

//    public MemberSocietyInfoDto() {
//    }
//
//    public List<MilkCollection> getPrevCollectionData() {
//        return prevCollectionData;
//    }
//
//    public void setPrevCollectionData(List<MilkCollection> prevCollectionData) {
//        this.prevCollectionData = prevCollectionData;
//    }
//
//    public List<MilkCollection> getCurrentPaymentCycleData() {
//        return currentPaymentCycleData;
//    }
//
//    public void setCurrentPaymentCycleData(List<MilkCollection> currentPaymentCycleData) {
//        this.currentPaymentCycleData = currentPaymentCycleData;
//    }
//
//    public Member getMember() {
//        return member;
//    }
//
//    public void setMember(Member member) {
//        this.member = member;
//    }
//
//    public List<MilkCollection> getMemberCollection() {
//        return memberCollection;
//    }
//
//    public void setMemberCollection(List<MilkCollection> memberCollection) {
//        this.memberCollection = memberCollection;
//    }
//
//    public BigDecimal getFat() {
//        return fat;
//    }
//
//    public void setFat(BigDecimal fat) {
//        this.fat = fat;
//    }
//
//    public BigDecimal getSnf() {
//        return snf;
//    }
//
//    public void setSnf(BigDecimal snf) {
//        this.snf = snf;
//    }
//
//    public BigDecimal getClr() {
//        return clr;
//    }
//
//    public void setClr(BigDecimal clr) {
//        this.clr = clr;
//    }
//
//    public BigDecimal getQty() {
//        return qty;
//    }
//
//    public void setQty(BigDecimal qty) {
//        this.qty = qty;
//    }
}
