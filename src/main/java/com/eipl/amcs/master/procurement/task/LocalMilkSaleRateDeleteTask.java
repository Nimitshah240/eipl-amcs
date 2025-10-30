package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.Optional;

public class LocalMilkSaleRateDeleteTask extends Task<Boolean> {
    private final String code;

    public LocalMilkSaleRateDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            LocalMilkSaleRateService service = EmcsAppContext.getContext().getBean(LocalMilkSaleRateService.class);
            Optional<LocalMilkSaleRate> localMilkSaleRateData = service.findById(code);
            if (localMilkSaleRateData == null || !localMilkSaleRateData.isPresent())
                return null;
            service.delete(localMilkSaleRateData.get(), CommonUtils.setIdentityHeader());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
