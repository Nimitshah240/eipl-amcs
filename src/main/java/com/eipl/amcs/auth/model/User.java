package com.eipl.amcs.auth.model;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseModel {
    @Id
    private String code;
    private String username;
    private String password;
    private String name;
    private String mobileNo;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_users_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district",
            "subDistrict", "village", "hamlet"})
    private Society society;

    @Transient
    private Set<String> permissions;


    @Override
    public String getTableName() {
        return "users";
    }

    public Set<String> getPermissions() {
        if (permissions == null)
            permissions = new HashSet<>();
        return permissions;
    }
}
