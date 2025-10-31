package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.RateTypeDeserializer;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.serialize.RateTypeSerialize;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "society_milk_purchase_rate")
public class SocietyMilkPurchaseRate extends BaseModel {

    @Id
    private String code;
    private String description;
    private Short rateGenMethodCode; // 1-Excel, (1-Manual,2-Auto,3-Excel)
    private String unionCode;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime wefDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_shift_code"))
    private Shift shift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "shift_applicable_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_shift_applicable_code"))
    private Shift shiftApplicable;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = RateTypeSerialize.class)
    @JsonDeserialize(using = RateTypeDeserializer.class)
    @JoinColumn(name = "rate_type_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_rate_type_code"))
    private RateType rateType;

    @Override
    public String getTableName() {
        return "society_milk_purchase_rate";
    }
}
