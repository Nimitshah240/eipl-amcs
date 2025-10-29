package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.BasicTaxDeserializer;
import com.eipl.amcs.json.deserialize.TaxDeserializer;
import com.eipl.amcs.json.serialize.BasicTaxSerialize;
import com.eipl.amcs.json.serialize.TaxSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tax_detail")
public class TaxDetail extends BaseModelTxn {

    @Id
    @Size(max = 10)
    private String code;
    private Short type; // 1-Addition, 2-Deduction
    private Double percentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BasicTaxSerialize.class)
    @JsonDeserialize(using = BasicTaxDeserializer.class)
    @JoinColumn(name = "basic_tax_code", foreignKey = @ForeignKey(name = "fk_tax_detail_basic_tax_code"))
    private BasicTax basicTax;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxSerialize.class)
    @JsonDeserialize(using = TaxDeserializer.class)
    @JoinColumn(name = "tax_code", foreignKey = @ForeignKey(name = "fk_tax_detail_tax_code"))
    @JsonIgnoreProperties(value = {"union"})
    private Tax tax;

    @Override
    public String getTableName() {
        return "tax_detail";
    }
}
