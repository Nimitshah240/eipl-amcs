package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModel;

import java.time.LocalDateTime;
public class StaffSalaryProcess extends BaseModel {

	private String code;
	private String accountNo;

	private double actualValue;

	private LocalDateTime disbursementDate;

	private  int effectiveWorkingDays;
	private LocalDateTime month;

	private  int typeOfHead;
	private double value;
	private String voucherNo;

	private String bankCode;

	private String branchCode;
	private String societyCode;

	private int designationCode;

	private int salaryHeadCode;

	private int staffMemberCode;

	private int subCenterCode;

	private int unionCode;



}
