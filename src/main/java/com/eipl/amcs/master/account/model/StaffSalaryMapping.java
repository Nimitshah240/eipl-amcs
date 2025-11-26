package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.StaffMemberDeserializer;
import com.eipl.amcs.json.deserialize.StaffSalaryHeadDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.StaffMemberSerialize;
import com.eipl.amcs.json.serialize.StaffSalaryHeadSerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "staff_salary_head_mapping")
public class StaffSalaryMapping extends BaseModelTxn {

    @Column(name = "is_active")
    protected boolean active;
    @Id
    private String code;
    private BigDecimal amount;
    private String unionCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate wefDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_staff_salary_head_mapping_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StaffMemberSerialize.class)
    @JsonDeserialize(using = StaffMemberDeserializer.class)
    @JoinColumn(name = "staff_member_code", foreignKey = @ForeignKey(name = "fk_staff_salary_head_mapping_staff_member_code"))
    @JsonIgnoreProperties(value = {"society", "bank", "branch", "gender", "designation"})
    private StaffMember staffMember;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StaffSalaryHeadSerialize.class)
    @JsonDeserialize(using = StaffSalaryHeadDeserializer.class)
    @JoinColumn(name = "salary_head_code", foreignKey = @ForeignKey(name = "fk_staff_salary_head_mapping_salary_head_code"))
    private StaffSalaryHead staffSalaryHead;

    @Override
    public String getTableName() {
        return "staff_salary_head_mapping";
    }
}
