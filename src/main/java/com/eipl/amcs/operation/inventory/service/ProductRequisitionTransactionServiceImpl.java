package com.eipl.amcs.operation.inventory.service;


import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionRepository;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionTransactionRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductRequisitionTransactionServiceImpl implements ProductRequisitionTransactionService {

    @Autowired
    private ProductRequisitionRepository productRequisitionRepository;
    @Autowired
    private ProductRequisitionTransactionRepository productRequisitionTransactionRepository;
    @Autowired
    private ProductRequisitionTransactionService productRequisitionTransactionService;

    @Override
    public List<ProductRequisitionTransaction> findAll() {
        return productRequisitionTransactionRepository.findAll();
    }

    @Override
    public ProductRequisitionTransaction save(ProductRequisitionTransaction productRequisitionTransaction) {
        return productRequisitionTransactionRepository.save(productRequisitionTransaction);
    }

    @Override
    public Optional<ProductRequisitionTransaction> findById(String code) {
        return productRequisitionTransactionRepository.findById(code);
    }

    @Override
    public void delete(String code) {
        productRequisitionRepository.deleteById(code);
    }

    @Override
    public ProductRequisitionDto findByProductReceipt(String code) {
        Optional<ProductRequisition> optionalProductRequisition = productRequisitionRepository.findById(code);
        if (optionalProductRequisition.isPresent()) {
            optionalProductRequisition.get().setSociety(Hibernate.unproxy(optionalProductRequisition.get().getSociety(), Society.class));
            return (new ProductRequisitionDto(optionalProductRequisition.get(), productRequisitionTransactionRepository.findByProductRequisition(optionalProductRequisition.get())));
        }
        return null;
    }
}