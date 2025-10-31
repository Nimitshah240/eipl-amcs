package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Dock;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "milk_collection_audit")
public class MilkCollectionAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JoinColumn(name = "society_payment_cycle_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SocietyPaymentCycle societyPaymentCycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkType milkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkQualityTypeSerialize.class)
    @JsonDeserialize(using = MilkQualityTypeDeserializer.class)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MilkQualityType milkQualityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DockSerialize.class)
    @JsonDeserialize(using = DockDeserializer.class)
    @JoinColumn(name = "dock_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Dock dock;

    @Override
    public String getTableName() {
        return "milk_collection_audit";
    }

}
