package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductPurchaseRate;
import com.eipl.amcs.master.inventory.service.ProductPurchaseRateService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class ProductPurchaseRateDeleteTask extends Task<Boolean> {
    private final String code;

    public ProductPurchaseRateDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductPurchaseRateService service = EmcsAppContext.getContext().getBean(ProductPurchaseRateService.class);
            Optional<ProductPurchaseRate> productData = service.findById(code);
            if (productData == null || !productData.isPresent())
                return null;
            service.delete(productData.get(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
