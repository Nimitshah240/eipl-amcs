package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.BillHeadDeserializer;
import com.eipl.amcs.json.deserialize.MemberBillDeserializer;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.json.serialize.BillHeadSerialize;
import com.eipl.amcs.json.serialize.MemberBillSerialize;
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
@Table(name = "member_bill_transaction_audit")
public class MemberBillTransactionAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JoinColumn(name = "member_bill_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MemberBill memberBill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BillHeadSerialize.class)
    @JsonDeserialize(using = BillHeadDeserializer.class)
    @JoinColumn(name = "bill_head_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private BillHead billHead;
}
