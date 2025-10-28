package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.deserialize.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.*;
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
@Table(name = "staff_member")
public class StaffMember extends BaseModelTxn {

    @Id
    @Size(max = 50)
    private String code;
    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String bankAccountNo;
    @Size(max = 255)
    private String emailId;
    @Size(max = 255)
    private String ifsc;
    @Size(max = 255)
    private String mobileNo;
    @Size(max = 255)
    private String panNo;
    @Size(max = 8)
    private String pinCode;
    private Integer paymentMode;
    private LocalDate tenureFromDate;
    private LocalDate tenureToDate;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_staff_member_bank_code"))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_staff_member_branch_code"))
    @JsonIgnoreProperties(value = {"bank", "state", "district", "subDistrict", "village", "hamlet"})
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_staff_member_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DesignationSerialize.class)
    @JsonDeserialize(using = DesignationDeserializer.class)
    @JoinColumn(name = "designation_code", foreignKey = @ForeignKey(name = "fk_staff_member_designation_code"))
    private Designation designation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = GenderSerialize.class)
    @JsonDeserialize(using = GenderDeserializer.class)
    @JoinColumn(name = "gender_code", foreignKey = @ForeignKey(name = "fk_staff_member_gender_code"))
    private Gender gender;

    @Override
    public String getTableName() {
        return "staff_member";
    }
}
