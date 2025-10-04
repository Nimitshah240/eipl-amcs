package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "committee_members")
public class CommitteeMembers extends BaseModelTxn {

    @Id
    private String code;
    private LocalDate tenureFromDate;
    private LocalDate electionDate;
    private LocalDate tenureToDate;
    private String unionCode;
    private String memberCode;
    private String memberName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_committee_members_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation_code", foreignKey = @ForeignKey(name = "fk_committee_members_designation_code"))
    private Designation designation;

    @Override
    public String getTableName() {
        return "committee_members";
    }
}
