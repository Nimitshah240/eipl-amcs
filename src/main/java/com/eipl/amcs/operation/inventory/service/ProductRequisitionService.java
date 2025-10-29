package com.eipl.amcs.operation.inventory.service;


import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRequisitionService {

    List<ProductRequisition> findAll();

    ProductRequisitionDto save(ProductRequisitionDto dto, String identityInfo);

    ProductRequisition save(ProductRequisition dto, String identityInfo);

    ProductRequisitionTransaction save(ProductRequisitionTransaction dto, String identityInfo);

    ProductRequisitionDto update(ProductRequisitionDto dto, String identityInfo);

    Optional<ProductRequisition> findById(String code);


    void delete(String code, String identityInfo);

    List<ProductRequisition> findByDate(LocalDate fromDt, LocalDate toDt);


}
