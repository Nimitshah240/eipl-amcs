package com.eipl.amcs.master.geo.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "villages")
public class Village extends BaseModel {

    @Id
    @Size(max = 6)
    private String code;
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_villages_sub_districts_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;

    @Override
    public String getTableName() {
        return "villages";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
