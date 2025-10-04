package com.eipl.amcs.auth.model;

import com.eipl.amcs.base.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer code;
	@Size(max = 50)
	private String name;
	@Size(max = 3)
	private String unionCode;

	@Override
	public String getTableName() {
		return "roles";
	}
}
