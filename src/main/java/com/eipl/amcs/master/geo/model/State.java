package com.eipl.amcs.master.geo.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.utils.CommonUtils;
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
@Table(name = "states")
public class State extends BaseModel {

    @Id

    private String code;
    private String name;
    private String nameLocal;

    @Override
    public String getTableName() {
        return "states";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
