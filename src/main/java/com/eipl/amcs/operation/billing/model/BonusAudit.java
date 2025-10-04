package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "bonus_audit")
public class BonusAudit extends BaseModelTxnAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private BigDecimal milkQty;
	private BigDecimal milkAmount;
	private BigDecimal bonusAmount;
	
	private short status; //0-PENDING,1-DISBURSED
	private short type; //0-Union,1-Society
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bonus_summary_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private BonusSummary bonusSummary;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Society society;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Union union;
	
	@Override
	public String getTableName() {
		return "bonus_audit";
	}
	
}
