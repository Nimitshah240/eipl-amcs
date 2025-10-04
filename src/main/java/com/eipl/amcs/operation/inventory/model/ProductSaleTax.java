package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.account.model.TaxDetail;
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
@Table(name = "product_sale_tax")
public class ProductSaleTax extends BaseModelTxn {
    @Id
    @Size(max = 35)
    private String code;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal value;
    @Size(max = 10)
    private String unionCode;
    @Size(max = 10)
    private String societyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_txn_no", foreignKey = @ForeignKey(name = "fk_product_sale_tax_invoice_transaction_no"))
    @JsonIgnoreProperties(value = {"product", "productSale"})
    private ProductSaleTransaction productSaleTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_no", foreignKey = @ForeignKey(name = "fk_product_sale_tax_invoice_no"))
    @JsonIgnoreProperties(value = {"dock", "union", "society"})
    private ProductSale productSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_detail_id", foreignKey = @ForeignKey(name = "fk_product_sale_tax_tax_detail_id"))
    @JsonIgnoreProperties(value = {"basicTax", "tax"})
    private TaxDetail taxDetail;


    @Override
    public String getTableName() {
        return "product_sale_tax";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductSaleTaxAudit audit = new ProductSaleTaxAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setValue(this.getValue());
        audit.setUnionCode(this.getUnionCode());
        audit.setSocietyCode(this.getSocietyCode());
        audit.setProductSale(this.getProductSale());
        audit.setProductSaleTransaction(this.getProductSaleTransaction());
        audit.setTaxDetail(this.getTaxDetail());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }


    public ProductSaleTransaction getProductSaleToMemberTransaction() {
        return productSaleTransaction;
    }

    public void setProductSaleToMemberTransaction(ProductSaleTransaction productSaleToMemberTransaction) {
        this.productSaleTransaction = productSaleToMemberTransaction;
    }

    public ProductSale getProductSaleToMember() {
        return productSale;
    }

    public void setProductSaleToMember(ProductSale productSaleToMember) {
        this.productSale = productSaleToMember;
    }
}
