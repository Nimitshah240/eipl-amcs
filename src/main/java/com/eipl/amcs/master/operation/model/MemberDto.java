package com.eipl.amcs.master.operation.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Getter
@Setter
public class MemberDto implements Serializable {

    private Member member;
    private MemberDetail memberDetail;
    private List<MemberFamilyDetail> memberFamilyDetailList;
    private List<MemberCattleDetail> memberCattleDetailList;

    public MemberDto() {

    }

    public MemberDto(Member member, MemberDetail memberDetail) {
        super();
        this.member = member;
        this.memberDetail = memberDetail;
    }

    public MemberDto(Member member, MemberDetail memberDetail, List<MemberFamilyDetail> memberFamilyDetailList, List<MemberCattleDetail> memberCattleDetailList) {
        super();
        this.member = member;
        this.memberDetail = memberDetail;
        this.memberCattleDetailList = memberCattleDetailList;
        this.memberFamilyDetailList = memberFamilyDetailList;
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
