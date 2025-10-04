package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductDispatchRepository extends BaseRepository<ProductDispatch, String> {


    List<ProductDispatch> findByDispatchDateBetween(LocalDate fromDt, LocalDate toDt);
}
