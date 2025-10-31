package com.eipl.amcs.master.inventory.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.service.ProductService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class ProductDeleteTask extends Task<Boolean> {
    private final String code;

    public ProductDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            ProductService service = EmcsAppContext.getContext().getBean(ProductService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
