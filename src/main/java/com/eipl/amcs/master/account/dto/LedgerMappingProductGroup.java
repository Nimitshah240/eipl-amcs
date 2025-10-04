package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

public class LedgerMappingProductGroup extends BaseModel {

    private String code;
    private Ledger ledgerSaleCode;
    private ProductGroup productGroup;
    private Ledger ledgerPurchaseCode;
    private Society society;
    private String unionCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Ledger getLedgerSaleCode() {
        return ledgerSaleCode;
    }

    public void setLedgerSaleCode(Ledger ledgerSaleCode) {
        this.ledgerSaleCode = ledgerSaleCode;
    }


    public Ledger getLedgerPurchaseCode() {
        return ledgerPurchaseCode;
    }

    public void setLedgerPurchaseCode(Ledger ledgerPurchaseCode) {
        this.ledgerPurchaseCode = ledgerPurchaseCode;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }
}
