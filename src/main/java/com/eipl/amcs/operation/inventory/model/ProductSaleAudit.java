package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_sale_audit")
public class ProductSaleAudit extends BaseModelTxnAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 35)
    private String invoiceNo;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal amount;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal discount;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal netAmount;
    @Digits(integer = 8, fraction = 2)
    private BigDecimal taxAmount;

    private LocalDate invoiceDate;
    private LocalDate deductionStartDate;
    private String voucherNo;
    private Short noOfInstallments;
    private Short paymentMode;
    private Short consumerType;
    private Short transactionType;
    @Size(max = 15)
    private String consumerCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dock_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society"})
    private Dock dock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @Override
    public String getTableName() {
        return "product_sale_audit";
    }
}
