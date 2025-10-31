package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.AppConstant;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "society_payment_cycles")
public class SocietyPaymentCycle extends BaseModelTxn {

    @Id
    private String code;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime fromDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime toDate;
    private Integer intervalValue;
    @Column(name = "is_billing")
    private Boolean billing;
    @Column(name = "lock_billing_process")
    private Boolean lockBillingProcess;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_society_payment_cycles_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "from_shift_code", foreignKey = @ForeignKey(name = "fk_society_payment_cycles_from_shift"))
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "to_shift_code", foreignKey = @ForeignKey(name = "fk_society_payment_cycles_to_shift"))
    private Shift toShift;

    @Override
    public String getTableName() {
        return "society_payment_cycles";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        SocietyPaymentCycleAudit audit = new SocietyPaymentCycleAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setFromDate(this.getFromDate());
        audit.setToDate(this.getToDate());
        audit.setIntervalValue(this.getIntervalValue());
        audit.setBilling(this.billing);
        audit.setLockBillingProcess(this.lockBillingProcess);
        audit.setUnionCode(this.getUnionCode());
        audit.setSociety(this.getSociety());
        audit.setFromShift(this.getFromShift());
        audit.setToShift(this.getToShift());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }

    public String toDateShiftString() {
        String sb = getFromDate().toLocalDate().format(AppConstant.DATE_FORMATTER) +
                CommonUtils.getShiftShort(getFromShift()) +
                " - " +
                getToDate().toLocalDate().format(AppConstant.DATE_FORMATTER) +
                CommonUtils.getShiftShort(getToShift());
        return sb;
    }
}
