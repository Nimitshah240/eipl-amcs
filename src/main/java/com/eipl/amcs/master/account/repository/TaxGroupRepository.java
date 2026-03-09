package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Taxgroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxGroupRepository extends BaseRepository<Taxgroup, Integer> {

    @EntityGraph(attributePaths = {"union"})
    List<Taxgroup> findAll();

    @Override
    Optional<Taxgroup> findById(Integer s);
}
