package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.global.model.MilkType;
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
@Table(name = "dock_milk_types")
public class DockMilkType extends BaseModelTxn {
	@Id
	@Size(max = 15)
	private String code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dock_no", foreignKey = @ForeignKey(name = "fk_dock_milk_types_dock_no"))
	@JsonIgnoreProperties(value = { "society" })
	private Dock dock;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_dock_milk_types_milk_type_code"))
	private MilkType milkType;

	@Override
	public String getTableName() {
		return "dock_milk_types";
	}

	@Override
	public Object getId() {
		return this.getCode();
	}

	@Override
	public JsonAndTableBuilder getAuditModel(String operation, String user) {
		DockMilkTypeAudit audit = new DockMilkTypeAudit();
		audit.setOperationType(operation);
		audit.setAuditCreatedBy(user);

		audit.setCode(this.getCode());
		audit.setDock(this.getDock());
		audit.setMilkType(this.getMilkType());

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