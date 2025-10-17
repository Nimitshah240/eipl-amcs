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
@Table(name = "product_receipt_tax")
public class ProductReceiptTax extends BaseModelTxn {

    @Id
    @Size(max = 40)
    private String code;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal value;
    @Size(max = 7)
    private String societyCode;
    @Size(max = 3)
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_no", foreignKey = @ForeignKey(name = "fk_product_receipt_tax_grn_no"))
    @JsonIgnoreProperties(value = {"society", "union", "customer"})
    private ProductReceipt productReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_txn_no", foreignKey = @ForeignKey(name = "fk_product_receipt_tax_grn_txn_no"))
    @JsonIgnoreProperties(value = {"productReceipt", "product", "unit", "tax"})
    private ProductReceiptTransaction productReceiptTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_detail_code", foreignKey = @ForeignKey(name = "fk_product_receipt_tax_tax_detail_code"))
    @JsonIgnoreProperties(value = {"basicTax", "tax"})
    private TaxDetail taxDetail;

    @Override
    public String getTableName() {
        return "product_receipt_tax";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductReceiptTaxAudit audit = new ProductReceiptTaxAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setValue(this.getValue());
        audit.setSocietyCode(this.getSocietyCode());
        audit.setUnionCode(this.getUnionCode());
        audit.setProductReceipt(this.getProductReceipt());
        audit.setProductReceiptTransaction(this.getProductReceiptTransaction());
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
}
