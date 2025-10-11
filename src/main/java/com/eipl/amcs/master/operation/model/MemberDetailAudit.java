package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.geo.model.*;
import com.eipl.amcs.master.global.model.Gender;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
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
    private LocalDate birthDate;
    private LocalDate registrationDate;
    private Short numberOfCow;
    private Short numberOfBuffalo;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Gender gender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Bank bank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private State state;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private District district;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_district_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SubDistrict subDistrict;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Village village;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hamlet_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Hamlet hamlet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Override
    public String getTableName() {
        return "member_details_audit";
    }

}
