package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.json.serialize.*;
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
@Table(name = "member_details")
public class MemberDetail extends BaseModelTxn {

    @Id
    @Size(max = 20)
    private String code;

    private Short paymentMode;
    @Size(max = 255)
    private String address;
    @Size(max = 6)
    private String pincode;
    @Size(max = 255)
    private String accountNo;
    @Size(max = 255)
    private String ifsc;
    @Size(max = 255)
    private String aadharNo;
    @Size(max = 255)
    private String panNo;
    @Size(max = 255)
    private String email;
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private Short numberOfCow;
    private Short numberOfBuffalo;
    @Size(max = 3)
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = GenderSerialize.class)
    @JsonDeserialize(using = GenderDeserializer.class)
    @JoinColumn(name = "gender_code", foreignKey = @ForeignKey(name = "fk_members_gender_code"))
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_members_bank_code"))
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_members_branch_code"))
    @JsonIgnoreProperties(value = {"bank", "state", "district", "subDistrict", "village", "hamlet"})
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(name = "fk_members_state_code"))
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(name = "fk_members_district_code"))
    @JsonIgnoreProperties(value = {"state"})
    private District district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubDistrictSerialize.class)
    @JsonDeserialize(using = SubDistrictDeserializer.class)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(name = "fk_members_sub_district_code"))
    @JsonIgnoreProperties(value = {"district"})
    private SubDistrict subDistrict;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(name = "fk_members_village_code"))
    @JsonIgnoreProperties(value = {"subDistrict"})
    private Village village;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HamletSerialize.class)
    @JsonDeserialize(using = HamletDeserializer.class)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(name = "fk_members_hamlet_code"))
    @JsonIgnoreProperties(value = {"village"})
    private Hamlet hamlet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_member_details_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
    private Member member;

    @Override
    public String getTableName() {
        return "member_details";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MemberDetailAudit audit = new MemberDetailAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setPaymentMode(this.getPaymentMode());
        audit.setAddress(this.getAddress());
        audit.setAccountNo(this.getAccountNo());
        audit.setPincode(this.getPincode());
        audit.setIfsc(this.getIfsc());
        audit.setAadharNo(this.getAadharNo());
        audit.setPanNo(this.getPanNo());
        audit.setEmail(this.getEmail());
        audit.setBirthDate(this.getBirthDate());
        audit.setRegistrationDate(this.getRegistrationDate());
        audit.setNumberOfBuffalo(this.getNumberOfBuffalo());
        audit.setNumberOfCow(this.getNumberOfCow());
        audit.setUnionCode(this.getUnionCode());
        audit.setGender(this.getGender());
        audit.setBank(this.getBank());
        audit.setBranch(this.getBranch());
        audit.setState(this.getState());
        audit.setDistrict(this.getDistrict());
        audit.setSubDistrict(this.getSubDistrict());
        audit.setVillage(this.getVillage());
        audit.setHamlet(this.getHamlet());
        audit.setMember(this.getMember());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }
}
