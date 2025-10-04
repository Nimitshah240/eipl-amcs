package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
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
@Table(name = "local_milk_sale_rate")
public class LocalMilkSaleRate extends BaseModelTxn {
	@Id
	@Size(max = 25)
	private String code; // Society + AI
	@Digits(integer = 4, fraction = 2)
	private BigDecimal rate;
	@Size(max = 3)
	private String unionCode;
	private LocalDate wefDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_local_sale_rate_milk_type_code"))
	private MilkType milkType;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "milk_class_code", foreignKey = @ForeignKey(name = "fk_local_sale_rate_milk_class_code"))
	private MilkClass milkClass;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_local_milk_sale_rate_society_code"))
	@JsonIgnoreProperties(value = { "bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
			"subDistrict", "village", "hamlet" })
	private Society society;

	@Override
	public String getTableName() {
		return "local_milk_sale_rate";
	}

	@Override
	public Object getId() {
		return this.getCode();
	}

	@Override
	public JsonAndTableBuilder getAuditModel(String operation, String user) {
		LocalMilkSaleRateAudit audit = new LocalMilkSaleRateAudit();
		audit.setOperationType(operation);
		audit.setAuditCreatedBy(user);

		audit.setCode(this.getCode());
		audit.setRate(this.getRate());
		audit.setUnionCode(this.getUnionCode());
		audit.setWefDate(this.getWefDate());
		audit.setMilkType(this.getMilkType());
		audit.setMilkClass(this.getMilkClass());
		audit.setSociety(this.getSociety());

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
