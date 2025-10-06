package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.operation.inventory.model.ProductStock;
import com.eipl.amcs.operation.inventory.repository.ProductStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductStockServiceImpl implements ProductStockService {

	@Autowired
	private ProductStockRepository stockRepository;
	@Autowired
	private ProductRepository productRepository;

	private static final Logger log = LoggerFactory.getLogger(ProductStockServiceImpl.class);

	@Override
	public List<ProductStock> findAll() {
		List<ProductStock> list = stockRepository.findAll(Sort.by("code"));
		log.info("Product Stock findAll {} items fetched", list.size());
		return list;
	}

	@Override
	public ProductStock save(ProductStock productStock) {
		return stockRepository.save(productStock);
	}

	@Override
	public ProductStock update(ProductStock productStock) {
		return stockRepository.save(productStock);
	}

	@Override
	public Optional<ProductStock> findById(String code) {
		return stockRepository.findById(code);
	}

	@Override
	public void delete(String code) {
		stockRepository.deleteById(code);
	}

	@Override
	@Transactional
	public void delete(ProductStock productStock) {
		stockRepository.deleteById(productStock.getCode());
	}

	@Override
	public ProductStock findByProduct(String code) {
		return stockRepository.findByProduct(productRepository.findById(code).get()).get();
	}

}
