package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Vendor;
import com.eipl.amcs.master.org.model.Society;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends BaseRepository<Vendor, String> {

    @Override
    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    List<Vendor> findAll(Sort sort);

    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    Vendor findByCode(String Code);

    @Override
    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    Optional<Vendor> findById(String id);

    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    List<Vendor> findAllBySociety(Society society, Sort sort);

    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    Vendor findByCodeAndVendorType(String code, String type);
}
