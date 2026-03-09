package com.eipl.amcs.master.operation.service;

import com.eipl.amcs.master.operation.dto.MemberImportDto;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.operation.procurement.dto.MemberSocietyInfoDto;
import com.eipl.amcs.report.dto.MemberRegister;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> findAll();

    Member findByMemberCode(String code);

    MemberDto save(MemberDto memberDto, String identityInfo);

    MemberDto update(MemberDto member, String identityInfo);

    Optional<Member> findById(String code);

    MemberDetail findDetailByMemberCode(String code);

    void delete(String code, String identityInfo);

    MemberSocietyInfoDto findMemberInformation(String code, LocalDateTime date, Integer count, String paymentCycle);

    List<MemberImportDto> importMembers(List<MemberDto> dtoList, String header);

    MemberDetail findDetailByMember(Member member);

    List<Member> findAllBySociety(String societyCode);

    MemberRegister findMemberRegisterData(String societyCode, LocalDateTime dt, String memberType);

    List<MemberDetail> findAllMemberDetails();

    void createSubLedgerOfMember(Member member);
}
