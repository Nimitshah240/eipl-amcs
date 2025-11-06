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
@Table(name = "society")
public class Society extends BaseModel {
    @Id
    private String code;
    private String codeEx;
    private String name;
    private String shortName;
    private String nameLocal;
    private String shortNameLocal;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    private String registrationCode;
    private String destinationCode;
    private Short destinationType;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;
    private Short isBmc;
    private String email;
    private String panNo;
    private String phoneNo;
    private String contactPerson;
    private String contactPersonMobileNo;
    private String bankAccountNo;
    private String ifsc;
    private String address;
    private String city;
    private String pincode;
    private Boolean allowMultiFamilyMember;
    private String tinNo;
    private String serviceTax;
    private String upiNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_society_bank_code"))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_society_branches_code"))
    @JsonIgnoreProperties(value = {"bank", "state", "district", "subDistrict", "village"})
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_society_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = PlantSerialize.class)
    @JsonDeserialize(using = PlantDeserializer.class)
    @JoinColumn(name = "plant_code", foreignKey = @ForeignKey(name = "fk_society_plant_code"))
    @JsonIgnoreProperties(value = {"union", "state", "district", "subDistrict", "village", "hamlet"})
    private Plant plant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MccSerialize.class)
    @JsonDeserialize(using = MccDeserializer.class)
    @JoinColumn(name = "mcc_code", foreignKey = @ForeignKey(name = "fk_society_mcc_code"))
    @JsonIgnoreProperties(value = {"union", "plant", "state", "district", "subDistrict", "village", "hamlet"})
    private Mcc mcc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BmcSerialize.class)
    @JsonDeserialize(using = BmcDeserializer.class)
    @JoinColumn(name = "bmc_code", foreignKey = @ForeignKey(name = "fk_society_bmc_code"))
    @JsonIgnoreProperties(value = {"union", "mcc", "state", "district", "subDistrict", "village", "hamlet"})
    private Bmc bmc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = RouteSerialize.class)
    @JsonDeserialize(using = RouteDeserializer.class)
    @JoinColumn(name = "route_code", foreignKey = @ForeignKey(name = "fk_society_route_code"))
    @JsonIgnoreProperties(value = {"union", "bmc"})
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_society_state_code"))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_society_district_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubDistrictSerialize.class)
    @JsonDeserialize(using = SubDistrictDeserializer.class)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_society_sub_district_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_society_village_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HamletSerialize.class)
    @JsonDeserialize(using = HamletDeserializer.class)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(name = "fk_society_hamlet_code"))
    @JsonIgnoreProperties(value = {"village"})
    private Hamlet hamlet;

    @Override
    public String getTableName() {
        return "society";
    }

    @Override
    public String toString() {
        return this.name;
    }
}
