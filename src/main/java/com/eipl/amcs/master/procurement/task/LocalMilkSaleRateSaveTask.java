package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class LocalMilkSaleRateSaveTask extends Task<Object> {
    private final LocalMilkSaleRate dto;
    private final short update;

    public LocalMilkSaleRateSaveTask(LocalMilkSaleRate dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            LocalMilkSaleRateService service = EmcsAppContext.getContext().getBean(LocalMilkSaleRateService.class);
            if (dto == null)
                return null;
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
