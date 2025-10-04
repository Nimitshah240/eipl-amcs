package com.eipl.amcs.operation.inventory.service;


import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.inventory.bootdto.ProductDispatchDto;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductDispatchRepository;
import com.eipl.amcs.operation.inventory.repository.ProductDispatchTransactionRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.config.BeanConfig.productDispatchRepository;
import static com.eipl.amcs.config.BeanConfig.productDispatchTransactionRepository;

@Service
public class ProductDispatchServiceImpl implements ProductDispatchService {
//    @Autowired
//    private ProductDispatchRepository productDispatchRepository;
//    @Autowired
//    private ProductDispatchTransactionRepository productDispatchTransactionRepository;

    @Override
    public List<ProductDispatch> findAll() {
        return productDispatchRepository.findAll();
    }

    @Override
    public ProductDispatch save(ProductDispatch dto, String identityInfo) {
        dto.setInitData();
        productDispatchRepository.customSave(dto, identityInfo);
        return null;
    }

    @Override
    public ProductDispatchTransaction save(ProductDispatchTransaction dto, String identityInfo) {
        dto.setInitData();
        productDispatchTransactionRepository.customSave(dto, identityInfo);
        return null;
    }

    @Override
    public List<ProductDispatchTransaction> findByDispatchDate(LocalDate fromDt, LocalDate toDt) {
        List<ProductDispatchTransaction> list = productDispatchTransactionRepository.findByDispatchDateBetween(fromDt, toDt);
        for (ProductDispatchTransaction requisition : list) {
            requisition.setSociety(Hibernate.unproxy(requisition.getSociety(), Society.class));
            requisition.setProductDispatch(Hibernate.unproxy(requisition.getProductDispatch(), ProductDispatch.class));
            requisition.setProduct(Hibernate.unproxy(requisition.getProduct(), Product.class));
            requisition.setProductRequisition(Hibernate.unproxy(requisition.getProductRequisition(), ProductRequisition.class));
            requisition.setProductRequisitionTransaction(Hibernate.unproxy(requisition.getProductRequisitionTransaction(), ProductRequisitionTransaction.class));
        }
        return list;
    }

    @Override
    public ProductDispatchDto update(ProductDispatchDto dto, String identityInfo) {
        return null;
    }

    @Override
    public ProductDispatchDto save(ProductDispatchDto dto, String identityInfo) {
        return null;
    }

    @Override
    public Optional<ProductDispatch> findById(String challanNo) {
        return productDispatchRepository.findById(challanNo);
    }

    @Override
    public void delete(String challanNo) {
        productDispatchRepository.deleteById(challanNo);
    }
}