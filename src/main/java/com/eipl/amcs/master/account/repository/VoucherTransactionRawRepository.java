package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.VoucherTransactionRaw;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherTransactionRawRepository extends BaseRepository<VoucherTransactionRaw, String> {
}
