package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Ledger;
import com.eipl.amcs.master.account.model.Voucher;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherTransactionRepository extends BaseRepository<VoucherTransaction, String> {

    @Override
    @EntityGraph(attributePaths = {"ledger", "voucher"})
    List<VoucherTransaction> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"ledger", "voucher"})
    Optional<VoucherTransaction> findById(String integer);

    @EntityGraph(attributePaths = {"ledger", "voucher"})
    List<VoucherTransaction> findByVoucher(Voucher voucher);

    @EntityGraph(attributePaths = {"ledger", "voucher"})
    List<VoucherTransaction> findByVoucherInAndLedgerIn(List<Voucher> voucherList, List<Ledger> ledgers);
}
