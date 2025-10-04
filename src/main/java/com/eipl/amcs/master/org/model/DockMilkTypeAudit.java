package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
@Table(name = "dock_milk_types_audit")
public class DockMilkTypeAudit extends BaseModelTxnAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 15)
	private String code;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dock_no", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@JsonIgnoreProperties(value = { "society" })
	private Dock dock;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MilkType milkType;

	@Override
	public String getTableName() {
		return "dock_milk_types_audit";
	}

}
