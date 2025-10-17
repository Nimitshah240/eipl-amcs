package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.inventory.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_requisition_transaction")
public class ProductRequisitionTransaction extends BaseModelTxn {
    @Id
    private String code;
    private String approvedBy;
    private LocalDate approvedDate;
    private BigDecimal approvedQuantity;
    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private BigDecimal discountAmount;
    private int isApproved;
    @Column(name = "is_cancel")
    private Boolean cancel;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal quantity;
    private LocalDateTime requisitionDate;
    private LocalDate expectedDeliveryDate;
    private String schemeAddType;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(name = "fk_product_requisition_transaction_product_code"))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_requisition_code", foreignKey = @ForeignKey(name = "fk_product_requisition_transaction_product_requisition_code"))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private ProductRequisition productRequisition;
    private String productSchemeCode;
    private int passonToMember;
    private String unionCode;
    private String societyCode;

    @Override
    public String getTableName() {
        return "product_requisition_transaction";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductRequisitionTransactionAudit audit = new ProductRequisitionTransactionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setApprovedBy(this.getApprovedBy());
        audit.setApprovedDate(this.getApprovedDate());
        audit.setApprovedQuantity(this.getApprovedQuantity());
        audit.setUnionCode(this.getUnionCode());
        audit.setCancelledAt(this.getCancelledAt());
        audit.setCancelledBy(this.getCancelledBy());
        audit.setDiscountAmount(this.getDiscountAmount());
        audit.setIsApproved(this.getIsApproved());
        audit.setIsCancel(this.getCancel());
        audit.setAmount(this.getAmount());
        audit.setRate(this.getRate());
        audit.setQuantity(this.getQuantity());
        audit.setRequisitionDate(this.getRequisitionDate());
        audit.setExpectedDeliveryDate(this.getExpectedDeliveryDate());
        audit.setSchemeAddType(this.getSchemeAddType());
        audit.setStatus(this.getStatus());
        audit.setProduct(this.getProduct());
        audit.setProductRequisition(this.getProductRequisition());
        audit.setProductSchemeCode(this.getProductSchemeCode());
        audit.setPassonToMember(this.getPassonToMember());
        audit.setSocietyCode(this.getSocietyCode());


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
