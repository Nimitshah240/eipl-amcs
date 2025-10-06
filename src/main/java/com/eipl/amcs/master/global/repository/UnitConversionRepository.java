package com.eipl.amcs.master.global.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.global.model.UnitConversion;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitConversionRepository  extends BaseRepository<UnitConversion, Integer> {

	@Override
	@EntityGraph(attributePaths = { "fromUnit", "toUnit" })
	List<UnitConversion> findAll(Sort sort);
}
