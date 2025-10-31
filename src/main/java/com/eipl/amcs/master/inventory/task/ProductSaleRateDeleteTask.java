package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.service.ProductSaleRateService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class ProductSaleRateDeleteTask extends Task<Boolean> {
    private final String code;

    public ProductSaleRateDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductSaleRateService service = EmcsAppContext.getContext().getBean(ProductSaleRateService.class);
            Optional<ProductSaleRate> productData = service.findById(code);
            if (productData == null || productData.isEmpty())
                return null;
            service.delete(productData.get(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
