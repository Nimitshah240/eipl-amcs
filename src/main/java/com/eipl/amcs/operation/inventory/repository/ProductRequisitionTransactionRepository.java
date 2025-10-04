package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRequisitionTransactionRepository extends BaseRepository<ProductRequisitionTransaction, String> {

    @EntityGraph(attributePaths = {"product", "productRequisition"})
    List<ProductRequisitionTransaction> findByProductRequisition(ProductRequisition productRequisition);


}
