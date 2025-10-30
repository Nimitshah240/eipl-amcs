package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.dto.SocietyMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class SocietyMilkPurchaseRateSaveTask extends Task<Object> {
    private final SocietyMilkPurchaseRateDto dto;

    public SocietyMilkPurchaseRateSaveTask(SocietyMilkPurchaseRateDto dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            SocietyMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
            if (dto == null)
                return null;
            return service.savePurchaseRate(dto);
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
