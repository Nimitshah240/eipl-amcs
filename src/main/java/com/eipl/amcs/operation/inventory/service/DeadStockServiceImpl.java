package com.eipl.amcs.operation.inventory.service;

import com.eipl.amcs.operation.inventory.model.DeadStock;
import com.eipl.amcs.operation.inventory.repository.DeadStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DeadStockServiceImpl implements DeadStockService {

    private static final Logger log = LoggerFactory.getLogger(DeadStockServiceImpl.class);
    @Autowired
    private DeadStockRepository stockRepository;

    @Override
    public List<DeadStock> findAll() {
        List<DeadStock> list = stockRepository.findAll(Sort.by("code"));
        log.info("Product Stock findAll {} items fetched", list.size());
        return list;
    }

    @Override
    public DeadStock save(DeadStock DeadStock) {
        return stockRepository.save(DeadStock);
    }

    @Override
    public DeadStock update(DeadStock DeadStock) {
        return stockRepository.save(DeadStock);
    }

    @Override
    public Optional<DeadStock> findById(String code) {
        return stockRepository.findById(code);
    }

    @Override
    public void delete(String code) {
        stockRepository.deleteById(code);
    }

    @Override
    public DeadStock findByDeadStock(String code) {
        return stockRepository.findById(code).orElse(null);
    }

    @Override
    public void delete(DeadStock deadStock) {
        stockRepository.deleteById(deadStock.getCode());
    }

    @Override
    public List<DeadStock> findByPurchaseDateBetween(LocalDate fromDate, LocalDate toDate) {
        return stockRepository.findByPurchaseDateBetween(fromDate, toDate);
    }


}
