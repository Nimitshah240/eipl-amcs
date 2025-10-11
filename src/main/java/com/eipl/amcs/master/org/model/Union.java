package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.master.geo.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "unions")
public class Union extends BaseModel {
    @Id
    @Size(max = 3)
    private String code;
    @Size(max = 10)
    private String codeEx;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;

    private LocalDate registrationDate;
    @Size(max = 20)
    private String registrationNo;
    @Size(max = 255)
    private String bankAccountNo;
    @Size(max = 255)
    private String ifsc;
    @Size(max = 255)
    private String upiNo;

    @Size(max = 500)
    private String address;
    @Size(max = 50)
    private String city;
    @Size(max = 255)
    private String phoneNo;
    @Size(max = 6)
    private String pincode;
    @Size(max = 100)
    private String contactPerson;
    @Size(max = 255)
    private String contactPersonEmail;
    @Size(max = 255)
    private String contactPersonMobileNo;
    @Size(max = 255)
    private String contactPersonPanNo;
    @Size(max = 255)
    private String contactPersonPhoneNo;
    @Size(max = 255)
    private String faxNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_unions_bank_code"))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_unions_branch_code"))
    @JsonIgnoreProperties(value = {"bank", "state", "district", "subDistrict", "village", "hamlet"})
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_unions_states_code"))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_unions_districts_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_unions_sub_districts_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_unions_villages_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(name = "fk_unions_hamlets_code"))
    @JsonIgnoreProperties(value = {"village"})
    private Hamlet hamlet;

    @Override
    public String getTableName() {
        return "unions";
    }
}
