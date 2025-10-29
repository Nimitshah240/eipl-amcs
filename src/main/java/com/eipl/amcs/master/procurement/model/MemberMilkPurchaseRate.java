package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.RateTypeDeserializer;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.json.serialize.RateTypeSerialize;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_milk_purchase_rate")
public class MemberMilkPurchaseRate extends BaseModel {

    @Id
    @Size(max = 15)
    private String code;
    @Size(max = 255)
    private String description;
    private Short rateGenMethodCode; // 1-Excel, (1-Manual,2-Auto,3-Excel)
    @Size(max = 3)
    private String unionCode;
    private LocalDateTime wefDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_shift_code"))
    private Shift shift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_applicable_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_shift_applicable_code"))
    private Shift shiftApplicable;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = RateTypeSerialize.class)
    @JsonDeserialize(using = RateTypeDeserializer.class)
    @JoinColumn(name = "rate_type_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_rate_type_code"))
    private RateType rateType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_member_milk_purchase_rate_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    @Override
    public String getTableName() {
        return "member_milk_purchase_rate";
    }
}
