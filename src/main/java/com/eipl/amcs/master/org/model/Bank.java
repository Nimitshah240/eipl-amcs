package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.LedgerDeserializer;
import com.eipl.amcs.json.serialize.LedgerSerialize;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.utils.CommonUtils;
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
@Table(name = "banks")
public class Bank extends BaseModel {
    @Id
    private String code;
    private String name;
    private String nameLocal;
    private Short acNoLength;
    private Boolean checkedAcNoLength;
    private Boolean nationalizedBank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_banks_ledger_code"))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;
    @Override
    public String getTableName() {
        return "banks";
    }

    public boolean isNationalizedBank() {
        return nationalizedBank;
    }

    public boolean isCheckedAcNoLength() {
        return checkedAcNoLength;
    }

    @Override
    public String toString() {
        return CommonUtils.getLocalString(this.name, this.nameLocal);
    }
}
