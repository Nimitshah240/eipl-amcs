package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Tax;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxRepository extends BaseRepository<Tax, String> {

    @EntityGraph(attributePaths = {"union"})
    List<Tax> findAll();

    @Override
    Optional<Tax> findById(String s);
}
