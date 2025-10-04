package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.base.JsonAndTableBuilder;
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
@Table(name = "dock")
public class Dock extends BaseModel {
	@Id
	@Size(max = 11)
	private String dockNo;
	@Size(max = 3)
	private String unionCode;
	private Short isDefault;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_dock_society_code"))
	@JsonIgnoreProperties(value = { "bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
			"subDistrict", "village", "hamlet" })
	private Society society;

	public Dock(String dockNo) {
		this.dockNo = dockNo;
	}

	@Override
	public String getTableName() {
		return "dock";
	}

	@Override
	public Object getId() {
		return this.getDockNo();
	}

	@Override
	public JsonAndTableBuilder getAuditModel(String operation, String user) {
		DockAudit audit = new DockAudit();
		audit.setOperationType(operation);
		audit.setAuditCreatedBy(user);

		audit.setDockNo(this.getDockNo());
		audit.setUnionCode(this.getUnionCode());
		audit.setIsDefault(this.getIsDefault());
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
}
