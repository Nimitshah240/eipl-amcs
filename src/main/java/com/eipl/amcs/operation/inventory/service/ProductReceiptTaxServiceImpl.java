package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.ProductReceiptTax;
import com.eipl.amcs.operation.inventory.repository.ProductReceiptTaxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.eipl.amcs.config.BeanConfig.receiptTaxRepository;

@Service
public class ProductReceiptTaxServiceImpl implements ProductReceiptTaxService {
//	@Autowired
//	private ProductReceiptTaxRepository receiptTaxRepository;

	private static final Logger log = LoggerFactory.getLogger(ProductReceiptTaxServiceImpl.class);

	@Override
	public List<ProductReceiptTax> findAll() {
		List<ProductReceiptTax> list = receiptTaxRepository.findAll(Sort.by("code"));
		log.info("ProductReceiptTax findAll {} items fetched", list.size());
		return list;
	}

	@Override
	public ProductReceiptTax save(ProductReceiptTax productReceiptTax) {
		return receiptTaxRepository.save(productReceiptTax);
	}

	@Override
	public ProductReceiptTax update(ProductReceiptTax productReceiptTax) {
		return receiptTaxRepository.save(productReceiptTax);
	}

	@Override
	public Optional<ProductReceiptTax> findById(String code) {
		return receiptTaxRepository.findById(code);
	}

	@Override
	public void delete(String code) {
		receiptTaxRepository.deleteById(code);
	}

	@Override
	@Transactional
	public void delete(ProductReceiptTax productReceiptTax) {
		receiptTaxRepository.deleteById(productReceiptTax.getCode());
	}

}