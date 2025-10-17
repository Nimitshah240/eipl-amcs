package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.exception.EntityNotFoundException;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.repository.ProductRepository;
import com.eipl.amcs.master.inventory.service.ProductPurchaseRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ProductPurchaseRateByProductTask extends Task<ProductPurchaseRate> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPurchaseRateByProductTask.class);

    private final String code;
    private final LocalDate date;


    public ProductPurchaseRateByProductTask(String code, LocalDate date) {
        this.code = code;
        this.date = date;
    }

    @Override
    protected ProductPurchaseRate call() throws Exception {
        try {
            ProductPurchaseRateService service = EmcsAppContext.getContext().getBean(ProductPurchaseRateService.class);
            ProductRepository productRepository = EmcsAppContext.getContext().getBean(ProductRepository.class);

            Product product = productRepository.findById(code)
                    .orElseThrow(() -> new EntityNotFoundException(Product.class, ""));
            return service.findProductRate(product, date);
        } catch (Exception e) {
            LOGGER.error("ProductSaleRate fetch", e);
        }
        return null;
    }
}
