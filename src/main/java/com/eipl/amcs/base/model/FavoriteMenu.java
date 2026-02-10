package com.eipl.amcs.base.model;

import com.eipl.amcs.auth.model.Permission;
import com.eipl.amcs.auth.model.RolePermission;
import com.eipl.amcs.auth.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name = "favorite_menu")
@Getter
@Setter
public class FavoriteMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;
    private String permissionType;
    private Integer parentCode;
    private String module;
    private Integer object;
    private String userName;

    @ManyToOne
    @JoinColumn(name = "role_permission_code", foreignKey = @ForeignKey(name = "fk_role_permission"))
    private RolePermission rolePermission;

    @ManyToOne
    @JoinColumn(name = "user_code", foreignKey = @ForeignKey(name = "fk_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "permission_code", foreignKey = @ForeignKey(name = "fk_permission"))
    private Permission permission;

}