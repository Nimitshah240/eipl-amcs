package com.eipl.amcs.operation.inventory.service;


import com.eipl.amcs.operation.inventory.bootdto.ProductDispatchDto;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductDispatchService {
    List<ProductDispatch> findAll();


    ProductDispatch save(ProductDispatch dto, String identityInfo);

    ProductDispatchTransaction save(ProductDispatchTransaction dto, String identityInfo);
    ProductDispatchDto update(ProductDispatchDto dto, String identityInfo);

    ProductDispatchDto save(ProductDispatchDto dto, String identityInfo);

    List<ProductDispatchTransaction> findByDispatchDate(LocalDate fromDt, LocalDate toDt);

    Optional<ProductDispatch> findById(String challanNo);

    void delete(String challanNo);
}
