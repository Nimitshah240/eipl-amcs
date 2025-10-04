package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.master.account.model.Mom;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface MomRepository extends BaseRepository<Mom, String> {

	@Override
	@EntityGraph(attributePaths = {"society", "union","MeetingAgenda"})
	List<Mom> findAll(Sort sort);


	@EntityGraph(attributePaths = {"society", "union"})
	List<Mom> findByMeetingAgenda(MeetingAgenda meetingAgenda);



}
