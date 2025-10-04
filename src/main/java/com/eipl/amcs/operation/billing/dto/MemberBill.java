package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MemberBill {
    private String code;
    private BigDecimal milkQty;
    private BigDecimal avgFat;
    private BigDecimal avgSnf;
    private BigDecimal avgClr;
    private BigDecimal kgFat;
    private BigDecimal kgSnf;
    private BigDecimal milkAmount;
    private BigDecimal productSaleAmount;
    private BigDecimal localSaleAmount;
    private BigDecimal loanAmount;
    private BigDecimal otherAddAmount;
    private BigDecimal otherDedAmount;
    private BigDecimal netAmount;

    private String voucherNo;
    private short paymnetMode; //0-Cash, 1-Bank
    private String bankAcno;
    private String ifsc;
    private String paymentRef;

    private short status; //1-PENDING, 2-SENT, 3-SENT_DBT, 4-REJECT, 5-PROCESSING, 6-DISBURSED, 7-FAIL

    private boolean disbursed;
    private LocalDate disbursedDate;
    private Society society;
    private Union union;
    private Member member;
    private SocietyPaymentCycle paymentCycle;

//    public MemberBill() {
//        selected = new SimpleBooleanProperty();
//    }
//
//    private BooleanProperty selected;

//    public final BooleanProperty selectedProperty() {
//        return this.selected;
//    }

//    public final boolean isSelected() {
//        return this.selectedProperty().get();
//    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMilkQty() {
        return milkQty;
    }

    public void setMilkQty(BigDecimal milkQty) {
        this.milkQty = milkQty;
    }

    public BigDecimal getAvgFat() {
        return avgFat;
    }

    public void setAvgFat(BigDecimal avgFat) {
        this.avgFat = avgFat;
    }

    public BigDecimal getAvgSnf() {
        return avgSnf;
    }

    public void setAvgSnf(BigDecimal avgSnf) {
        this.avgSnf = avgSnf;
    }

    public BigDecimal getAvgClr() {
        return avgClr;
    }

    public void setAvgClr(BigDecimal avgClr) {
        this.avgClr = avgClr;
    }

    public BigDecimal getKgFat() {
        return kgFat;
    }

    public void setKgFat(BigDecimal kgFat) {
        this.kgFat = kgFat;
    }

    public BigDecimal getKgSnf() {
        return kgSnf;
    }

    public void setKgSnf(BigDecimal kgSnf) {
        this.kgSnf = kgSnf;
    }

    public BigDecimal getMilkAmount() {
        return milkAmount;
    }

    public void setMilkAmount(BigDecimal milkAmount) {
        this.milkAmount = milkAmount;
    }

    public BigDecimal getProductSaleAmount() {
        return productSaleAmount;
    }

    public void setProductSaleAmount(BigDecimal productSaleAmount) {
        this.productSaleAmount = productSaleAmount;
    }

    public BigDecimal getLocalSaleAmount() {
        return localSaleAmount;
    }

    public void setLocalSaleAmount(BigDecimal localSaleAmount) {
        this.localSaleAmount = localSaleAmount;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getOtherAddAmount() {
        return otherAddAmount;
    }

    public void setOtherAddAmount(BigDecimal otherAddAmount) {
        this.otherAddAmount = otherAddAmount;
    }

    public BigDecimal getOtherDedAmount() {
        return otherDedAmount;
    }

    public void setOtherDedAmount(BigDecimal otherDedAmount) {
        this.otherDedAmount = otherDedAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public short getPaymnetMode() {
        return paymnetMode;
    }

    public void setPaymnetMode(short paymnetMode) {
        this.paymnetMode = paymnetMode;
    }

    public String getBankAcno() {
        return bankAcno;
    }

    public void setBankAcno(String bankAcno) {
        this.bankAcno = bankAcno;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public boolean isDisbursed() {
        return disbursed;
    }

    public void setDisbursed(boolean disbursed) {
        this.disbursed = disbursed;
    }

    public LocalDate getDisbursedDate() {
        return disbursedDate;
    }

    public void setDisbursedDate(LocalDate disbursedDate) {
        this.disbursedDate = disbursedDate;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public Union getUnion() {
        return union;
    }

    public void setUnion(Union union) {
        this.union = union;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public SocietyPaymentCycle getPaymentCycle() {
        return paymentCycle;
    }

    public void setPaymentCycle(SocietyPaymentCycle paymentCycle) {
        this.paymentCycle = paymentCycle;
    }
}
