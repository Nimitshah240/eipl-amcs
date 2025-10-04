package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Union;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;

@Setter
@Getter
public class TaxDto extends BaseModel {

    private Tax tax;
    private List<TaxDetail> taxDetails;

//    public Tax getTax() {
//        return tax;
//    }
//
//    public void setTax(Tax tax) {
//        this.tax = tax;
//    }
//
//    public List<TaxDetail> getTaxDetails() {
//        return taxDetails;
//    }
//
//    public void setTaxDetails(List<TaxDetail> taxDetails) {
//        this.taxDetails = taxDetails;
//    }
}