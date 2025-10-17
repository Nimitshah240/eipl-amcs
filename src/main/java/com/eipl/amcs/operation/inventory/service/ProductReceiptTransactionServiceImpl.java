package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.dto.ReceiptTxnTaxDto;
import com.eipl.amcs.operation.inventory.model.ProductReceiptTransaction;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptRepository;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptTaxRepository;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptTransactionRepository;
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
public class ProductReceiptTransactionServiceImpl implements ProductReceiptTransactionService {
    private static final Logger log = LoggerFactory.getLogger(ProductReceiptTransactionServiceImpl.class);
    @Autowired
    private ProductReceiptTransactionRepository receiptTransRepository;
    @Autowired
    private ProductReceiptRepository productReceiptRepository;
    @Autowired
    private ProductReceiptTaxRepository receiptTaxRepository;

    @Override
    public List<ProductReceiptTransaction> findAll() {
        List<ProductReceiptTransaction> list = receiptTransRepository.findAll(Sort.by("grnTxnNo"));
        log.info("ProductReceiptTransactions findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public ProductReceiptTransaction save(ProductReceiptTransaction productReceiptTransaction) {
        return receiptTransRepository.save(productReceiptTransaction);
    }

    @Override
    public ProductReceiptTransaction update(ProductReceiptTransaction productReceiptTransaction) {
        return receiptTransRepository.save(productReceiptTransaction);
    }

    @Override
    public Optional<ProductReceiptTransaction> findById(String code) {
        return receiptTransRepository.findById(code);
    }

    @Override
    public void delete(String code) {
        receiptTransRepository.deleteById(code);
    }

    @Override
    @Transactional
    public void delete(ProductReceiptTransaction productReceiptTransaction) {
        receiptTransRepository.deleteById(productReceiptTransaction.getGrnTxnNo());
    }

    @Override
    public List<ReceiptTxnTaxDto> findByProductReceipt(String code) {
        // TODO Auto-generated method stub
        List<ProductReceiptTransaction> listTrans = receiptTransRepository.findByProductReceipt(productReceiptRepository.findById(code).orElse(null));
        List<ReceiptTxnTaxDto> listDto = new ArrayList<>();
        for (int i = 0; i < listTrans.size(); i++) {
            ReceiptTxnTaxDto dto = new ReceiptTxnTaxDto();
            dto.setTransaction(listTrans.get(i));
            dto.setReceiptTaxList(receiptTaxRepository.findByproductReceiptTransaction(listTrans.get(i)));
            listDto.add(dto);
        }
        return listDto;
    }

}