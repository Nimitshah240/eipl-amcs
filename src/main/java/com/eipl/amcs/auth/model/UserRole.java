package com.eipl.amcs.auth.model;

import com.eipl.amcs.base.BaseModelTxn;
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
@Table(name = "user_roles")
public class UserRole extends BaseModelTxn {

    @Id
    @Size(max = 15)
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code", foreignKey = @ForeignKey(name = "fk_user_roles_user_code"))
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code", foreignKey = @ForeignKey(name = "fk_user_roles_role_code"))
    private Role role;
    @Size(max = 3)
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_user_roles_society_code"))
    private Society society;

    @Override
    public String getTableName() {
        return "user_roles";
    }
}
