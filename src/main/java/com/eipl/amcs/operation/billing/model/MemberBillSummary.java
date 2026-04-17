package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.BankDeserializer;
import com.eipl.amcs.json.deserialize.SocietyPaymentCycleDeserializer;
import com.eipl.amcs.json.serialize.BankSerialize;
import com.eipl.amcs.json.serialize.SocietyPaymentCycleSerialize;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "member_bill_summary")
public class MemberBillSummary extends BaseModelTxn {
    @Id
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
    private short status; //1-PENDING, 6-DISBURSED

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonSerialize(using = SocietyPaymentCycleSerialize.class)
    @JsonDeserialize(using = SocietyPaymentCycleDeserializer.class)
    @JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(name = "fk_member_bill_summary_payment_cycle_code"))
    @JsonIgnoreProperties(value = {"society", "fromShift", "toShift"})
    private SocietyPaymentCycle paymentCycle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_member_bill_summary_bank_code"))
    private Bank bank;

    @Override
    public String getTableName() {
        return "member_bill_summary";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MemberBillSummaryAudit audit = new MemberBillSummaryAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setMilkAmount(this.getMilkAmount());
        audit.setMilkQty(this.getMilkQty());
        audit.setProductSaleAmount(this.getProductSaleAmount());
        audit.setLocalSaleAmount(this.getLocalSaleAmount());
        audit.setLoanAmount(this.getLoanAmount());
        audit.setOtherAddAmount(this.getOtherAddAmount());
        audit.setNetAmount(this.getNetAmount());
        audit.setOtherDedAmount(this.getOtherDedAmount());
        audit.setDisbursedAmount(this.getDisbursedAmount());
        audit.setStatus(this.getStatus());
        audit.setPaymentCycle(this.getPaymentCycle());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }
}
