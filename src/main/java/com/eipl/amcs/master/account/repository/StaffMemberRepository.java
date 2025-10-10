package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.StaffMember;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffMemberRepository extends BaseRepository<StaffMember, String> {

    @Override
    @EntityGraph(attributePaths = {"bank", "branch", "society", "designation", "gender"})
    List<StaffMember> findAll(Sort sort);

}
