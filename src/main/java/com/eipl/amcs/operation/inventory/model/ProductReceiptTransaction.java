package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_receipt_transaction")
public class ProductReceiptTransaction extends BaseModelTxn {
	@Id
	@Size(max = 40)
	private String grnTxnNo;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal amount;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal discount;
	private Integer quantity;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal rate;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal taxAmount;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal netAmount;
	
	@Size(max = 100)
	private String remark;
	@Size(max = 3)
	private String unionCode;
	@Size(max = 7)
	private String societyCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_no", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_grn_no"))
	@JsonIgnoreProperties(value = { "society", "union","customer" })
	private ProductReceipt productReceipt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_product_code"))
	@JsonIgnoreProperties(value = { "conversionUnit", "primaryUom","productGroup","tax","secondaryPackaging" })
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_code", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_unit_code"))
	private Unit unit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_code", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_tax_code"))
	@JsonIgnoreProperties(value = { "union"})
	private Tax tax;

	@Override
	public String getTableName() {
		return "product_receipt_transaction";
	}
	
	@Override
	public Object getId() {
		return this.getGrnTxnNo();
	}

	@Override
	public JsonAndTableBuilder getAuditModel(String operation, String user) {
		ProductReceiptTransactionAudit audit = new ProductReceiptTransactionAudit();
		audit.setOperationType(operation);
		audit.setAuditCreatedBy(user);

		audit.setGrnTxnNo(this.getGrnTxnNo());
		audit.setAmount(this.getAmount());
		audit.setDiscount(this.getDiscount());
		audit.setQuantity(this.getQuantity());
		audit.setRate(this.getRate());
		audit.setTaxAmount(this.getTaxAmount());
		audit.setTax(this.getTax());
		audit.setNetAmount(this.getNetAmount());
		audit.setRemark(this.getRemark());
		audit.setUnionCode(this.getUnionCode());
		audit.setUnit(this.getUnit());
		audit.setSocietyCode(this.getSocietyCode());
		audit.setProduct(this.getProduct());
		audit.setProductReceipt(this.getProductReceipt());

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
