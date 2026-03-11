package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.TaxDepend;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxDependsRepository extends BaseRepository<TaxDepend, String> {

    @EntityGraph(attributePaths = {"taxDetail", "taxDetails"})
    List<TaxDepend> findAll();

    @Override
    Optional<TaxDepend> findById(String s);
}