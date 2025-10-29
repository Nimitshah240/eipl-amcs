package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "banks")
public class Bank extends BaseModel {
    @Id
    @Size(max = 4)
    private String code;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
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
