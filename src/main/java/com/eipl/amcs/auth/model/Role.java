package com.eipl.amcs.auth.model;

import com.eipl.amcs.base.model.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private String name;
    private String unionCode;

    @Override
    public String getTableName() {
        return "roles";
    }
}
