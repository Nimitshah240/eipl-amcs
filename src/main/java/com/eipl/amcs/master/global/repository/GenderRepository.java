package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.Gender;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface GenderRepository  extends BaseRepository<Gender, Integer> {

	@Override
	List<Gender> findAll(Sort sort);
}
