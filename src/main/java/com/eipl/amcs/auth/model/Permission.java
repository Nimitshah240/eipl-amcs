package com.eipl.amcs.auth.model;

import com.eipl.amcs.base.model.BaseModel;
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
@Table(name = "permissions")
public class Permission extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    @Size(max = 100)
    private String name;
    @Size(max = 100)
    private String type;
    @Size(max = 100)
    private String description;
    private Integer parentCode;
    @Size(max = 100)
    private String module;
    private Integer object;
    @Size(max = 3)
    private String unionCode;

    @Override
    public String getTableName() {
        return "permissions";
    }
}
