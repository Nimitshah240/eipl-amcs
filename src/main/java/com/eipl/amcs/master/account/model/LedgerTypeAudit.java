package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    @Override
    public String getTableName() {
        return "ledger_types_audit";
    }
}
