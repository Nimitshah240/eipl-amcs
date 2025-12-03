package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import com.eipl.amcs.json.deserialize.LedgerTypeDeserializer;
import com.eipl.amcs.json.serialize.LedgerTypeSerialize;
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
@Table(name = "ledger_groups_audit")
public class LedgerGroupAudit extends BaseModelAudit {

    @Id
    private String code;
    private String name;
    private String nameLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerTypeSerialize.class)
    @JsonDeserialize(using = LedgerTypeDeserializer.class)
    @JoinColumn(name = "ledger_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private LedgerType ledgerType;

    @Override
    public String getTableName() {
        return "ledger_groups_audit";
    }
}
