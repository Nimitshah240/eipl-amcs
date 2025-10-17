package com.eipl.amcs.operation.share.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "share_dividend_audit")
public class ShareDividendAudit extends BaseModelTxnAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 25)
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
    private FinancialYear financialYearCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_share_dividend_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "route", "bmc", "mcc", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @Column(name = "x_col4", length = 255)
    private String xCol4;
    @Column(name = "x_col5", length = 255)
    private String xCol5;

    @Override
    public String getTableName() {
        return "share_dividend_audit";
    }
}
