package com.eipl.amcs.operation.share.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "share")
public class Share extends BaseModel {
    @Id
    private String code;
    private LocalDate issueDate;

    private LocalDate cancelDate;
    private LocalDate transferDate;
    private BigDecimal shareAmount;
    private String certificateNo;
    private String ledgerNo;

    @Column(name = "is_cancelled")
    private Boolean cancelled;
    @Column(name = "is_refund")
    private Boolean refund;
    @Column(name = "is_transferred")
    private Boolean transferred;
    private Integer noOfRefundShare;
    private Integer noOfShare;
    private Integer certificate_from;
    private Integer certificate_to;
    private Integer noOfTransferredShare;
    private BigDecimal unitCost;
    private String voucherNo;
    private String unionCode;
    private String shareCode;
    private String transferredFromCode;
    @Column(name = "is_member")
    private Boolean checkMember;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_share_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "transferred_from_share_code", foreignKey = @ForeignKey(name = "fk_share_transferred_from_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member transferredFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_share_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "route", "bmc", "mcc", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @Column(name = "x_col4", length = 255)
    private String xCol4;
    @Column(name = "x_col5", length = 255)
    private String xCol5;

    @Override
    public String getTableName() {
        return "share";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ShareAudit audit = new ShareAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setIssueDate(this.getIssueDate());
        audit.setIsCancelled(this.getCancelled());
        audit.setTransferDate(this.getTransferDate());
        audit.setShareAmount(this.getShareAmount());
        audit.setCertificateNo(this.getCertificateNo());
        audit.setLedgerNo(this.getLedgerNo());
        audit.setCancelDate(this.getCancelDate());
        audit.setIsRefund(this.getRefund());
        audit.setTransferred(this.getTransferred());
        audit.setNoOfRefundShare(this.getNoOfRefundShare());
        audit.setNoOfShare(this.getNoOfShare());
        audit.setCertificate_from(this.getCertificate_from());
        audit.setCertificate_to(this.getCertificate_to());
        audit.setNoOfTransferredShare(this.getNoOfTransferredShare());
        audit.setUnitCost(this.getUnitCost());
        audit.setVoucherNo(this.getVoucherNo());
        audit.setUnionCode(this.getUnionCode());
        audit.setShareCode(this.getShareCode());
        audit.setTransferredFromCode(this.getTransferredFromCode());
        audit.setMember(this.getMember());
        audit.setTransferredFrom(this.getTransferredFrom());
        audit.setSociety(this.getSociety());
        audit.setCheckMember(this.getCheckMember());


        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        return audit;
    }

    public String getXcol4() {
        return xCol4;
    }

    public void setXcol4(String xcol4) {
        this.xCol4 = xcol4;
    }

    public String getXcol5() {
        return xCol5;
    }

    public void setXcol5(String xcol5) {
        this.xCol5 = xcol5;
    }
}
