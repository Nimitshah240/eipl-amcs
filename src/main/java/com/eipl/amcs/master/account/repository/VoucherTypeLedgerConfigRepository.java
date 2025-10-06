package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.VoucherTypeLedgerConfig;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherTypeLedgerConfigRepository extends BaseRepository<VoucherTypeLedgerConfig, String> {

	@Override
	@EntityGraph(attributePaths = { "voucherType", "ledger", "society" })
	List<VoucherTypeLedgerConfig> findAll(Sort sort);


	@Override
	Optional<VoucherTypeLedgerConfig> findById(String s);
}
