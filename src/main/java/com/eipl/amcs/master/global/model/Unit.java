package com.eipl.amcs.master.global.model;

import com.eipl.amcs.base.BaseModel;
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
@Table(name = "units")
public class Unit extends BaseModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;
	@Size(max = 25)
	private String name;
	@Size(max = 100)
	private String nameLocal;
	@Size(max = 10)
	private String shortName;

	@Override
	public String getTableName() {
		return "units";
	}

	@Override
	public String toString() {
		return CommonUtils.getLocalString(this.name, this.nameLocal);
	}
}
