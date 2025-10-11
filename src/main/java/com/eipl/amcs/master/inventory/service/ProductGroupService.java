package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.master.inventory.model.ProductGroup;

import java.util.List;

public interface ProductGroupService {
    List<ProductGroup> findAll();

    ProductGroup findByProductGroupCode(String code);

}
