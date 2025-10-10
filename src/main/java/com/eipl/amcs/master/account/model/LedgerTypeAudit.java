package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ledger_types_audit")
public class LedgerTypeAudit extends BaseModelAudit {

    @Id
    private Integer code;
    @Size(max = 100)
    private String name;
    @Size(max = 255)
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
