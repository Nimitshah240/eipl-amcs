package com.eipl.amcs.operation.share.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.operation.share.model.Share;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShareRepository extends BaseRepository<Share, String> {

    @Override
    @EntityGraph(attributePaths = {"member", "transferredFrom", "society"})
    Optional<Share> findById(String id);

    @EntityGraph(attributePaths = {"member", "transferredFrom", "society"})
    List<Share> findAll();

    @EntityGraph(attributePaths = {"member", "transferredFrom", "society"})
    List<Share> findByIssueDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

    @EntityGraph(attributePaths = {"member", "transferredFrom", "society"})
    List<Share> findByMember(Member member);

    @EntityGraph(attributePaths = {"member", "transferredFrom", "society"})
    List<Share> findByShareCode(String code);
}