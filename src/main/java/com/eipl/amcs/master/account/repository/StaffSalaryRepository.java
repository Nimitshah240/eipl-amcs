package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.StaffSalary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffSalaryRepository extends BaseRepository<StaffSalary, Integer> {

	@Override
	List<StaffSalary> findAll(Sort sort);

}
