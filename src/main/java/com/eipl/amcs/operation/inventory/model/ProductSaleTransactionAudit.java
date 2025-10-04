package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
@Table(name = "product_sale_transaction_audit")
public class ProductSaleTransactionAudit extends BaseModelTxnAudit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max=40)
	private String invoiceTxnNo;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal amount;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal netAmount;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal discount;
	@Digits(integer = 7, fraction = 3)
	private BigDecimal quantity;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal rate;
	@Digits(integer = 8, fraction = 2)
	private BigDecimal taxAmount;
	@Column(name = "is_loose_sale")
	private Boolean looseSale;
	
	@Size(max=10)
	private String taxCode;
	@Size(max=10)
	private String unionCode;
	@Size(max=10)
	private String societyCode;	
	private Integer unitCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = {"dock","union","society"})
	private ProductSale productSale;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "conversionUnit", "primaryUom","productGroup","tax","secondaryPackaging" })
	private Product product;
	
	@Override
	public String getTableName() {
		return "product_sale_transaction_audit";
	}
}
