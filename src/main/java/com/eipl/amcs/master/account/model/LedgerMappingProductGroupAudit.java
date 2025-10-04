package com.eipl.amcs.master.account.model;


import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ledger_mapping_product_group_audit")
public class LedgerMappingProductGroupAudit extends BaseModelTxnAudit {

    @Id
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_sale_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledgerSaleCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_purchase_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledgerPurchaseCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_group_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_product_group_product_group_code"))
    @JsonIgnoreProperties(value = {"unit"})
    private ProductGroup productGroup;

    private String unionCode;


    @Override
    public String getTableName() {
        return "ledger_mapping_product_group_audit";
    }


}
