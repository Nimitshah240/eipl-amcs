package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface MeetingAgendaRepository extends BaseRepository<MeetingAgenda, String> {

	@Override
	@EntityGraph(attributePaths = {"society", "union"})
	List<MeetingAgenda> findAll(Sort sort);

}
