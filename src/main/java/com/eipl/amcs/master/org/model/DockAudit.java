package com.eipl.amcs.master.org.model;

import com.eipl.amcs.base.BaseModelAudit;
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
@Table(name = "dock_audit")
public class DockAudit extends BaseModelAudit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max = 11)
	private String dockNo;
	@Size(max = 3)
	private String unionCode;
	private Short isDefault;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Society society;

	@Override
	public String getTableName() {
		return "dock_audit";
	}
}
