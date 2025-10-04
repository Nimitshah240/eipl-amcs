package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_receipt_audit")
public class ProductReceiptAudit extends BaseModelTxnAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 35)
	private String grnNo;
	private LocalDate grnDate;
	private LocalDate challanDate;
	@Size(max = 35)
	private String challanNo;
	@Size(max = 500)
	private String description;
	@Size(max = 35)
	private String voucherNo;
	private BigDecimal amount;
	private BigDecimal discount;
	private BigDecimal taxAmount;
	private BigDecimal netAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "society", "union" })
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "bank", "branch", "state", "district", "subDistrict", "village", "hamlet" })
	private Union union;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
			"subDistrict", "village", "hamlet" })
	private Society society;

	@Override
	public String getTableName() {
		return "product_receipt_audit";
	}
}
