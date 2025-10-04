package com.eipl.amcs.master.operation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_ekyc")
public class MemberEkyc {
    @Id
    @Size(max = 20)
    private String code;
    @Size(max = 15)
    private String status;
    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_member_ekyc_member_code"))
//    @JsonIgnoreProperties(value = {"milkType", "memberType", "society"})
//    private Member member;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_code", foreignKey = @ForeignKey(name = "fk_member_ekyc_member_code"))
    @JsonIgnoreProperties(value = {"milkType", "memberType", "society", "hibernateLazyInitializer", "handler"})
    private Member member;

}
