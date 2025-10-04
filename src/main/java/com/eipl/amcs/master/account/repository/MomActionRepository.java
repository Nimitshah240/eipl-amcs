package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.model.MomAction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface MomActionRepository extends BaseRepository<MomAction, String> {

	@Override
	@EntityGraph(attributePaths = {"society", "union","meetingAgenda","mom"})
	List<MomAction> findAll(Sort sort);


	@EntityGraph(attributePaths = {"society", "union","meetingAgenda","mom"})
	List<MomAction> findByMom(Mom mom);

}
