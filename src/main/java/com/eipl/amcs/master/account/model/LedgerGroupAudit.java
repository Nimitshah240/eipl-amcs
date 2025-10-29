package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.deserialize.LedgerTypeDeserializer;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.serialize.LedgerTypeSerialize;
import com.eipl.amcs.serialize.SocietySerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using = LedgerTypeSerialize.class)
    @JsonDeserialize(using = LedgerTypeDeserializer.class)
    @JoinColumn(name = "ledger_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private LedgerType ledgerType;

    @Override
    public String getTableName() {
        return "ledger_groups_audit";
    }
}
