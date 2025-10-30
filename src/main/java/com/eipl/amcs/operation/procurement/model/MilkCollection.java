package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "milk_collection")
public class MilkCollection extends BaseModelTxn {

    @Id
    @Size(max = 40)
    private String code;
    private int sampleNo;
    private LocalDateTime collectionDate;
    private BigDecimal fat;
    private BigDecimal snf;
    private BigDecimal clr;
    private BigDecimal water;
    private BigDecimal density;
    private BigDecimal lectose;
    private BigDecimal protein;
    private BigDecimal rtpl;
    private BigDecimal qty;
    private BigDecimal amount;
    @Column(name = "is_weight_auto")
    private boolean weightAuto;
    @Column(name = "is_quality_auto")
    private boolean qualityAuto;
    @Column(name = "is_avg_param")
    private boolean avgParam;
    private LocalDateTime qualityAt;
    private LocalDateTime weightAt;
    private String voucherNo;
    private String rateCode;
    private String wsCode;
    private String analyserCode;
    private String unionCode;
    private int qtyMode;
    private BigDecimal convertedQty;
    private int convertedQtyMode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietyPaymentCycleSerialize.class)
    @JsonDeserialize(using = SocietyPaymentCycleDeserializer.class)
    @JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(name = "fk_milk_collection_society_payment_cycle_code"))
    @JsonIgnoreProperties(value = {"society", "toShift", "fromShift"})
    private SocietyPaymentCycle societyPaymentCycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_milk_collection_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(name = "fk_milk_collection_shift_code"))
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_milk_collection_milk_type_code"))
    private MilkType milkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkQualityTypeSerialize.class)
    @JsonDeserialize(using = MilkQualityTypeDeserializer.class)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_milk_collection_milk_quality_code"))
    private MilkQualityType milkQualityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_milk_collection_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DockSerialize.class)
    @JsonDeserialize(using = DockDeserializer.class)
    @JoinColumn(name = "dock_no", foreignKey = @ForeignKey(name = "fk_milk_collection_dock_code"))
    @JsonIgnoreProperties(value = {"society"})
    private Dock dock;

    @Transient
    @JsonIgnore
    private BigDecimal newRate;
    @JsonIgnore
    @Transient
    private BigDecimal newAmount;
    private String xCol4;
    private String xCol5;

    @Override
    public String getTableName() {
        return "milk_collection";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MilkCollectionAudit audit = new MilkCollectionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setSampleNo(this.getSampleNo());
        audit.setCollectionDate(this.getCollectionDate());
        audit.setFat(this.getFat());
        audit.setSnf(this.getSnf());
        audit.setClr(this.getClr());
        audit.setQty(this.getQty());
        audit.setWater(this.getWater());
        audit.setDensity(this.getDensity());
        audit.setLectose(this.getLectose());
        audit.setProtein(this.getProtein());
        audit.setRtpl(this.getRtpl());
        audit.setAmount(this.getAmount());
        audit.setWeightAuto(this.weightAuto);
        audit.setQualityAuto(this.qualityAuto);
        audit.setAvgParam(this.avgParam);
        audit.setQualityAt(this.getQualityAt());
        audit.setWeightAt(this.getWeightAt());
        audit.setVoucherNo(this.getVoucherNo());
        audit.setRateCode(this.getRateCode());
        audit.setWsCode(this.getWsCode());
        audit.setAnalyserCode(this.getAnalyserCode());
        audit.setUnionCode(this.getUnionCode());
        audit.setQtyMode(this.getQtyMode());
        audit.setConvertedQty(this.getConvertedQty());
        audit.setConvertedQtyMode(this.getConvertedQtyMode());
        audit.setSocietyPaymentCycle(this.getSocietyPaymentCycle());
        audit.setMember(this.getMember());
        audit.setShift(this.getShift());
        audit.setMilkType(this.getMilkType());
        audit.setMilkQualityType(this.getMilkQualityType());
        audit.setDock(this.getDock());
        audit.setSociety(this.getSociety());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }

    public String getxCol4() {
        return xCol4;
    }

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }

    public String getxCol5() {
        return xCol5;
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }

}
