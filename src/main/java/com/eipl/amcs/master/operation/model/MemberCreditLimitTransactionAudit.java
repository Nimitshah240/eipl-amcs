package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
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
    private String code;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal newValue;
    @Digits(integer = 10, fraction = 2)
    private BigDecimal oldValue;
    private String consumerCode;
    private int consumerType;
    private String referenceCode;
    private String transactionType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
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
