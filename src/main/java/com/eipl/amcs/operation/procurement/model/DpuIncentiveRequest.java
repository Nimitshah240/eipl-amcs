package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dpu_incentive_master")
public class DpuIncentiveRequest extends BaseModelTxn {

	@Id
	private Long incentiveMasterCode;
	@Column(name = "m_cutoff_time")
	private LocalTime mctime;
	@Column(name = "e_cutoff_time")
	private LocalTime ectime;
	@Column(name = "m_start_time")
	private LocalTime mstime;
	@Column(name = "e_start_time")
	private LocalTime estime;
	@Column(name = "m_lock_time")
	private LocalTime mltime;
	@Column(name = "e_lock_time")
	private LocalTime eltime;
	private Integer incRate;
	private Integer incDeduction;
	private String originatingOrgCode;
	private String originatingOrgType;
	private Integer originatingType;
	private LocalDate fromDate;
	private LocalDate toDate;
	private String xCol4;
	private String xCol5;
	private String unionCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_local_milk_sale_society_code"))
	@JsonIgnoreProperties(value = { "bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
			"subDistrict", "village", "hamlet" })
	private Society society;

	@Override
	public String getTableName() {
		return "dpu_incentive_master";
	}


}
