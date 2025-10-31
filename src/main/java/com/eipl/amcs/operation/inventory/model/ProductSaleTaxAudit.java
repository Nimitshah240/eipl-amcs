package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.ProductSaleDeserializer;
import com.eipl.amcs.json.deserialize.ProductSaleTransactionDeserializer;
import com.eipl.amcs.json.deserialize.TaxDetailDeserializer;
import com.eipl.amcs.json.serialize.ProductSaleSerialize;
import com.eipl.amcs.json.serialize.ProductSaleTransactionSerialize;
import com.eipl.amcs.json.serialize.TaxDetailSerialize;
import com.eipl.amcs.master.account.model.TaxDetail;
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
@Table(name = "product_sale_tax_audit")
public class ProductSaleTaxAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal value;
    private String unionCode;
    private String societyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSaleTransactionSerialize.class)
    @JsonDeserialize(using = ProductSaleTransactionDeserializer.class)
    @JoinColumn(name = "invoice_txn_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"product", "productSale"})
    private ProductSaleTransaction productSaleTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSaleSerialize.class)
    @JsonDeserialize(using = ProductSaleDeserializer.class)
    @JoinColumn(name = "invoice_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"dock", "union", "society"})
    private ProductSale productSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxDetailSerialize.class)
    @JsonDeserialize(using = TaxDetailDeserializer.class)
    @JoinColumn(name = "tax_detail_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"basicTax", "tax"})
    private TaxDetail taxDetail;


    @Override
    public String getTableName() {
        return "product_sale_tax_audit";
    }
}
