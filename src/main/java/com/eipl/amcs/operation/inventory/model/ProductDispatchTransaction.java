package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.*;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_dispatch_transaction")
public class ProductDispatchTransaction extends BaseModelTxn {
    @Id
    private String code;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private LocalDate dispatchDate;
    private BigDecimal dispatchQty;
    private BigDecimal rate;
    private String productSchemeCode;
    private int passonToMember;
    private String status;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductDispatchSerialize.class)
    @JsonDeserialize(using = ProductDispatchDeserializer.class)
    @JoinColumn(name = "challan_no", foreignKey = @ForeignKey(name = "fk_product_dispatch_transaction_challan_no"))
    @JsonIgnoreProperties(value = {"society", "route"})
    private ProductDispatch productDispatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_product_dispatch_transaction_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "fk_product_dispatch_transaction_product_code"))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductRequisitionSerialize.class)
    @JsonDeserialize(using = ProductRequisitionDeserializer.class)
    @JoinColumn(name = "product_requisition_code", foreignKey = @ForeignKey(name = "fk_product_dispatch_transaction_product_requisition_code"))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private ProductRequisition productRequisition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductRequisitionTransactionSerialize.class)
    @JsonDeserialize(using = ProductRequisitionTransactionDeserializer.class)
    @JoinColumn(name = "product_requisition_transaction_code", foreignKey = @ForeignKey(name = "fk_product_dispatch_transaction_product_requisition_transaction_code"))
    @JsonIgnoreProperties(value = {"society", "productRequisition", "product"})
    private ProductRequisitionTransaction productRequisitionTransaction;

    @Override
    public String getTableName() {
        return "product_dispatch_transaction";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductDispatchTransactionAudit audit = new ProductDispatchTransactionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setAmount(this.getAmount());
        audit.setDiscountAmount(this.getDiscountAmount());
        audit.setSociety(this.getSociety());
        audit.setDispatchOnDate(this.getDispatchDate());
        audit.setDispatchQty(this.getDispatchQty());
        audit.setUnionCode(this.getUnionCode());
        audit.setRate(this.getRate());
        audit.setProductSchemeCode(this.getProductSchemeCode());
        audit.setPassonToMember(this.getPassonToMember());
        audit.setProduct(this.getProduct());
        audit.setProductRequisitionTransaction(this.getProductRequisitionTransaction());
        audit.setProductRequisition(this.getProductRequisition());
        audit.setProductDispatch(this.getProductDispatch());
        audit.setStatus(this.getStatus());


        audit.setCreatedAt(this.getCreatedAt());
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
