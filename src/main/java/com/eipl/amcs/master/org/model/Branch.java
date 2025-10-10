package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.State;
import com.eipl.amcs.master.geo.model.SubDistrict;
import com.eipl.amcs.master.geo.model.Village;
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
@Table(name = "branches")
public class Branch extends BaseModel {
    @Id
    @Size(max = 9)
    private String code;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;
    @Size(max = 250)
    private String address;
    @Size(max = 255)
    private String ifsc;
    @Size(max = 6)
    private String pincode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_branches_bank_code"))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_branches_states_code"))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_branches_districts_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_branches_sub_districts_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_branches_villages_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;

    @Override
    public String getTableName() {
        return "branches";
    }

}
