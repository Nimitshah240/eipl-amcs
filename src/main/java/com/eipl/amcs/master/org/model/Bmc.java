package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.json.serialize.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "bmc")
public class Bmc extends BaseModel {
    @Id
    @Size(max = 6)
    private String code;
    @Size(max = 10)
    private String codeEx;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    @Size(max = 500)
    private String address;
    @Size(max = 50)
    private String city;
    @Size(max = 255)
    private String phoneNo;
    @Size(max = 100)
    private String contactPerson;
    @Size(max = 255)
    private String contactPersonEmail;
    @Size(max = 255)
    private String contactPersonMobileNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_bmc_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MccSerialize.class)
    @JsonDeserialize(using = MccDeserializer.class)
    @JoinColumn(name = "mcc_code", foreignKey = @ForeignKey(name = "fk_bmc_mcc_code"))
    @JsonIgnoreProperties(value = {"union", "plant", "state", "district", "subDistrict", "village", "hamlet"})
    private Mcc mcc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_bmc_state_code"))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_bmc_district_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubDistrictSerialize.class)
    @JsonDeserialize(using = SubDistrictDeserializer.class)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_bmc_sub_district_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_bmc_village_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HamletSerialize.class)
    @JsonDeserialize(using = HamletDeserializer.class)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(name = "fk_bmc_hamlet_code"))
    @JsonIgnoreProperties(value = {"village"})
    private Hamlet hamlet;

    @Override
    public String getTableName() {
        return "bmc";
    }
}
