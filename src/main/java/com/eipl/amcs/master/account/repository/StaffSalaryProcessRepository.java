package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.StaffSalaryProcess;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface StaffSalaryProcessRepository extends BaseRepository<StaffSalaryProcess, String> {

	@Override
	List<StaffSalaryProcess> findAll(Sort sort);

}
