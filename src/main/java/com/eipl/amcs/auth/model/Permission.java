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
@Table(name = "permissions")
public class Permission extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    private String name;
    private String type;
    private String description;
    private Integer parentCode;
    private String module;
    private Integer object;
    private String unionCode;

    @Override
    public String getTableName() {
        return "permissions";
    }
}
