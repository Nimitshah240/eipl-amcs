package com.eipl.amcs.master.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "product_purchase_rate_audit")
public class ProductPurchaseRateAudit extends BaseModelTxnAudit {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 25)
	private String code;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal rate;
	private Short entryType;
	private LocalDate wefDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "bank", "branch", "state", "district", "subDistrict", "village", "hamlet", "union",
			"plant", "mcc", "bmc", "route" })
	private Society society;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "bank", "branch", "state", "district", "subDistrict", "village", "hamlet" })
	private Union union;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging" })
	private Product product;

	@Override
	public String getTableName() {
		return "product_purchase_rate_audit";
	}

}
