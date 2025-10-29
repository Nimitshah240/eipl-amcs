package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.ProductSaleTax;
import com.eipl.amcs.operation.inventory.repository.ProductSaleTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleTaxServiceImpl implements ProductSaleTaxService {

    private static final Logger log = LoggerFactory.getLogger(ProductSaleTaxServiceImpl.class);
    @Autowired
    private ProductSaleTaxRepository saleTaxRepository;

    @Override
    public List<ProductSaleTax> findAll() {
        List<ProductSaleTax> list = saleTaxRepository.findAll(Sort.by("code"));
        log.info("ProductSaleToMemberTaxCalculated findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public ProductSaleTax save(ProductSaleTax productSaleToMemberTaxCalculated) {
        return saleTaxRepository.save(productSaleToMemberTaxCalculated);
    }

    @Override
    public ProductSaleTax update(ProductSaleTax productSaleToMemberTaxCalculated) {
        return saleTaxRepository.save(productSaleToMemberTaxCalculated);
    }

    @Override
    public Optional<ProductSaleTax> findById(String code) {
        return saleTaxRepository.findById(code);
    }

    @Override
    public void delete(String code) {
        saleTaxRepository.deleteById(code);
    }

    @Override
    @Transactional
    public void delete(ProductSaleTax productSaleToMemberTaxCalculated) {
        saleTaxRepository.deleteById(productSaleToMemberTaxCalculated.getCode());
    }

}