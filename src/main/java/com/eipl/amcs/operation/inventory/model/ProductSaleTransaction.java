package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.ProductDeserializer;
import com.eipl.amcs.json.deserialize.ProductSaleDeserializer;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.json.serialize.ProductSaleSerialize;
import com.eipl.amcs.json.serialize.ProductSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "product_sale_transaction")
public class ProductSaleTransaction extends BaseModelTxn {
    @Id
    @Size(max = 40)
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

    @Size(max = 10)
    private String taxCode;
    @Size(max = 10)
    private String unionCode;
    @Size(max = 10)
    private String societyCode;
    private Integer unitCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSaleSerialize.class)
    @JsonDeserialize(using = ProductSaleDeserializer.class)
    @JoinColumn(name = "invoice_no", foreignKey = @ForeignKey(name = "fk_product_sale_transaction_invoice_no"))
    @JsonIgnoreProperties(value = {"dock", "union", "society"})
    private ProductSale productSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "fk_product_sale_transaction_product_code"))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private Product product;

    @Override
    public String getTableName() {
        return "product_sale_transaction";
    }


    @Override
    public Object getId() {
        return this.getInvoiceTxnNo();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductSaleTransactionAudit audit = new ProductSaleTransactionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setInvoiceTxnNo(this.getInvoiceTxnNo());
        audit.setAmount(this.getAmount());
        audit.setNetAmount(this.getNetAmount());
        audit.setDiscount(this.getDiscount());
        audit.setQuantity(this.getQuantity());
        audit.setRate(this.getRate());
        audit.setTaxAmount(this.getTaxAmount());
        audit.setLooseSale(this.getLooseSale());
        audit.setTaxCode(this.getTaxCode());
        audit.setUnionCode(this.getUnionCode());
        audit.setSocietyCode(this.getSocietyCode());
        audit.setUnitCode(this.getUnitCode());
        audit.setProductSale(this.getProductSale());
        audit.setProduct(this.getProduct());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }

    public void setProductSaleToMember(ProductSale productSale) {
        this.productSale = productSale;
    }
}
