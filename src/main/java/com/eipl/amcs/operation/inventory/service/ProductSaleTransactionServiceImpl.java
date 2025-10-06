package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.dto.SaleTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductSaleTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductSaleRepository;
import com.eipl.amcs.operation.inventory.repository.ProductSaleTaxRepository;
import com.eipl.amcs.operation.inventory.repository.ProductSaleTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleTransactionServiceImpl implements ProductSaleTransactionService {

    @Autowired
    private ProductSaleTransactionRepository saleTransRepository;
    @Autowired
    private ProductSaleRepository productSaleRepository;
    @Autowired
    private ProductSaleTaxRepository productSaleTaxRepository;
    @Autowired
    private ProductSaleTaxRepository saleTaxRepository;


    private static final Logger log = LoggerFactory.getLogger(ProductSaleTransactionServiceImpl.class);

    @Override
    public List<ProductSaleTransaction> findAll() {
        List<ProductSaleTransaction> list = saleTransRepository.findAll(Sort.by("invoiceTransactionNo"));
        log.info("ProductSaleToMemberTransactions findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public ProductSaleTransaction save(ProductSaleTransaction productSaleToMemberTransaction) {
        return saleTransRepository.save(productSaleToMemberTransaction);
    }

    @Override
    public ProductSaleTransaction update(ProductSaleTransaction productSaleToMemberTransaction) {
        return saleTransRepository.save(productSaleToMemberTransaction);
    }

    @Override
    public Optional<ProductSaleTransaction> findById(String code) {
        return saleTransRepository.findById(code);
    }

    @Override
    public void delete(String code) {
        saleTransRepository.deleteById(code);
    }

    @Override
    @Transactional
    public void delete(ProductSaleTransaction productSaleToMemberTransaction) {
        saleTransRepository.deleteById(productSaleToMemberTransaction.getInvoiceTxnNo());
    }

    @Override
    public List<SaleTxnTaxDto> findByProductSale(String code) {
        List<ProductSaleTransaction> listTrans = saleTransRepository
                .findByProductSale(productSaleRepository.findById(code).orElse(null));
        List<SaleTxnTaxDto> listDto = new ArrayList<>();
        for (int i = 0; i < listTrans.size(); i++) {
            SaleTxnTaxDto dto = new SaleTxnTaxDto();
            dto.setTransaction(listTrans.get(i));
            dto.setSaleTaxList(saleTaxRepository.findByproductSaleTransaction(listTrans.get(i)));
            listDto.add(dto);
        }
        return listDto;
    }
}