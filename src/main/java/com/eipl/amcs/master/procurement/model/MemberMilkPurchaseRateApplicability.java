package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.MemberMilkPurchaseRateDeserializer;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.MemberMilkPurchaseRateSerialize;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
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
@Table(name = "member_milk_purchase_rate_applicability")
public class MemberMilkPurchaseRateApplicability extends BaseModelTxn {

    @Id
    private String code;
    private LocalDateTime wefDate;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_app_shift_code"))
    private Shift shift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberMilkPurchaseRateSerialize.class)
    @JsonDeserialize(using = MemberMilkPurchaseRateDeserializer.class)
    @JoinColumn(name = "member_milk_purchase_rate_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_app_rate_code"))
    private MemberMilkPurchaseRate memberMilkPurchaseRate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_app_society_code"))
    private Society society;

    @Override
    public String getTableName() {
        return "member_milk_purchase_rate_applicability";
    }
}
