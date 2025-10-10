package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_milk_purchase_rate_details")
public class MemberMilkPurchaseRateDetail extends BaseModelTxn {

    @Id
    @Size(max = 25)
    private String code;
    @Digits(integer = 2, fraction = 2)
    private BigDecimal fat;
    @Digits(integer = 2, fraction = 2)
    private BigDecimal snf;
    @Digits(integer = 3, fraction = 2)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_details_milk_type_code"))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_details_milk_quality_type_code"))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_milk_purchase_rate_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_details_rate_code"))
    private MemberMilkPurchaseRate memberMilkPurchaseRate;

    @Override
    public String getTableName() {
        return "member_milk_purchase_rate_details";
    }
}
