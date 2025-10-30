package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.geo.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "society")
public class Society extends BaseModel {
    @Id
    @Size(max = 12)
    private String code;
    @Size(max = 10)
    private String codeEx;
    @Size(max = 100)
    private String name;
    @Size(max = 100)
    private String shortName;
    @Size(max = 255)
    private String nameLocal;
    @Size(max = 255)
    private String shortNameLocal;

    private LocalDate registrationDate;
    @Size(max = 20)
    private String registrationCode;
    @Size(max = 10)
    private String destinationCode;
    private Short destinationType;
    private LocalDate effectiveDate;
    private Short isBmc;
    @Size(max = 255)
    private String email;
    @Size(max = 255)
    private String panNo;
    @Size(max = 255)
    private String phoneNo;
    @Size(max = 100)
    private String contactPerson;
    @Size(max = 255)
    private String contactPersonMobileNo;
    @Size(max = 255)
    private String bankAccountNo;
    @Size(max = 255)
    private String ifsc;
    @Size(max = 500)
    private String address;
    @Size(max = 100)
    private String city;
    @Size(max = 6)
    private String pincode;
    private Boolean allowMultiFamilyMember;
    @Size(max = 255)
    private String tinNo;
    @Size(max = 255)
    private String serviceTax;
    @Size(max = 255)
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
}
