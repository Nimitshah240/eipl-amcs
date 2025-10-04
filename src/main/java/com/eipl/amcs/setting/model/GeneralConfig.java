package com.eipl.amcs.setting.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "general_config")
public class GeneralConfig extends BaseModelTxn {

	@Id
	@Size(max = 10)
	private String code;
	@Column(name ="json_key")
	private String key;
	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_hardware_device_configs_society_code"))
	@JsonIgnoreProperties(value = { "bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
			"subDistrict", "village", "hamlet" })
	private Society society;

	@Override
	public String getTableName() {
		return "general_config";
	}

	@Override
	public Object getId() {
		return this.getCode();
	}

	public JsonAndTableBuilder getAuditModel() {
		GeneralConfigAudit audit = new GeneralConfigAudit();


		audit.setCode(this.getCode());
		audit.setKey(this.getKey());
		audit.setValue(this.getValue());
		audit.setSocietyCode(this.getSociety() != null ? this.getSociety().getCode() : "");

		audit.setCreatedAt(this.getCreatedAt());
		audit.setCreatedBy(this.getCreatedBy());
		audit.setUpdatedAt(this.getUpdatedAt());
		audit.setUpdatedBy(this.getUpdatedBy());
		audit.setXCol1(this.getXCol1());
		audit.setXCol2(this.getXCol2());
		audit.setXCol3(this.getXCol3());

		return audit;
	}
}
