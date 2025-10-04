package com.eipl.amcs.master.global.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.utils.CommonUtils;
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
@Table(name = "milk_types")
public class MilkType extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    @Size(max = 25)
    private String name;
    @Size(max = 255)
    private String nameLocal;


    public MilkType(String name) {
        this.name = name;
    }

    public MilkType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getTableName() {
        return "milk_types";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(name, nameLocal);
    }
}