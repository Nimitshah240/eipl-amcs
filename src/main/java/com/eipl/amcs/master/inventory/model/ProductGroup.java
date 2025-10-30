package com.eipl.amcs.master.inventory.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.UnitDeserializer;
import com.eipl.amcs.json.serialize.UnitSerialize;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.utils.CommonUtils;
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
@Table(name = "product_groups")
public class ProductGroup extends BaseModel {

    @Id
    private Integer code;
    private String name;
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnitSerialize.class)
    @JsonDeserialize(using = UnitDeserializer.class)
    @JoinColumn(name = "base_unit", foreignKey = @ForeignKey(name = "fk_product_groups_units_base_unit"))
    private Unit unit;

    @Override
    public String getTableName() {
        return "product_groups";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }

}
