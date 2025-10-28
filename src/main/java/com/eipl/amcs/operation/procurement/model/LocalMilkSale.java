package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.*;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.*;
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
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "local_milk_sale")
public class LocalMilkSale extends BaseModelTxn {

    @Id
    @Size(max = 25)
    private String code;
    @Size(max = 45)
    private String invoiceNo;
    private Short consumerType; //
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
    private String voucherNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(name = "fk_local_milk_sale_shift_code"))
    private Shift shift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_local_milk_sale_milk_type_code"))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkClassSerialize.class)
    @JsonDeserialize(using = MilkClassDeserializer.class)
    @JoinColumn(name = "milk_class_code", foreignKey = @ForeignKey(name = "fk_local_milk_sale_milk_class_code"))
    private MilkClass milkClass;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_local_milk_sale_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DockSerialize.class)
    @JsonDeserialize(using = DockDeserializer.class)
    @JoinColumn(name = "dock_no", foreignKey = @ForeignKey(name = "fk_local_milk_sale_dock_no"))
    @JsonIgnoreProperties(value = {"society"})
    private Dock dock;

    @Override
    public String getTableName() {
        return "local_milk_sale";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        LocalMilkSaleAudit audit = new LocalMilkSaleAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setInvoiceNo(this.getInvoiceNo());
        audit.setConsumerType(this.getConsumerType());
        audit.setConsumerCode(this.getConsumerCode());
        audit.setSaleDate(this.getSaleDate());
        audit.setEntryType(this.getEntryType());
        audit.setPaymentMode(this.getPaymentMode());
        audit.setQuantity(this.getQuantity());
        audit.setConvertedQuantity(this.getConvertedQuantity());
        audit.setConvertedQuantityMode(this.getConvertedQuantityMode());
        audit.setRate(this.getRate());
        audit.setAmount(this.getAmount());
        audit.setCash(this.getCash());
        audit.setCoupon(this.getCoupon());
        audit.setCredit(this.getCredit());
        audit.setUnionCode(this.getUnionCode());
        audit.setShift(this.getShift());
        audit.setMilkClass(this.getMilkClass());
        audit.setMilkType(this.getMilkType());
        audit.setSociety(this.getSociety());
        audit.setDock(this.getDock());

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
