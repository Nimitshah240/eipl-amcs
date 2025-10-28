package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.deserialize.FinancialYearDeserializer;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.FinancialYearSerialize;
import com.eipl.amcs.serialize.SocietySerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "society_year_closing")
public class SocietyYearClosing extends BaseModelTxn {

    @Id
    @Size(max = 50)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = FinancialYearSerialize.class)
    @JsonDeserialize(using = FinancialYearDeserializer.class)
    @JoinColumn(name = "financial_years_code", foreignKey = @ForeignKey(name = "fk_society_year_closing_financial_years_code"))
    private FinancialYear financialYear;

    private LocalDate closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_society_year_closing_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    private String unionCode;

    @Override
    public String getTableName() {
        return "society_year_closing";
    }
}
