package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.*;
import com.eipl.amcs.json.serialize.*;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_staff-salary-process")
public class StaffSalaryProcess extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;
    @Column(name = "account_no")
    private String accountNo;

    private double actualValue;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    @Column(name = "disbursement_date")
    private LocalDateTime disbursementDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private int effectiveWorkingDays;
    private LocalDateTime month;

    private int typeOfHead;
    private double value;
    @Column(name = "voucher_no")
    private String voucherNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BankSerialize.class)
    @JsonDeserialize(using = BankDeserializer.class)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_bank_code"))
    private Bank bankCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BranchSerialize.class)
    @JsonDeserialize(using = BranchDeserializer.class)
    @JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_branch_code"))
    private Branch branchCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_society_code"))
    private Society societyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = DesignationSerialize.class)
    @JsonDeserialize(using = DesignationDeserializer.class)
    @JoinColumn(name = "designation_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_designation_code"))
    private Designation designationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StaffSalaryHeadSerialize.class)
    @JsonDeserialize(using = StaffSalaryHeadDeserializer.class)
    @JoinColumn(name = "salary_head_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_salary_head_code"))
    private StaffSalaryHead salaryHeadCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = StaffMemberSerialize.class)
    @JsonDeserialize(using = StaffMemberDeserializer.class)
    @JoinColumn(name = "staff_member_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_staff_member_code"))
    private StaffMember staffMemberCode;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_union_code"))
    private Union unionCode;


    @Override
    public String getTableName() {
        return "tbl_staff-salary-process";
    }
}
