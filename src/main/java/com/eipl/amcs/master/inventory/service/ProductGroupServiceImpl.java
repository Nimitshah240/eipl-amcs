package com.eipl.amcs.master.inventory.service;

import com.eipl.amcs.master.inventory.model.ProductGroup;
import com.eipl.amcs.master.inventory.repository.ProductGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductGroupServiceImpl implements ProductGroupService {
    @Autowired
    private ProductGroupRepository productGroupRepository;

    private static final Logger log = LoggerFactory.getLogger(ProductGroupServiceImpl.class);

    @Override
    public List<ProductGroup> findAll() {
        List<ProductGroup> list = productGroupRepository.findAll(Sort.by("name"));
        log.info("ProductGroups findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public ProductGroup findByProductGroupCode(String code) {
        ProductGroup productGroup = productGroupRepository.findByCode(Integer.valueOf(code));
        return productGroup;
    }

}
