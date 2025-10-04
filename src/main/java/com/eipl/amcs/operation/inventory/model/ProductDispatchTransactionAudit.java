package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
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
public class ProductDispatchTransactionAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private LocalDate dispatchOnDate;
    private BigDecimal dispatchQty;
    private BigDecimal rate;
    private String productSchemeCode;
    private int passonToMember;
    private String status;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challan_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//	@JsonIgnoreProperties(value = { "society", "union" })
    private ProductDispatch productDispatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//	@JsonIgnoreProperties(value = { "society", "union" })
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//	@JsonIgnoreProperties(value = { "society", "union" })
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_requisition_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//	@JsonIgnoreProperties(value = { "society", "union" })
    private ProductRequisition productRequisition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_requisition_transaction_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
//	@JsonIgnoreProperties(value = { "society", "union" })
    private ProductRequisitionTransaction productRequisitionTransaction;

}
