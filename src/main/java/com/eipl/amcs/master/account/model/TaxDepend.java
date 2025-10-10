package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModel;
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
    @JoinColumn(name = "tax_detail_code", foreignKey = @ForeignKey(name = "fk_tax_depends_tax_detail_code"))
    private TaxDetail taxDetail;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_details_code", foreignKey = @ForeignKey(name = "fk_tax_depends_tax_details_code"))
    private TaxDetail taxDetails;

    @Override
    public String getTableName() {
        return "tax_detail";
    }
}
