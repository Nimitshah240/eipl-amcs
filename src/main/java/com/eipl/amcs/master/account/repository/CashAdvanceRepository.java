package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.CashAdvance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface CashAdvanceRepository extends BaseRepository<CashAdvance, String> {

	@Override
	@EntityGraph(attributePaths = {"society", "societyPaymentCycle","member"})
	List<CashAdvance> findAll(Sort sort);

}
