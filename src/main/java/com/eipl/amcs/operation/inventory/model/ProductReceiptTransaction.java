package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.ProductDeserializer;
import com.eipl.amcs.json.deserialize.ProductReceiptDeserializer;
import com.eipl.amcs.json.deserialize.TaxDeserializer;
import com.eipl.amcs.json.deserialize.UnitDeserializer;
import com.eipl.amcs.json.serialize.ProductReceiptSerialize;
import com.eipl.amcs.json.serialize.ProductSerialize;
import com.eipl.amcs.json.serialize.TaxSerialize;
import com.eipl.amcs.json.serialize.UnitSerialize;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.master.inventory.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_receipt_transaction")
public class ProductReceiptTransaction extends BaseModelTxn {
    @Id
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

    private String remark;
    private String unionCode;
    private String societyCode;

    private String batchNo;
    private BigDecimal saleRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductReceiptSerialize.class)
    @JsonDeserialize(using = ProductReceiptDeserializer.class)
    @JoinColumn(name = "grn_no", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_grn_no"))
    @JsonIgnoreProperties(value = {"society", "union", "customer"})
    private ProductReceipt productReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_product_code"))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging"})
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnitSerialize.class)
    @JsonDeserialize(using = UnitDeserializer.class)
    @JoinColumn(name = "unit_code", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_unit_code"))
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxSerialize.class)
    @JsonDeserialize(using = TaxDeserializer.class)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(name = "fk_product_receipt_transaction_tax_code"))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;

    @OneToMany(
            mappedBy = "productReceiptTransaction",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY // Set to LAZY so it doesn't load unless explicitly accessed
    )
    @Getter(AccessLevel.PRIVATE) // Optional: Lombok annotation to keep the getter private if you make one
    @Setter(AccessLevel.PRIVATE) // Optional: Lombok annotation to keep the setter private if you make one
    private Set<ProductReceiptTaxAudit> productReceiptTaxAudits = new HashSet<>();


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
