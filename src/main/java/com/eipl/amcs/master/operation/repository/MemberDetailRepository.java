package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.model.MemberDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberDetailRepository extends BaseRepository<MemberDetail, String> {

	@EntityGraph(attributePaths = { "state", "gender", "bank", "branch", "district", "subDistrict", "village",
			"hamlet" })
	Optional<MemberDetail> findByMember(Member member);

	@Override
	@EntityGraph(attributePaths = { "state", "gender", "bank", "branch", "district", "subDistrict", "village",
			"hamlet" })
	Optional<MemberDetail> findById(String id);


	@Override
	@EntityGraph(attributePaths = { "state", "gender", "bank", "branch", "district", "subDistrict", "village",
			"hamlet" })
	List<MemberDetail> findAll();
}
