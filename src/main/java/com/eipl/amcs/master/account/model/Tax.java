package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModel;
import com.eipl.amcs.master.org.model.Union;
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
@Table(name = "tax")
public class Tax extends BaseModel {

	@Id
	@Size(max = 10)
	private String code;
	@Size(max = 100)
	private String name;
	@Size(max = 255)
	private String nameLocal;
	private Integer entryType; //1-union

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_tax_union_code"))
	@JsonIgnoreProperties(value = { "bank", "branch", "state", "district", "subDistrict", "village", "hamlet" })
	private Union union;
	
	@Override
	public String getTableName() {
		return "tax";
	}
}
