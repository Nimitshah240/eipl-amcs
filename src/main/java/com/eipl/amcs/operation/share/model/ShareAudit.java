package com.eipl.amcs.operation.share.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.MemberDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.json.serialize.MemberSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "share_audit")
public class ShareAudit extends BaseModelTxnAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 25)
    private String code;
    private LocalDate issueDate;
    private LocalDate cancelDate;
    private LocalDate transferDate;
    private BigDecimal shareAmount;
    private String certificateNo;
    private String ledgerNo;
    @Column(name = "is_cancelled")
    private Boolean isCancelled;
    @Column(name = "is_refund")
    private Boolean isRefund;
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
    @Column(name = "is_active")
    private boolean active;
    private String transferredFromCode;
    @Column(name = "is_member")
    private Boolean checkMember;


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
        return "share_audit";
    }
}
