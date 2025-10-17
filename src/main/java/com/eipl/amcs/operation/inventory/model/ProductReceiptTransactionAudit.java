package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
@Table(name = "product_receipt_transaction_audit")
public class ProductReceiptTransactionAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JoinColumn(name = "grn_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union", "customer"})
    private ProductReceipt productReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging"})
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;

    @Override
    public String getTableName() {
        return "product_receipt_transaction_audit";
    }
}
