package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.MemberMilkPurchaseRateDeserializer;
import com.eipl.amcs.json.deserialize.MilkQualityTypeDeserializer;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.serialize.MemberMilkPurchaseRateSerialize;
import com.eipl.amcs.json.serialize.MilkQualityTypeSerialize;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_milk_purchase_rate_details")
public class MemberMilkPurchaseRateDetail extends BaseModelTxn {

    @Id
    private String code;
    @Digits(integer = 2, fraction = 2)
    private BigDecimal fat;
    @Digits(integer = 2, fraction = 2)
    private BigDecimal snf;
    @Digits(integer = 3, fraction = 2)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_details_milk_type_code"))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkQualityTypeSerialize.class)
    @JsonDeserialize(using = MilkQualityTypeDeserializer.class)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_details_milk_quality_type_code"))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberMilkPurchaseRateSerialize.class)
    @JsonDeserialize(using = MemberMilkPurchaseRateDeserializer.class)
    @JoinColumn(name = "member_milk_purchase_rate_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_details_rate_code"))
    private MemberMilkPurchaseRate memberMilkPurchaseRate;

    @Override
    public String getTableName() {
        return "member_milk_purchase_rate_details";
    }
}
