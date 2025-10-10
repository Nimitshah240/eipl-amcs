package com.eipl.amcs.master.account.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.account.model.Tax;
import com.eipl.amcs.master.account.model.TaxDetail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxDetailRepository extends BaseRepository<TaxDetail, String> {

    @EntityGraph(attributePaths = {"basicTax", "tax"})
    List<TaxDetail> findAll();

    @EntityGraph(attributePaths = {"basicTax", "tax"})
    List<TaxDetail> findByTax(Tax tax);

    @Override
    Optional<TaxDetail> findById(String s);
}
