package com.eipl.amcs.master.operation.dto;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.operation.model.Member;

public class MemberEkyc extends BaseModel {
    private String code;
    private String status;
    private Member member;

    public MemberEkyc() {
        createdBy = MainApp.getUser() != null ? MainApp.getUser().getUsername() : null;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
