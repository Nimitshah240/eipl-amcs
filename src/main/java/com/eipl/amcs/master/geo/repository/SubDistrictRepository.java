package com.eipl.amcs.master.geo.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.geo.model.District;
import com.eipl.amcs.master.geo.model.SubDistrict;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface SubDistrictRepository extends BaseRepository<SubDistrict, String> {

	@Override
	@EntityGraph(attributePaths = { "district" })
	List<SubDistrict> findAll(Sort sort);

	@EntityGraph(attributePaths = { "district" })
	List<SubDistrict> findByDistrict(District distsrict, Sort sort);
}
