package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.ProductSaleRate;
import com.eipl.amcs.master.inventory.service.ProductSaleRateService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductSaleRateSaveTask extends Task<Object> {
    private final ProductSaleRate dto;
    private final short update;

    public ProductSaleRateSaveTask(ProductSaleRate dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            ProductSaleRateService service = EmcsAppContext.getContext().getBean(ProductSaleRateService.class);
            if (this.update == 0) {
                service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                service.update(dto, CommonUtils.setIdentityHeader());
            }
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
