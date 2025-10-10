package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_credit_limit_transaction_audit")
public class MemberCreditLimitTransactionAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 15)
    private String code;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal newValue;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal oldValue;
    @Size(max = 25)
    private String consumerCode;
    private int consumerType;
    @Size(max = 45)
    private String referenceCode;
    @Size(max = 50)
    private String transactionType;
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    private String unionCode;

    @Override
    public String getTableName() {
        return "member_credit_limit_transaction_audit";
    }
}
