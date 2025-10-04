package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface VoucherSubLedgerRepository extends BaseRepository<VoucherSubLedger, String> {

    @Override
    @EntityGraph(attributePaths = {"subLedger", "voucher", "voucherTransaction"})
    List<VoucherSubLedger> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"subLedger", "voucher", "voucherTransaction"})
    Optional<VoucherSubLedger> findById(String s);

    @EntityGraph(attributePaths = {"subLedger", "voucher", "voucherTransaction"})
    List<VoucherSubLedger> findByVoucherTransaction(VoucherTransaction voucherTransaction);
}
