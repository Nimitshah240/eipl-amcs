package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
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
@Table(name = "product_receipt")
public class ProductReceipt extends BaseModelTxn {
	@Id
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
	@JoinColumn(name = "customer_code", foreignKey = @ForeignKey(name = "fk_product_receipt_customer_code"))
	@JsonIgnoreProperties(value = { "society", "union" })
	private Customer customer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_product_receipt_union_code"))
	@JsonIgnoreProperties(value = { "bank", "branch", "state", "district", "subDistrict", "village", "hamlet" })
	private Union union;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_product_receipt_society_code"))
	@JsonIgnoreProperties(value = { "bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
			"subDistrict", "village", "hamlet" })
	private Society society;

	@Override
	public String getTableName() {
		return "product_receipt";
	}

	@Override
	public Object getId() {
		return this.getGrnNo();
	}

	@Override
	public JsonAndTableBuilder getAuditModel(String operation, String user) {
		ProductReceiptAudit audit = new ProductReceiptAudit();
		audit.setOperationType(operation);
		audit.setAuditCreatedBy(user);

		audit.setGrnDate(this.getGrnDate());
		audit.setGrnNo(this.getGrnNo());
		audit.setChallanDate(this.getChallanDate());
		audit.setChallanNo(this.getChallanNo());
		audit.setDescription(this.getDescription());
		audit.setVoucherNo(this.getVoucherNo());
		audit.setAmount(this.getAmount());
		audit.setTaxAmount(this.getTaxAmount());
		audit.setDiscount(this.getDiscount());
		audit.setNetAmount(this.getNetAmount());
		audit.setCustomer(this.getCustomer());
		audit.setUnion(this.getUnion());
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
