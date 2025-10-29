package com.eipl.amcs.master.geo.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.deserialize.LedgerDeserializer;
import com.eipl.amcs.deserialize.StateDeserializer;
import com.eipl.amcs.serialize.LedgerSerialize;
import com.eipl.amcs.serialize.StateSerialize;
import com.eipl.amcs.utils.CommonUtils;
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
@Table(name = "districts")
public class District extends BaseModel {

    @Id
    @Size(max = 3)
    private String code;
    @Size(max = 100)
    private String name;
    @Size(max = 255)
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
