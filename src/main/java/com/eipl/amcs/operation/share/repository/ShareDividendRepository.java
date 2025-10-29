package com.eipl.amcs.operation.share.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.share.model.ShareDividend;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShareDividendRepository extends BaseRepository<ShareDividend, String> {

    @Override
    @EntityGraph(attributePaths = {"member", "financialYear", "society"})
    Optional<ShareDividend> findById(String id);

    @EntityGraph(attributePaths = {"member", "financialYear", "society"})
    List<ShareDividend> findByDisbursementDateBetween(LocalDate fromDt, LocalDate toDt, Sort disbursementDate);
}