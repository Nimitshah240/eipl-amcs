package com.eipl.amcs.operation.share.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Share extends BaseModel {
    private String code;
    private LocalDate issueDate;
    private LocalDate transferDate;
    private BigDecimal shareAmount;
    private String certificateNo;
    private String ledgerNo;
    private Boolean cancelled;
    private Boolean refund;
    private Boolean transferred;
    private Integer noOfShare;
    private Boolean checkMember;

    private Integer noOfRefundShare;
    private Integer noOfTransferredShare;
    private Member transferredFrom;

    public Member getTransferredFrom() {
        return transferredFrom;
    }

    public void setTransferredFrom(Member transferredFrom) {
        this.transferredFrom = transferredFrom;
    }

    private BigDecimal unitCost;
    private String voucherNo;
    private String unionCode;
    private String shareCode;
    private Member member;
    private Society society;
    private String transferredFromCode;

    private String xcol4;
    private String xcol5;

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public String getTransferredFromCode() {
        return transferredFromCode;
    }

    public void setTransferredFromCode(String transferredFromCode) {
        this.transferredFromCode = transferredFromCode;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    private LocalDate cancelDate;


    public String getLedgerNo() {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo) {
        this.ledgerNo = ledgerNo;
    }

    public String getXcol4() {
        return xcol4;
    }

    public Boolean getCheckMember() {
        return checkMember;
    }

    public void setCheckMember(Boolean checkMember) {
        this.checkMember = checkMember;
    }

    public void setXcol4(String xcol4) {
        this.xcol4 = xcol4;
    }

    public String getXcol5() {
        return xcol5;
    }

    public void setXcol5(String xcol5) {
        this.xcol5 = xcol5;
    }

    public Integer getNoOfShare() {
        return noOfShare;
    }

    public void setNoOfShare(Integer noOfShare) {
        this.noOfShare = noOfShare;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Integer getNoOfRefundShare() {
        return noOfRefundShare;
    }

    public void setNoOfRefundShare(Integer noOfRefundShare) {
        this.noOfRefundShare = noOfRefundShare;
    }

    public Integer getNoOfTransferredShare() {
        return noOfTransferredShare;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Boolean getRefund() {
        return refund;
    }

    public void setRefund(Boolean refund) {
        this.refund = refund;
    }

    public Boolean getTransferred() {
        return transferred;
    }

    public void setTransferred(Boolean transferred) {
        this.transferred = transferred;
    }

    public void setNoOfTransferredShare(Integer noOfTransferredShare) {
        this.noOfTransferredShare = noOfTransferredShare;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }


    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}
