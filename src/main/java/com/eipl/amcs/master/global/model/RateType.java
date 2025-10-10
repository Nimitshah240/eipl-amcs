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
@Table(name = "rate_types")
public class RateType extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    @Size(max = 50)
    private String rateType;

    @Override
    public String getTableName() {
        return "rate_types";
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.rateType, null);
    }
}