package com.eipl.amcs.auth.task;

import com.eipl.amcs.master.org.model.Society;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String code;
    private String username;
    private String password;
    private String name;
    private String mobileNo;
    private String unionCode;

    private Society society;

    private Set<String> permissions;

    public User() {
    }

    public Set<String> getPermissions() {
        if (permissions == null)
            permissions = new HashSet<>();
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
