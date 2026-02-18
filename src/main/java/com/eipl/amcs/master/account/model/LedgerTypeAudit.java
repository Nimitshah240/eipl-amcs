package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "ledger_types_audit")
public class LedgerTypeAudit extends BaseModelAudit {

    @Id
    private String code;
    private String name;
    private String nameLocal;

    @Column(name = "profit_loss")
    private boolean profitLoss;
    @Column(name = "balance_sheet")
    private boolean balanceSheet;
    private String unionCode;
    @Override
    public String getTableName() {
        return "ledger_types_audit";
    }
}
