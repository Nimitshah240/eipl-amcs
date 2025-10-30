package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductReceiptSerialize.class)
    @JsonDeserialize(using = ProductReceiptDeserializer.class)
    @JoinColumn(name = "grn_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union", "customer"})
    private ProductReceipt productReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging"})
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnitSerialize.class)
    @JsonDeserialize(using = UnitDeserializer.class)
    @JoinColumn(name = "unit_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxSerialize.class)
    @JsonDeserialize(using = TaxDeserializer.class)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;

    @Override
    public String getTableName() {
        return "product_receipt_transaction_audit";
    }
}
