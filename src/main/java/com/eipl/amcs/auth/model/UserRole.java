package com.eipl.amcs.auth.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UserDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UserSerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_roles")
public class UserRole extends BaseModelTxn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UserSerialize.class)
    @JsonDeserialize(using = UserDeserializer.class)
    @JoinColumn(name = "user_code", foreignKey = @ForeignKey(name = "fk_user_roles_user_code"))
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_code", foreignKey = @ForeignKey(name = "fk_user_roles_role_code"))
    private Role role;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_user_roles_society_code"))
    private Society society;

    @Override
    public String getTableName() {
        return "user_roles";
    }
}
