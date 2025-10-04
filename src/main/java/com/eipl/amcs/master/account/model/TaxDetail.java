package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tax_detail")
public class TaxDetail extends BaseModelTxn {

	@Id
	@Size(max = 10)
	private String code;
	private Short type; // 1-Addition, 2-Deduction
	private Double percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "basic_tax_code", foreignKey = @ForeignKey(name = "fk_tax_detail_basic_tax_code"))
	private BasicTax basicTax;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_code", foreignKey = @ForeignKey(name = "fk_tax_detail_tax_code"))
	@JsonIgnoreProperties(value = {"union"})
	private Tax tax;

	@Override
	public String getTableName() {
		return "tax_detail";
	}
}
