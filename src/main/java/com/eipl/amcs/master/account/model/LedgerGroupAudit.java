package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ledger_groups_audit")
public class LedgerGroupAudit extends BaseModelAudit {

    @Id
    private Integer code;
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private LedgerType ledgerType;

    @Override
    public String getTableName() {
        return "ledger_groups_audit";
    }
}
