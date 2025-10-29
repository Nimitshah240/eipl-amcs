package com.eipl.amcs.auth.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
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
@Table(name = "role_permissions")
public class RolePermission extends BaseModelTxn {
    @Id
    @Size(max = 25)
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code", foreignKey = @ForeignKey(name = "fk_role_permissions_role_code"))
    private Role role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_code", foreignKey = @ForeignKey(name = "fk_role_permissions_permission_code"))
    private Permission permission;
    @Size(max = 3)
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_role_permissions_society_code"))
    private Society society;

    @Override
    public String getTableName() {
        return "role_permissions";
    }
}
