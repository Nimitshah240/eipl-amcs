package com.eipl.amcs.master.geo.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.geo.model.Hamlet;
import com.eipl.amcs.master.geo.model.Village;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface HamletRepository extends BaseRepository<Hamlet, String> {

	@Override
	@EntityGraph(attributePaths = { "village" })
	List<Hamlet> findAll(Sort sort);

	@EntityGraph(attributePaths = { "village" })
	List<Hamlet> findByVillage(Village village, Sort sort);
}
