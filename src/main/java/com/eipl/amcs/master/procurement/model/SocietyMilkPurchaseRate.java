package com.eipl.amcs.master.procurement.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.master.global.model.RateType;
import com.eipl.amcs.master.global.model.Shift;
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
@Table(name = "society_milk_purchase_rate")
public class SocietyMilkPurchaseRate extends BaseModel {

    @Id
    @Size(max = 25)
    private String code;
    @Size(max = 255)
    private String description;
    private Short rateGenMethodCode; // 1-Excel, (1-Manual,2-Auto,3-Excel)
    @Size(max = 3)
    private String unionCode;
    private LocalDateTime wefDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_shift_code"))
    private Shift shift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_applicable_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_shift_applicable_code"))
    private Shift shiftApplicable;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_type_code", foreignKey = @ForeignKey(name = "fk_society_milk_purchase_rate_rate_type_code"))
    private RateType rateType;

    @Override
    public String getTableName() {
        return "society_milk_purchase_rate";
    }
}
