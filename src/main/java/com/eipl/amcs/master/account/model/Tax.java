package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tax")
public class Tax extends BaseModel {

    @Id
    private String code;
    private String name;
    private String nameLocal;
    private Integer entryType; //1-union

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_tax_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @Override
    public String getTableName() {
        return "tax";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
