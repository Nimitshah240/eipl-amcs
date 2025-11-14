package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class SocietyPaymentCycleSaveTask extends Task<Object> {
    private final List<SocietyPaymentCycle> paymentCycleList;
    private final short update;

    public SocietyPaymentCycleSaveTask(List<SocietyPaymentCycle> paymentCycleList, short update) {
        this.paymentCycleList = paymentCycleList;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            SocietyPaymentCycleService service = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);
            if (paymentCycleList == null || paymentCycleList.isEmpty())
                return null;
            if (this.update == 0) {
                return service.save(paymentCycleList, CommonUtils.setIdentityHeader());
            }
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            return new RuntimeException("erorr");
        }
        return null;
    }
}
