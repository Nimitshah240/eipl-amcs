package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommitteeMembersRepository extends BaseRepository<CommitteeMembers, String> {

    @Override
    @EntityGraph(attributePaths = {"society", "designation"})
    List<CommitteeMembers> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"society", "designation"})
    Optional<CommitteeMembers> findById(String s);

    void deleteByCommittee_Code(String code);
}
