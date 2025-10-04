package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.operation.model.BillHead;
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
public class MemberBillTransactionAudit extends BaseModelTxnAudit{
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
	@JoinColumn(name = "member_bill_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MemberBill memberBill;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bill_head_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private BillHead billHead;
}
