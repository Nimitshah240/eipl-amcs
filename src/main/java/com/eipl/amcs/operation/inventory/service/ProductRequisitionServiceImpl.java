package com.eipl.amcs.operation.inventory.service;


import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.model.ProductRequisition;
import com.eipl.amcs.operation.inventory.model.ProductRequisitionTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionRepository;
import com.eipl.amcs.operation.inventory.repository.ProductRequisitionTransactionRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductRequisitionServiceImpl implements ProductRequisitionService {
    @Autowired
    private ProductRequisitionRepository productRequisitionRepository;
    @Autowired
    private ProductRequisitionTransactionRepository productRequisitionTransactionRepository;

    @Override
    public List<ProductRequisition> findAll() {
        return productRequisitionRepository.findAll();
    }

    @Override
    public ProductRequisitionDto save(ProductRequisitionDto dto, String identityInfo) {
        dto.getProductRequisition().setInitData();
        dto.getProductRequisition().setxCol1(UUID.randomUUID().toString());
        ProductRequisition requisition = productRequisitionRepository.customSave(dto.getProductRequisition(), identityInfo);
        int i = 1;
        for (ProductRequisitionTransaction transaction : dto.getTransactionList()) {
            transaction.setInitData();
            transaction.setProductRequisition(requisition);
            transaction.setCode(requisition.getCode() + "T" + i++);
            transaction.setxCol1(UUID.randomUUID().toString());
            productRequisitionTransactionRepository.customSave(transaction, identityInfo);
        }
        return null;
    }

    @Override
    public ProductRequisition save(ProductRequisition dto, String identityInfo) {
        Optional<ProductRequisition> requisitionOptional = productRequisitionRepository.findById(dto.getCode());
        if (requisitionOptional.isPresent()) {
            ProductRequisition requisition = requisitionOptional.get();
            requisition.setStatus(dto.getStatus());
            requisition.setCancelledBy(dto.getCancelledBy());
            requisition.setCancelledAt(dto.getCancelledAt());
            requisition.setCancel(dto.getCancel());
            productRequisitionRepository.customUpdate(requisition, identityInfo);
        }
        return null;

    }

    @Override
    public ProductRequisitionTransaction save(ProductRequisitionTransaction dto, String identityInfo) {
        Optional<ProductRequisitionTransaction> transactionOptional = productRequisitionTransactionRepository.findById(dto.getCode());
        if (transactionOptional.isPresent()) {
            ProductRequisitionTransaction transaction = transactionOptional.get();
            transaction.setStatus(dto.getStatus());
            transaction.setCancelledAt(dto.getCancelledAt());
            transaction.setCancelledBy(dto.getCancelledBy());
            transaction.setApprovedBy(dto.getApprovedBy());
            transaction.setApprovedDate(dto.getApprovedDate());
            transaction.setApprovedQuantity(dto.getApprovedQuantity());
            transaction.setIsApproved(dto.getIsApproved());
            transaction.setAmount(dto.getAmount());
            transaction.setRate(dto.getRate());
            transaction.setQuantity(dto.getQuantity());
            transaction.setProduct(Hibernate.unproxy(transaction.getProduct(), Product.class));
            transaction.setProductRequisition(Hibernate.unproxy(transaction.getProductRequisition(), ProductRequisition.class));
            productRequisitionTransactionRepository.customUpdate(transaction, identityInfo);
        }
        return null;

    }

    @Override
    public ProductRequisitionDto update(ProductRequisitionDto dto, String identityInfo) {
        dto.getProductRequisition().setupdateData();
        ProductRequisition requisition = productRequisitionRepository.customUpdate(dto.getProductRequisition(), identityInfo);
        int i = 1;
        // Existing product requisition transactions
        List<ProductRequisitionTransaction> listTxn = productRequisitionTransactionRepository.findByProductRequisition(requisition);
        for (ProductRequisitionTransaction txn : listTxn) {
            i++;
            productRequisitionTransactionRepository.customDelete(txn, identityInfo);
        }
        for (ProductRequisitionTransaction transaction : dto.getTransactionList()) {
            transaction.setProductRequisition(requisition);
            transaction.setCode(requisition.getCode() + "T" + i++);
            transaction.setInitData();
            productRequisitionTransactionRepository.customSave(transaction, identityInfo);
        }
        return null;
    }

    @Override
    public Optional<ProductRequisition> findById(String code) {
        return productRequisitionRepository.findById(code);
    }

    @Override
    public void delete(String code, String identityInfo) {
        Optional<ProductRequisition> requisition = productRequisitionRepository.findById(code);
        if (requisition.isPresent()) {
            for (ProductRequisitionTransaction transaction : productRequisitionTransactionRepository.findByProductRequisition(requisition.get())) {
                productRequisitionTransactionRepository.customDelete(transaction, identityInfo);
            }
        }
        productRequisitionRepository.customDelete(code, identityInfo);
    }

    @Override
    public List<ProductRequisition> findByDate(LocalDate fromDt, LocalDate toDt) {
        List<ProductRequisition> list = productRequisitionRepository.findByRequisitionDateBetweenOrderByRequisitionDateDesc(fromDt.atStartOfDay(), toDt.atTime(23, 11, 59));
        for (ProductRequisition requisition : list) {
            requisition.setSociety(Hibernate.unproxy(requisition.getSociety(), Society.class));
        }
        return list;
    }
}