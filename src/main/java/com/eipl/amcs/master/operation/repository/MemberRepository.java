package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends BaseRepository<Member, String> {

    @Override
    @EntityGraph(attributePaths = {"milkType", "memberType", "society"})
    List<Member> findAll(Sort sort);

    @EntityGraph(attributePaths = {"milkType", "memberType", "society", "casteCategory"})
    List<Member> findAllBySociety(Society society, Sort sort);

    @Override
    @EntityGraph(attributePaths = {"milkType", "memberType", "society"})
    Optional<Member> findById(String code);

    @EntityGraph(attributePaths = {"milkType", "memberType"})
    Member findByCode(String Code);

    @EntityGraph(attributePaths = {"milkType", "memberType"})
    List<Member> findByxCol1(String Code);
}


