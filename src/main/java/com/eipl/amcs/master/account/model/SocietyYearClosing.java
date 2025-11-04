package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.FinancialYearDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.FinancialYearSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "society_year_closing")
public class SocietyYearClosing extends BaseModelTxn {

    @Id
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = FinancialYearSerialize.class)
    @JsonDeserialize(using = FinancialYearDeserializer.class)
    @JoinColumn(name = "financial_years_code", foreignKey = @ForeignKey(name = "fk_society_year_closing_financial_years_code"))
    private FinancialYear financialYear;

    @JsonFormat(pattern = "yyyy-MM-dd")
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
