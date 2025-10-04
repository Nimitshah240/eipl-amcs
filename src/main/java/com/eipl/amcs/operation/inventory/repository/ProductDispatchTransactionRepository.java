package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductDispatchTransactionRepository extends BaseRepository<ProductDispatchTransaction, String> {

    List<ProductDispatchTransaction> findByDispatchDateBetween(LocalDate fromDt, LocalDate toDt);
}
