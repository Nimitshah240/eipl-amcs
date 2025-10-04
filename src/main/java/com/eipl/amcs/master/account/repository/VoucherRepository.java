package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Voucher;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends BaseRepository<Voucher, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "voucherType"})
    List<Voucher> findAll(Sort sort);


    @Override
    @EntityGraph(attributePaths = {"society", "voucherType"})
    Optional<Voucher> findById(String voucherCode);
}
