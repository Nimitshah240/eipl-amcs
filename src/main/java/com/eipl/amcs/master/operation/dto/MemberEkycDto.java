package com.eipl.amcs.master.operation.dto;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberEkycDto {
	private String code;
	private String name;
	private String mobileNo;
	private String aadharNo;
	private Member member;
	private MemberDetail memberDetail;
}
