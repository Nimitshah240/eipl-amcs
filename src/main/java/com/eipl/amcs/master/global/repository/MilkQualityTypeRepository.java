package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.MilkQualityType;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MilkQualityTypeRepository  extends BaseRepository<MilkQualityType, Integer> {

	@Override
	List<MilkQualityType> findAll(Sort sort);
}
