package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.LedgerType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerTypeRepository extends BaseRepository<LedgerType, String> {

    @Override
    List<LedgerType> findAll(Sort sort);

    @Override
    Optional<LedgerType> findById(String integer);
}
