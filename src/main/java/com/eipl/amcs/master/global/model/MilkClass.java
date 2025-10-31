package com.eipl.amcs.master.global.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.utils.CommonUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "milk_classes")
public class MilkClass extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    private String name;
    private String nameLocal;

    @Override
    public String getTableName() {
        return "milk_classes";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(name, nameLocal);
    }
}