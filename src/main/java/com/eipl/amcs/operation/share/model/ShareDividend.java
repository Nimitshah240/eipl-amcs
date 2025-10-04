package com.eipl.amcs.operation.share.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "share_dividend")
public class ShareDividend extends BaseModel {
    @Id
    private String code;
    private LocalDate disbursementDate;
    private BigDecimal dividendAmount;
    private BigDecimal dividendValue;
    private Boolean isDisbursed;
    private Integer noOfShare;
    private Integer dividendValueType;
    private BigDecimal shareAmount;
    private String unionCode;
    private String shareCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_share_dividend_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_year_code", foreignKey = @ForeignKey(name = "fk_financial_year_share_dividend_code"))
    private FinancialYear financialYear;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_share_dividend_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "route", "bmc", "mcc", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @Column(name = "x_col4", length = 255)
    private String xCol4;
    @Column(name = "x_col5", length = 255)
    private String xCol5;

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ShareDividendAudit audit = new ShareDividendAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setDividendAmount(this.getDividendAmount());
        audit.setDisbursementDate(this.getDisbursementDate());
        audit.setIsDisbursed(this.getIsDisbursed());
        audit.setNoOfShare(this.getNoOfShare());
        audit.setFinancialYearCode(this.getFinancialYear());
        audit.setSociety(this.getSociety());
        audit.setDividendValue(this.getDividendValue());
        audit.setShareAmount(this.getShareAmount());
        audit.setUnionCode(this.getUnionCode());
        audit.setMember(this.getMember());
        audit.setShareCode(this.getShareCode());
        audit.setDividendValueType(this.getDividendValueType());
        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        return audit;
    }

    public void setDisbursed(Boolean disbursed) {
        isDisbursed = disbursed;
    }

}
