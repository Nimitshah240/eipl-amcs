package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;

/*
 * NO USAGE, SHIFT TO MODEL
 */

@Setter
@Getter
public class LedgerGroup extends BaseModel {
    private Integer code;
    private String name;
    private String nameLocal;
    private LedgerType ledgerType;

    public LedgerGroup() {
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
