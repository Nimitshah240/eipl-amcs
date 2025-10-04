package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.report.dto.MemberCollectionSummary;
import com.eipl.amcs.report.dto.MemberRegister;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends BaseRepository<Member, String> {

    @Override
    @EntityGraph(attributePaths = {"milkType", "memberType", "society"})
    List<Member> findAll(Sort sort);

    @EntityGraph(attributePaths = {"milkType", "memberType", "society"})
    List<Member> findAllBySociety(Society society, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"milkType", "memberType", "society"})
    Optional<Member> findById(String code);

    @EntityGraph(attributePaths = {"milkType", "memberType"})
    public Member findByCode(String Code);

    @Query(value = "CALL rpt_member_collection_summary(:p_from_collection_date, :p_to_collection_date, :p_society_code, :p_ltr_kg);", nativeQuery = true)
    List<MemberCollectionSummary> findMemberCollectionReport(@Param("p_from_collection_date") LocalDateTime fromDate,
                                                             @Param("p_to_collection_date") LocalDateTime toDate,
                                                             @Param("p_society_code") String societyCode,
                                                             @Param("p_ltr_kg") Integer qtyMode);

    @Query(value = "CALL rpt_member_reg(:p_society_code,:p_from_date,:p_member_type);", nativeQuery = true)
    List<MemberRegister> findMemberRegisterData(@Param("p_society_code") String societyCode,
                                                @Param("p_from_date") LocalDate fromDate,
                                                @Param("p_member_type") Integer memberType);

}


