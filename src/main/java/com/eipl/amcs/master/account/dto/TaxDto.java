package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TaxDto extends BaseModel {
    private Tax tax;
    private List<TaxDetail> taxDetails;
}