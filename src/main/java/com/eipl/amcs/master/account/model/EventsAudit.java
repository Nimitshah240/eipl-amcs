package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "event_audit")
public class EventsAudit extends BaseModelAudit {

    @Id
    private Integer code;
    @Size(max = 200)
    private String eventName;
    private String description;
    private Boolean ledgerCredit;
    private Boolean ledgerDebit;
    private Boolean subLedgerCredit;
    private Boolean subLedgerDebit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;

    @Override
    public String getTableName() {
        return "event_audit";
    }


}
