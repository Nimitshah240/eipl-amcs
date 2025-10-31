package com.eipl.amcs.master.geo.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.StateDeserializer;
import com.eipl.amcs.json.serialize.StateSerialize;
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
@Table(name = "districts")
public class District extends BaseModel {

    @Id
    private String code;
    private String name;
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_districts_states_code"))
    private State state;

    @Override
    public String getTableName() {
        return "districts";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }

}
