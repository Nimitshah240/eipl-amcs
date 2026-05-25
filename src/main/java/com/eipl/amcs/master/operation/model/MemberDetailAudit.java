package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "member_details_audit")
public class MemberDetailAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Short paymentMode;
    private String address;
    private String pincode;
    private String accountNo;
    private String ifsc;
    private String aadharNo;
    private String panNo;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    private Short numberOfCow;
    private Short numberOfBuffalo;
    private String unionCode;
    private String qualification;
    @Column(name = "is_educated")
    private boolean educated;
    @Column(name = "is_cooking_gas")
    private boolean cookingGas;
    @Column(name = "is_milk_machine")
    private boolean milkMachine;
    @Column(name = "is_piyet_land")
    private boolean piyetLand;
    @Column(name = "is_chaf_cutter")
    private boolean chafCutter;
    @Column(name = "is_toilet")
    private boolean toilet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = GenderSerialize.class)
    @JsonDeserialize(using = GenderDeserializer.class)
    @JoinColumn(name = "gender_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Gender gender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StateSerialize.class)
    @JsonDeserialize(using = StateDeserializer.class)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DistrictSerialize.class)
    @JsonDeserialize(using = DistrictDeserializer.class)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubDistrictSerialize.class)
    @JsonDeserialize(using = SubDistrictDeserializer.class)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VillageSerialize.class)
    @JsonDeserialize(using = VillageDeserializer.class)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = HamletSerialize.class)
    @JsonDeserialize(using = HamletDeserializer.class)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Hamlet hamlet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberSerialize.class)
    @JsonDeserialize(using = MemberDeserializer.class)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Override
    public String getTableName() {
        return "member_details_audit";
    }

}
