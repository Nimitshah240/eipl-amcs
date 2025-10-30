package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
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
@Table(name = "banks")
public class Bank extends BaseModel {
    @Id
    private String code;
    private String name;
    private String nameLocal;
    private Short acNoLength;
    private Boolean checkedAcNoLength;
    private Boolean nationalizedBank;

    @Override
    public String getTableName() {
        return "banks";
    }

    public boolean isNationalizedBank() {
        return nationalizedBank;
    }

    public boolean isCheckedAcNoLength() {
        return checkedAcNoLength;
    }
}
