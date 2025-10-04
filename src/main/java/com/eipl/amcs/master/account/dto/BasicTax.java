package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.utils.CommonUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * NO USAGE, SHIFT TO MODEL
 */
@Setter
@Getter
@NoArgsConstructor
public class BasicTax extends BaseModel {


    private Integer code;
    private String name;
    private String nameLocal;

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }

}