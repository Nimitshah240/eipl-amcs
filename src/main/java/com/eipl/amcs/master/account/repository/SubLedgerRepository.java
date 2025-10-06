package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.SubLedger;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubLedgerRepository extends BaseRepository<SubLedger, String> {

    @Override
    @EntityGraph(attributePaths = {"society"})
    List<SubLedger> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"society"})
    Optional<SubLedger> findById(String code);

    @EntityGraph(attributePaths = {"society"})
    Optional<SubLedger> findByReferenceCodeAndType(String code,short type);
    @EntityGraph(attributePaths = {"society"})
    Optional<SubLedger> findByTypeAndReferenceCode(short type, String refrenceCode);
}
