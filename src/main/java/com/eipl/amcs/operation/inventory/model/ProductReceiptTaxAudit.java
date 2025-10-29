package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.deserialize.ProductReceiptDeserializer;
import com.eipl.amcs.deserialize.ProductReceiptTransactionDeserializer;
import com.eipl.amcs.deserialize.ProductRequisitionTransactionDeserializer;
import com.eipl.amcs.deserialize.TaxDetailDeserializer;
import com.eipl.amcs.master.account.model.TaxDetail;
import com.eipl.amcs.operation.inventory.service.ProductReceiptTransactionService;
import com.eipl.amcs.serialize.ProductReceiptSerialize;
import com.eipl.amcs.serialize.ProductReceiptTransactionSerialize;
import com.eipl.amcs.serialize.ProductRequisitionTransactionSerialize;
import com.eipl.amcs.serialize.TaxDetailSerialize;
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
@Table(name = "product_receipt_tax_audit")
public class ProductReceiptTaxAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 40)
    private String code;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal value;
    @Size(max = 7)
    private String societyCode;
    @Size(max = 3)
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductReceiptSerialize.class)
    @JsonDeserialize(using = ProductReceiptDeserializer.class)
    @JoinColumn(name = "grn_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union", "customer"})
    private ProductReceipt productReceipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductReceiptTransactionSerialize.class)
    @JsonDeserialize(using = ProductReceiptTransactionDeserializer.class)
    @JoinColumn(name = "grn_txn_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"productReceipt", "product", "unit", "tax"})
    private ProductReceiptTransaction productReceiptTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxDetailSerialize.class)
    @JsonDeserialize(using = TaxDetailDeserializer.class)
    @JoinColumn(name = "tax_detail_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"basicTax", "tax"})
    private TaxDetail taxDetail;

    @Override
    public String getTableName() {
        return "product_receipt_tax_audit";
    }
}
