package com.eipl.amcs.master.org.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.org.model.Dock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DockRepository extends BaseRepository<Dock, String> {

	@Override
	@EntityGraph(attributePaths = { "society" })
	List<Dock> findAll(Sort sort);
	
	@EntityGraph(attributePaths = { "society" })
	Optional<Dock> findById(String dockNumber);

}
