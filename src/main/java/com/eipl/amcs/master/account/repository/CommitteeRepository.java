package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Committee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CommitteeRepository extends BaseRepository<Committee, String> {

    @Override
    @EntityGraph(attributePaths = {"members.designation"})
    List<Committee> findAll(Sort sort);

    @Override
    @EntityGraph(attributePaths = {"members"})
    Optional<Committee> findById(String s);
}
