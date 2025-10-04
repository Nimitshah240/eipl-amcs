package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import lombok.Getter;
import lombok.Setter;

/*
 * NO USAGE, SHIFT TO MODEL
 */
@Setter
@Getter
public class Events extends BaseModel {

    private Integer code;
    private String eventName;
    private Integer eventCode;
    private String description;
    private Boolean ledgerCredit;
    private Boolean ledgerDebit;
    private Boolean subLedgerCredit;
    private Boolean subLedgerDebit;
    private Society society;
    private Union union;

}
