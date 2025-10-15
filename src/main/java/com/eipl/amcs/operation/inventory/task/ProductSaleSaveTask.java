package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.ProductSaleDto;
import com.eipl.amcs.operation.inventory.service.ProductSaleService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductSaleSaveTask extends Task<Object> {
    private final ProductSaleDto dto;
    private final short update;

    public ProductSaleSaveTask(ProductSaleDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            ProductSaleService service = EmcsAppContext.getContext().getBean(ProductSaleService.class);
            if (this.update == 0)
                service.save(dto, CommonUtil.setIdentityHeader());
            else
                service.update(dto, CommonUtil.setIdentityHeader());
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
