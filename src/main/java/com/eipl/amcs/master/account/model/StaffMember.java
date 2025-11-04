package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
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
@Table(name = "staff_member")
public class StaffMember extends BaseModelTxn {

    @Id
    private String code;
    private String name;
    private String bankAccountNo;
    private String emailId;
    private String ifsc;
    private String mobileNo;
    private String panNo;
    private String pinCode;
    private Integer paymentMode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate tenureFromDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
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
