package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.Shift;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository  extends BaseRepository<Shift, Integer> {

	@Override
	List<Shift> findAll(Sort sort);
}
