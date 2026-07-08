package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.MemberTypeDeserializer;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.MemberTypeSerialize;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "members")
public class Member extends BaseModel {
    @Id
    private String code;
    private String codeEx;
    private String firstName;
    private String middleName;
    private String lastName;
    private String firstNameLocal;
    private String middleNameLocal;
    private String lastNameLocal;
    private String mobileNo;
    private String sapFarmerCode;

    @Column(name = "is_dcs_member")
    private boolean dcsMember;
    @ManyToOne
    @JoinColumn(name = "caste_category_code")
    private CasteCategory casteCategory;

    @Digits(integer = 8, fraction = 2)
    private BigDecimal creditLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_members_milk_type_code"))
    private MilkType milkType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MemberTypeSerialize.class)
    @JsonDeserialize(using = MemberTypeDeserializer.class)
    @JoinColumn(name = "member_type_code", foreignKey = @ForeignKey(name = "fk_members_member_type_code"))
    private MemberType memberType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_members_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    @Override
    public String getTableName() {
        return "members";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        MemberAudit audit = new MemberAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setCodeEx(this.getCodeEx());
        audit.setFirstName(this.getFirstName());
        audit.setMiddleName(this.getMiddleName());
        audit.setLastName(this.getLastName());
        audit.setFirstNameLocal(this.getFirstNameLocal());
        audit.setMiddleNameLocal(this.getMiddleNameLocal());
        audit.setLastNameLocal(this.getLastNameLocal());
        audit.setMobileNo(this.getMobileNo());
        audit.setCreditLimit(this.getCreditLimit());
        audit.setMilktype(this.getMilkType());
        audit.setMemberType(this.getMemberType());
        audit.setSociety(this.getSociety());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setActive(this.isActive());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }

    @Override
    public String toString() {
        return toMemberName();
    }

    public String toMemberName() {
        if (middleName == null) {
            middleName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        return CommonUtils.getLocalString(firstName, getFirstNameLocal()) + " " +
                CommonUtils.getLocalString(middleName, getMiddleNameLocal()) + " " +
                CommonUtils.getLocalString(lastName, getLastNameLocal());

    }


    public String toMemberNameWithExCode() {
        if (middleName == null) {
            middleName = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        return codeEx + "-" + CommonUtils.getLocalString(firstName, getFirstNameLocal()) + " " +
                CommonUtils.getLocalString(middleName, getMiddleNameLocal()) + " " +
                CommonUtils.getLocalString(lastName, getLastNameLocal());

    }

    public String toMemberName(String locale) {
        if (locale.equalsIgnoreCase("en")) {
            if (middleName == null)
                middleName = "";
            if (lastName == null)
                lastName = "";
            return firstName + " " + middleName + " " + lastName;
        } else if (locale.equalsIgnoreCase("gu") || locale.equalsIgnoreCase("hi")) {
            if (middleNameLocal == null)
                middleNameLocal = "";
            if (lastNameLocal == null)
                lastNameLocal = "";
            return firstNameLocal + " " + middleNameLocal + " " + lastNameLocal;
        }
        return "";
    }
}
