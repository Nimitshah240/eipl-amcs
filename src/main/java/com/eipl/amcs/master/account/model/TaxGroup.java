package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tax_group")
public class TaxGroup extends BaseModel {

    @Id
    private Integer code;
    private String name;
    private String unionCode;
    private String xCol4;
    private String xCol5;

    @Override
    public String getTableName() {
        return "tax_group";
    }
}
