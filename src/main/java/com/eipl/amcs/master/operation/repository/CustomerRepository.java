package com.eipl.amcs.master.operation.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.org.model.Society;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, String> {

    @Override
    @EntityGraph(attributePaths = {"union", "society"})
    List<Customer> findAll(Sort sort);

    @Query(value = "SELECT count(*) FROM Customer m WHERE m.name = ?1 and m.code != ?2")
    Long checkName(String str1, String str2);

    @Query(value = "SELECT count(*) FROM CustomerDetails m WHERE m.panNo = ?1 AND m.code != ?2")
    Long checkPanNo(String str1, String str2);

    @Query(value = "SELECT count(*) FROM CustomerDetails m WHERE m.aadharCardNo = ?1 AND m.code != ?2")
    Long checkAadharNo(String str1, String str2);

    @Query(value = "SELECT count(*) FROM Customer m WHERE m.code = ?1")
    Long checkCode(String str);

    @EntityGraph(attributePaths = {"union", "society"})
    public Customer findByCode(String Code);

    @Override
    @EntityGraph(attributePaths = {"union", "society"})
    Optional<Customer> findById(String id);

    @EntityGraph(attributePaths = {"union", "society"})
    List<Customer> findAllBySociety(Society society, Sort sort);

}
