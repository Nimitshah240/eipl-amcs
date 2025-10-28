package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.deserialize.MilkQualityTypeDeserializer;
import com.eipl.amcs.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.deserialize.SocietyMilkPurchaseRateDeserializer;
import com.eipl.amcs.master.global.model.MilkQualityType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.serialize.MilkQualityTypeSerialize;
import com.eipl.amcs.serialize.MilkTypeSerialize;
import com.eipl.amcs.serialize.SocietyMilkPurchaseRateSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "society_milk_purchase_rate_details")
public class SocietyMilkPurchaseRateDetail extends BaseModelTxn {

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
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_details_milk_type_code"))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkQualityTypeSerialize.class)
    @JsonDeserialize(using = MilkQualityTypeDeserializer.class)
    @JoinColumn(name = "milk_quality_type_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_details_milk_quality_type_code"))
    private MilkQualityType milkQualityType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietyMilkPurchaseRateSerialize.class)
    @JsonDeserialize(using = SocietyMilkPurchaseRateDeserializer.class)
    @JoinColumn(name = "society_milk_purchase_rate_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_details_rate_code"))
    private SocietyMilkPurchaseRate societyMilkPurchaseRate;

    @Override
    public String getTableName() {
        return "society_milk_purchase_rate_details";
    }
}
