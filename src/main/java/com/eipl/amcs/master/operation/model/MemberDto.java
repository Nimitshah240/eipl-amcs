package com.eipl.amcs.master.operation.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberDto implements Serializable {

    private Member member;
    private MemberDetail memberDetail;

    public MemberDto() {

    }

    public MemberDto(Member member, MemberDetail memberDetail) {
        super();
        this.member = member;
        this.memberDetail = memberDetail;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MemberDetail getMemberDetail() {
        return memberDetail;
    }

    public void setMemberDetail(MemberDetail memberDetail) {
        this.memberDetail = memberDetail;
    }


}
