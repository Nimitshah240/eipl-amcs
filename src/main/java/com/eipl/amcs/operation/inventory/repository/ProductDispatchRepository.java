package com.eipl.amcs.operation.inventory.repository;

import com.eipl.amcs.base.repository.BaseRepository;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDispatchRepository extends BaseRepository<ProductDispatch, String> {
}
