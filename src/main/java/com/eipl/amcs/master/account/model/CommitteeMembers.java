package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.CommitteeDeserializer;
import com.eipl.amcs.json.deserialize.DesignationDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.CommitteeSerialize;
import com.eipl.amcs.json.serialize.DesignationSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tenureFromDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate electionDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tenureToDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    private String unionCode;
    private String memberCode;
    private String memberName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_committee_members_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DesignationSerialize.class)
    @JsonDeserialize(using = DesignationDeserializer.class)
    @JoinColumn(name = "designation_code", foreignKey = @ForeignKey(name = "fk_committee_members_designation_code"))
    private Designation designation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = CommitteeSerialize.class)
    @JsonDeserialize(using = CommitteeDeserializer.class)
    @JoinColumn(name = "committee_code")
    private Committee committee;

    @Override
    public String getTableName() {
        return "committee_members";
    }
}
