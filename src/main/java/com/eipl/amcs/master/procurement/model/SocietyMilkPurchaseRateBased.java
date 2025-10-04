package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.operation.model.Formula;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "society_milk_purchase_rate_based")
public class SocietyMilkPurchaseRateBased extends BaseModelTxn {
    @Id
    private String code; // Purchase Rate Code + AI

    private int rateType; // 1-FAT, 2-FAT+SNF
    private int qualityParam; // 1-FAT, 2-SNF, 3-CLR, 4-TS
    @Digits(integer = 2, fraction = 2)
    private BigDecimal startVal;
    @Digits(integer = 2, fraction = 2)
    private BigDecimal endVal;
    @Digits(integer = 3, fraction = 2)
    private BigDecimal kgRate;

    private int deductionType; //0-NA, 1-Value Addition, 2-Value Deduction,3-Percentage Addition, 4-Percentage Deduction
    private int refType; //0-NA, 1-Fixed Point, 2-Actual
    private BigDecimal val;
    private BigDecimal fixedPoint;
    private int step;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_based_formula_code"))
    @JsonIgnoreProperties(value = {"union"})
    private Formula formula;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_based_milk_type_code"))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_based_milk_quality_type_code"))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_milk_purchase_rate_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_based_rate_code"))
    private SocietyMilkPurchaseRate societyMilkPurchaseRate;

    @Override
    public String getTableName() {
        return "society_milk_purchase_rate_based";
    }
}
