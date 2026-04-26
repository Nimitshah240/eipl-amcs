package com.eipl.amcs.master.operation.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tbl_member_cattle_detail")
public class MemberCattleDetail {

    @Id
    @Column(name = "member_cattle_detail_code", length = 30)
    private String memberCattleDetailCode;

    private Integer milky;
    private Integer dry;
    private Integer calf;
    private Integer total;
    @Column(name = "cattle_detail", length = 20)
    private String cattleDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_code")
    private Member member;
}