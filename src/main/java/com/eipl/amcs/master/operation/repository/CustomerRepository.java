package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.org.model.Society;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, String> {

    @Override
    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    List<Customer> findAll(Sort sort);

    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    Customer findByCode(String Code);

    @Override
    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    Optional<Customer> findById(String id);

    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    List<Customer> findAllBySociety(Society society, Sort sort);

    @EntityGraph(attributePaths = {"union", "society", "ledger"})
    Customer findByCodeAndType(String code, Integer type);

}
