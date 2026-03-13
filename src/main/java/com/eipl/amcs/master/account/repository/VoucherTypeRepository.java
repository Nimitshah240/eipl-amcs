package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.VoucherType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherTypeRepository extends BaseRepository<VoucherType, Long> {

    @Override
    List<VoucherType> findAll(Sort sort);


    @Override
    Optional<VoucherType> findById(Long integer);
}
