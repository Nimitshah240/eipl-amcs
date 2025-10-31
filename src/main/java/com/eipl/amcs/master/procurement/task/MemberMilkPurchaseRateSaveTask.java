package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.dto.MemberMilkPurchaseRateDto;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class MemberMilkPurchaseRateSaveTask extends Task<Object> {
    private final MemberMilkPurchaseRateDto dto;

    public MemberMilkPurchaseRateSaveTask(MemberMilkPurchaseRateDto dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MemberMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
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
