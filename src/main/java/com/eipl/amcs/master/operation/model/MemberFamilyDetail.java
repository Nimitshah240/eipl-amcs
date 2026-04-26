package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.master.global.model.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tbl_member_family_details")
public class MemberFamilyDetail {
    @Id
    @Column(name = "member_family_detail_code")
    private String memberFamilyDetailCode;

    @Column(name = "union_code", length = 3)
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_code")
    private Member member;

    @Column(name = "family_member_name")
    private String familyMemberName;

    @Column(name = "local_family_member_name")
    private String localFamilyMemberName;

    @Column(name = "dob")
    private LocalDate dob; // ⚠️ stored as VARCHAR in DB

    private Integer age;

    // 🔗 Gender FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_code")
    private Gender gender;

    // 🔗 Relationship FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationship_code")
    private Relationship relationship;

    @Column(name = "is_nominee")
    private boolean nominee;

    @Column(name = "nominee_address")
    private String nomineeAddress;

    @Column(name = "local_nominee_address")
    private String localNomineeAddress;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "local_guardian_name")
    private String localGuardianName;

    private String remarks;

    @Column(name = "originating_type")
    private Integer originatingType;

    @Column(name = "originating_org_code")
    private String originatingOrgCode;

    @Column(name = "originating_org_type")
    private String originatingOrgType;

    @Column(name = "ration_card_no")
    private String rationCardNo;

    @Column(name = "ration_card_type")
    private String rationCardType;

    @Column(name = "farmer_code")
    private String farmerCode;

    @Column(name = "farmer_name")
    private String farmerName;

    @Column(name = "is_farmer")
    private boolean farmer;

    @Column(name = "aadhar_card")
    private String aadharCard;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;
}