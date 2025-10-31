package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffSalaryMapping;
import com.eipl.amcs.master.account.service.StaffSalaryMappingService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class StaffSalaryMappingSaveTask extends Task<Object> {
    private final List<StaffSalaryMapping> paymentCycleList;
    private final short update;

    public StaffSalaryMappingSaveTask(List<StaffSalaryMapping> paymentCycleList, short update) {
        this.paymentCycleList = paymentCycleList;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            StaffSalaryMappingService service = EmcsAppContext.getContext().getBean(StaffSalaryMappingService.class);
            if (this.update == 0) {
                return service.save(paymentCycleList, CommonUtils.setIdentityHeader());
            }
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
