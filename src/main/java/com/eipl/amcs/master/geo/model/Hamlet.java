package com.eipl.amcs.master.geo.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.VillageDeserializer;
import com.eipl.amcs.json.serialize.VillageSerialize;
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
@Table(name = "hamlets")
public class Hamlet extends BaseModel {

    @Id
    @Size(max = 8)
    private String code;
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_hamlets_villages_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;

    @Override
    public String getTableName() {
        return "hamlets";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
