package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.repository.SocietyPaymentCycleRepository;
import com.eipl.amcs.operation.inventory.model.ProductSaleInstallment;
import com.eipl.amcs.operation.inventory.repository.ProductSaleInstallmentRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleInstallmentServiceImpl implements ProductSaleInstallmentService {
    private static final Logger log = LoggerFactory.getLogger(ProductSaleInstallmentServiceImpl.class);
    @Autowired
    private ProductSaleInstallmentRepository installmentRepository;
    @Autowired
    private SocietyPaymentCycleRepository paymentCycleRepository;

    @Override
    public List<ProductSaleInstallment> findAll() {
        List<ProductSaleInstallment> list = installmentRepository.findAll(Sort.by("installmentNo"));
        log.info("ProductSaleToMemberInstallments findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public ProductSaleInstallment save(ProductSaleInstallment productSaleToMemberInstallment) {
        return installmentRepository.save(productSaleToMemberInstallment);
    }

    @Override
    public ProductSaleInstallment update(ProductSaleInstallment productSaleToMemberInstallment) {
        return installmentRepository.save(productSaleToMemberInstallment);
    }

    @Override
    public Optional<ProductSaleInstallment> findById(String code) {
        return installmentRepository.findById(code);
    }

    @Override
    public void delete(String code) {
        installmentRepository.deleteById(code);
    }

    @Override
    @Transactional
    public void delete(ProductSaleInstallment productSaleToMemberInstallment) {
        installmentRepository.deleteById(productSaleToMemberInstallment.getCode());
    }

    @Override
    public List<ProductSaleInstallment> fetchInstallmentIsBilled(String str, boolean b) {
        List<ProductSaleInstallment> installmentList = installmentRepository.fetchInstallmentIsBilled(str, b);
        for (ProductSaleInstallment productSaleInstallment : installmentList) {
            productSaleInstallment.setSocietyPaymentCycle(Hibernate.unproxy(productSaleInstallment.getSocietyPaymentCycle(), SocietyPaymentCycle.class));
            productSaleInstallment.setMember(Hibernate.unproxy(productSaleInstallment.getMember(), Member.class));
        }
        return installmentList;
    }

    @Override
    public List<ProductSaleInstallment> fetchByPaymentCycle(String str) {
        List<ProductSaleInstallment> installmentList = installmentRepository.findByInvoiceNo(str);
        for (ProductSaleInstallment productSaleInstallment : installmentList) {
            productSaleInstallment.setSocietyPaymentCycle(Hibernate.unproxy(productSaleInstallment.getSocietyPaymentCycle(), SocietyPaymentCycle.class));
            productSaleInstallment.setMember(Hibernate.unproxy(productSaleInstallment.getMember(), Member.class));
        }
        return installmentList;
    }

}