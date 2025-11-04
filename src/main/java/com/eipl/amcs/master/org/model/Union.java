package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.geo.model.*;
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
@Table(name = "unions")
public class Union extends BaseModel {
    @Id
    private String code;
    private String codeEx;
    private String name;
    private String nameLocal;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    private String registrationNo;
    private String bankAccountNo;
    private String ifsc;
    private String upiNo;

    private String address;
    private String city;
    private String phoneNo;
    private String pincode;
    private String contactPerson;
    private String contactPersonEmail;
    private String contactPersonMobileNo;
    private String contactPersonPanNo;
    private String contactPersonPhoneNo;
    private String faxNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_unions_bank_code"))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_unions_branch_code"))
    @JsonIgnoreProperties(value = {"bank", "state", "district", "subDistrict", "village", "hamlet"})
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_unions_states_code"))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_unions_districts_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubDistrictSerialize.class)
    @JsonDeserialize(using = SubDistrictDeserializer.class)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_unions_sub_districts_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_unions_villages_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HamletSerialize.class)
    @JsonDeserialize(using = HamletDeserializer.class)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(name = "fk_unions_hamlets_code"))
    @JsonIgnoreProperties(value = {"village"})
    private Hamlet hamlet;

    @Override
    public String getTableName() {
        return "unions";
    }
}
