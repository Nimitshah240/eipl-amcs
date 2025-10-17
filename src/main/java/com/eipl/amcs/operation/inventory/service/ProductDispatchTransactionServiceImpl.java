package com.eipl.amcs.operation.inventory.service;


import com.eipl.amcs.operation.inventory.model.ProductDispatchTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductDispatchTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductDispatchTransactionServiceImpl implements ProductDispatchTransactionService {

    @Autowired
    private ProductDispatchTransactionRepository productDispatchTransactionRepository;


    @Override
    public List<ProductDispatchTransaction> findAll() {
        return productDispatchTransactionRepository.findAll();
    }

    @Override
    public ProductDispatchTransaction save(ProductDispatchTransaction productDispatchTransaction) {
        return productDispatchTransactionRepository.save(productDispatchTransaction);
    }

    @Override
    public Optional<ProductDispatchTransaction> findById(String code) {
        return productDispatchTransactionRepository.findById(code);
    }

    @Override
    public void delete(String code) {
        productDispatchTransactionRepository.deleteById(code);
    }
}