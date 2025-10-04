package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
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
@Table(name = "local_milk_sale_rate_audit")
public class LocalMilkSaleRateAudit extends BaseModelTxnAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 25)
	private String code;
	@Digits(integer = 4, fraction = 2)
	private BigDecimal rate;
	@Size(max = 3)
	private String unionCode;
	private LocalDate wefDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MilkType milkType;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "milk_class_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MilkClass milkClass;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Society society;

	@Override
	public String getTableName() {
		return "local_milk_sale_rate_audit";
	}
}
