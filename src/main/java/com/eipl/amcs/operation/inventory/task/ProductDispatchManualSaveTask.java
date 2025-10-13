package com.eipl.amcs.operation.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.model.ProductDispatch;
import com.eipl.amcs.operation.inventory.service.ProductDispatchService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class ProductDispatchManualSaveTask extends Task<Object> {
    private final ProductDispatch dto;
    private final short update;

    public ProductDispatchManualSaveTask(ProductDispatch dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            ProductDispatchService service = EmcsAppContext.getContext().getBean(ProductDispatchService.class);
            service.save(dto, CommonUtil.setIdentityHeader());
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
