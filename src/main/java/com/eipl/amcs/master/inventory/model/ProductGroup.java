package com.eipl.amcs.master.inventory.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.master.global.model.Unit;
import com.eipl.amcs.utils.CommonUtils;
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
@Table(name = "product_groups")
public class ProductGroup extends BaseModel {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;
	@Size(max = 100)
	private String name;
	@Size(max = 255)
	private String nameLocal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "base_unit", foreignKey = @ForeignKey(name = "fk_product_groups_units_base_unit"))
	private Unit unit;

	@Override
	public String getTableName() {
		return "product_groups";
	}

	@Override
	public String toString() {
		return CommonUtils.getLocalString(this.name, this.nameLocal);
	}

}
