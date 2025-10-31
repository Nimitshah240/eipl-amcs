package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.dto.ProductRequisitionDto;
import com.eipl.amcs.operation.inventory.service.ProductRequisitionService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductRequisitionSaveTask extends Task<Object> {
    private final ProductRequisitionDto dto;
    private final short update;

    public ProductRequisitionSaveTask(ProductRequisitionDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            ProductRequisitionService service = EmcsAppContext.getContext().getBean(ProductRequisitionService.class);
            if (this.update == 0) {
                service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                service.update(dto, CommonUtils.setIdentityHeader());
            }
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
