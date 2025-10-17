package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "local_milk_sale_audit")
public class LocalMilkSaleAudit extends BaseModelTxnAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 25)
    private String code;
    @Size(max = 45)
    private String invoiceNo;
    private Short consumerType;
    @Size(max = 25)
    private String consumerCode;
    private LocalDateTime saleDate;
    private Short entryType;
    private Short paymentMode;
    @Digits(integer = 3, fraction = 3)
    private BigDecimal quantity;
    private Short quantityMode;
    @Digits(integer = 3, fraction = 3)
    private BigDecimal convertedQuantity;
    private Short convertedQuantityMode;
    @Digits(integer = 3, fraction = 2)
    private BigDecimal rate;
    @Digits(integer = 5, fraction = 2)
    private BigDecimal amount;
    @Digits(integer = 5, fraction = 2)
    private BigDecimal cash;
    @Digits(integer = 5, fraction = 2)
    private BigDecimal coupon;
    @Digits(integer = 5, fraction = 2)
    private BigDecimal credit;
    @Size(max = 3)
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Shift shift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_class_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkClass milkClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dock_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society"})
    private Dock dock;

    @Override
    public String getTableName() {
        return "local_milk_sale_audit";
    }
}
