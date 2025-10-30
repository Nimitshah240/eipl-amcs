package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.service.MemberMilkPurchaseRateService;
import com.eipl.amcs.master.procurement.service.SocietyMilkPurchaseRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RateViewTask extends Task<List<String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateViewTask.class);
    private final short rateType;
    private final String code;
    private final Integer milkType;
    private final Integer milkQualityType;

    public RateViewTask(short rateType, String code, Integer milkType, Integer milkQualityType) {
        this.rateType = rateType;
        this.code = code;
        this.milkType = milkType;
        this.milkQualityType = milkQualityType;
    }

    @Override
    protected List<String> call() throws Exception {
        try {
            List<String> listStr = null;
            if (rateType == (short) 0) {
                MemberMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(MemberMilkPurchaseRateService.class);
                listStr = service.fetchRateDetails(code, milkType, milkQualityType);
                if (listStr == null || listStr.isEmpty()) {
                    return null;
                }
            } else {
                SocietyMilkPurchaseRateService service = EmcsAppContext.getContext().getBean(SocietyMilkPurchaseRateService.class);
                listStr = service.fetchRateDetails(code, milkType, milkQualityType);
                if (listStr == null || listStr.isEmpty()) {
                    return null;
                }
            }
            return listStr;
        } catch (Exception e) {
            LOGGER.error("Rate Fetch fetch", e);
        }
        return null;
    }

}
