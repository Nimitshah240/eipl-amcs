package com.eipl.amcs.master.account.model;


import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.LedgerDeserializer;
import com.eipl.amcs.json.deserialize.ProductGroupDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.json.serialize.LedgerSerialize;
import com.eipl.amcs.json.serialize.ProductGroupSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_sale_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledgerSaleCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_purchase_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledgerPurchaseCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ProductGroupSerialize.class)
    @JsonDeserialize(using = ProductGroupDeserializer.class)
    @JoinColumn(name = "product_group_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_product_group_product_group_code"))
    @JsonIgnoreProperties(value = {"unit"})
    private ProductGroup productGroup;

    private String unionCode;


    @Override
    public String getTableName() {
        return "ledger_mapping_product_group_audit";
    }


}
