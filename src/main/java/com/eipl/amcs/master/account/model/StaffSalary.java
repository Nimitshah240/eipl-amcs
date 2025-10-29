package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.StaffMemberDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.StaffMemberSerialize;
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
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "staff_salary")
public class StaffSalary extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;

    @Column(name = "wef_date")
    private LocalDate wefDate;

    @Column(name = "effective_working_days")
    private int effectiveWorkingDays;

    private LocalDate month;

    @Column(name = "type_of_head")
    private int typeOfHead;

    @Column(name = "value")
    private double value;

    @Size(max = 255)
    @Column(name = "voucher_no")
    private String voucherNo;
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_staff_salary_society_code"))
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society societyCode;
    @Size(max = 20)
    @JsonSerialize(using = StaffMemberSerialize.class)
    @JsonDeserialize(using = StaffMemberDeserializer.class)
    @JoinColumn(name = "staff_member_code", foreignKey = @ForeignKey(name = "fk_staff_salary_staff_member_code"))
    private StaffMember staffMemberCode;

    private String unionCode;


    @Override
    public String getTableName() {
        return "staff_salary";
    }

}
