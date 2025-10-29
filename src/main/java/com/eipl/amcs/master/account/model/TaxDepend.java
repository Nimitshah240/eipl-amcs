package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.TaxDetailDeserializer;
import com.eipl.amcs.json.serialize.TaxDetailSerialize;
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
@Table(name = "tax_depends")
public class TaxDepend extends BaseModel {

    @Id
    @Size(max = 10)
    private String code;
    private Short steps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxDetailSerialize.class)
    @JsonDeserialize(using = TaxDetailDeserializer.class)
    @JoinColumn(name = "tax_detail_code", foreignKey = @ForeignKey(name = "fk_tax_depends_tax_detail_code"))
    private TaxDetail taxDetail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = TaxDetailSerialize.class)
    @JsonDeserialize(using = TaxDetailDeserializer.class)
    @JoinColumn(name = "tax_details_code", foreignKey = @ForeignKey(name = "fk_tax_depends_tax_details_code"))
    private TaxDetail taxDetails;

    @Override
    public String getTableName() {
        return "tax_detail";
    }
}
