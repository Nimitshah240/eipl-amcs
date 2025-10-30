package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.SocietyPaymentCycleDeserializer;
import com.eipl.amcs.json.serialize.SocietyPaymentCycleSerialize;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_bill_summary_audit")
public class MemberBillSummaryAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private BigDecimal milkQty;
    private BigDecimal milkAmount;
    private BigDecimal productSaleAmount;
    private BigDecimal localSaleAmount;
    private BigDecimal loanAmount;
    private BigDecimal otherAddAmount;
    private BigDecimal otherDedAmount;
    private BigDecimal netAmount;
    private BigDecimal disbursedAmount;
    private short status; //1-PENDING

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietyPaymentCycleSerialize.class)
    @JsonDeserialize(using = SocietyPaymentCycleDeserializer.class)
    @JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SocietyPaymentCycle paymentCycle;

    @Override
    public String getTableName() {
        return "member_bill_summary";
    }
}
