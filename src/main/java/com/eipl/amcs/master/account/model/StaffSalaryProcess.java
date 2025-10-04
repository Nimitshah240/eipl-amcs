package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
	@Size(max = 20)
	private String code;
	@Column(name= "account_no")
	@Size(max = 255)
	private String accountNo;

	private double actualValue;

	@Column(name = "disbursement_date")
	private LocalDateTime disbursementDate;

	private  int effectiveWorkingDays;
	private LocalDateTime month;

	private  int typeOfHead;
	private double value;
	@Size(max = 255)
	@Column(name = "voucher_no")
	private String voucherNo;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_bank_code"))
	private Bank bankCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_branch_code"))
	private Branch branchCode;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_society_code"))
	private Society societyCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "designation_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_designation_code"))
	private Designation designationCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salary_head_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_salary_head_code"))
	private StaffSalaryHead salaryHeadCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staff_member_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_staff_member_code"))
	private StaffMember staffMemberCode;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_staff_salary_process_union_code"))
	private Union unionCode;



	@Override
	public String getTableName() {
		return "tbl_staff-salary-process";
	}
}
