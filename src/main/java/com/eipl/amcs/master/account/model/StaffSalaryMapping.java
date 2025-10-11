package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Id
    private Integer code;
    private BigDecimal amount;
    private String unionCode;
    private LocalDate wefDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_staff_salary_head_mapping_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_member_code", foreignKey = @ForeignKey(name = "fk_staff_salary_head_mapping_staff_member_code"))
    @JsonIgnoreProperties(value = {"society", "bank", "branch", "gender", "designation"})
    private StaffMember staffMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_head_code", foreignKey = @ForeignKey(name = "fk_staff_salary_head_mapping_salary_head_code"))
    private StaffSalaryHead staffSalaryHead;


    @Override
    public String getTableName() {
        return "staff_salary_head_mapping";
    }
}
