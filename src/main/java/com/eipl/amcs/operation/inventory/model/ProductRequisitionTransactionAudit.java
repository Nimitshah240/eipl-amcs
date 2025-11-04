package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.ProductDeserializer;
import com.eipl.amcs.json.deserialize.ProductRequisitionDeserializer;
import com.eipl.amcs.json.serialize.ProductRequisitionSerialize;
import com.eipl.amcs.json.serialize.ProductSerialize;
import com.eipl.amcs.master.inventory.model.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvedDate;
    private BigDecimal approvedQuantity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private BigDecimal discountAmount;
    private int isApproved;
    private Boolean isCancel;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal quantity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime requisitionDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedDeliveryDate;
    private String schemeAddType;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductSerialize.class)
    @JsonDeserialize(using = ProductDeserializer.class)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductRequisitionSerialize.class)
    @JsonDeserialize(using = ProductRequisitionDeserializer.class)
    @JoinColumn(name = "product_requisition_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"conversionUnit", "primaryUom", "productGroup", "tax", "secondaryPackaging", "union", "society"})
    private ProductRequisition productRequisition;
    private String productSchemeCode;
    private int passonToMember;
    private String unionCode;
    private String societyCode;
}
