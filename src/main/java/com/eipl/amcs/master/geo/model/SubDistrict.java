package com.eipl.amcs.master.geo.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.DistrictDeserializer;
import com.eipl.amcs.json.serialize.DistrictSerialize;
import com.eipl.amcs.utils.CommonUtils;
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
@Table(name = "sub_districts")
public class SubDistrict extends BaseModel {

    @Id
    @Size(max = 5)
    private String code;
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_sub_districts_districts_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;

    @Override
    public String getTableName() {
        return "sub_districts";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
