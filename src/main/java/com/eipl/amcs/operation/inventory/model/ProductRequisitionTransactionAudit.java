package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
@Table(name = "product_requisition_transaction_audit")
public class ProductRequisitionTransactionAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String approvedBy;
    private LocalDate approvedDate;
    private BigDecimal approvedQuantity;
    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private BigDecimal discountAmount;
    private int isApproved;
    private Boolean isCancel;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal quantity;
    private LocalDateTime requisitionDate;
    private LocalDate expectedDeliveryDate;
    private String schemeAddType;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_requisition_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private ProductRequisition productRequisition;
    private String productSchemeCode;
    private int passonToMember;
    private String unionCode;
    private String societyCode;

}
