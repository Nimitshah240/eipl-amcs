package com.eipl.amcs.master.global.model;

import com.eipl.amcs.base.BaseModel;
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
@Table(name = "unit_conversions")
public class UnitConversion extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    @Digits(integer = 5, fraction = 2)
    private BigDecimal conversionFactor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_unit", foreignKey = @ForeignKey(name = "fk_unit_conversions_units_from_unit"))
    private Unit fromUnit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_unit", foreignKey = @ForeignKey(name = "fk_unit_conversions_units_to_unit"))
    private Unit toUnit;

    @Override
    public String getTableName() {
        return "unit_conversions";
    }
}
