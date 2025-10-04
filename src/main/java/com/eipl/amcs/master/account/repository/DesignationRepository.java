package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Designation;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DesignationRepository extends BaseRepository<Designation, Integer> {

	@Override
	List<Designation> findAll(Sort sort);

}
