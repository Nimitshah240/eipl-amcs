package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.inventory.service.ProductSaleRateService;
import com.eipl.amcs.master.org.model.Union;
import javafx.concurrent.Task;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ProductSaleRateByProductLoadTask extends Task<ProductSaleRate> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaleRateByProductLoadTask.class);

    private final String code;
    private final LocalDate date;

    private ProductSaleRateService service;
    private ProductRepository repository;

    public ProductSaleRateByProductLoadTask(String code, LocalDate date) {
        this.code = code;
        this.date = date;
    }

    @Override
    protected ProductSaleRate call() throws Exception {
        try {

            ProductSaleRate list = service.findByProduct(repository.findById(code).get(), date);
            list.setUnion(Hibernate.unproxy(list.getUnion(), Union.class));
            return list;
        } catch (Exception e) {
            LOGGER.error("ProductSaleRate fetch", e);
        }
        return null;
    }
}
