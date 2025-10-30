package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.BillHeadDeserializer;
import com.eipl.amcs.json.deserialize.MemberBillDeserializer;
import com.eipl.amcs.json.serialize.BillHeadSerialize;
import com.eipl.amcs.json.serialize.MemberBillSerialize;
import com.eipl.amcs.master.operation.model.BillHead;
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
@Table(name = "member_bill_transaction")
public class MemberBillTransaction extends BaseModelTxn {
    @Id
    private String code;
    private BigDecimal amount;
    private BigDecimal adjustment;
    private BigDecimal prevDue;
    private BigDecimal due;
    private short type; //1-Addition, 2-Deduction
    private String refNo;
    private String formula;
    private String fraction;
    private String unionCode;
    private String societyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberBillSerialize.class)
    @JsonDeserialize(using = MemberBillDeserializer.class)
    @JoinColumn(name = "member_bill_code", foreignKey = @ForeignKey(name = "fk_member_bill_transaction_member_bill_code"))
    @JsonIgnoreProperties(value = {"society", "union", "member", "paymentCycle"})
    private MemberBill memberBill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BillHeadSerialize.class)
    @JsonDeserialize(using = BillHeadDeserializer.class)
    @JoinColumn(name = "bill_head_code", foreignKey = @ForeignKey(name = "fk_member_bill_transaction_bill_head_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private BillHead billHead;

    @Override
    public String getTableName() {
        return "member_bill_transaction";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MemberBillTransactionAudit audit = new MemberBillTransactionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setAmount(this.getAmount());
        audit.setAdjustment(this.getAdjustment());
        audit.setPrevDue(this.getPrevDue());
        audit.setDue(this.getDue());
        audit.setType(this.getType());
        audit.setRefNo(this.getRefNo());
        audit.setFormula(this.getFormula());
        audit.setFraction(this.getFraction());
        audit.setUnionCode(this.getUnionCode());
        audit.setSocietyCode(this.getSocietyCode());
        audit.setMemberBill(this.getMemberBill());
        audit.setBillHead(this.getBillHead());

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
