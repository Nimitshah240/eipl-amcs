package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class InsuranceDetailDeleteTask extends Task<Object> {
    private final InsuranceDetail dto;

    public InsuranceDetailDeleteTask(InsuranceDetail dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            InsuranceMasterService service = EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            service.deleteDetails(dto, CommonUtils.setIdentityHeader());
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}