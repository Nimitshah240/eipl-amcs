package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.insurance.model.InsuranceDetail;
import com.eipl.amcs.master.insurance.service.InsuranceMasterService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class InsuranceDetailFinalizeSaveTask extends Task<Object> {

    private final InsuranceDetail dto;
    private final int process;

    public InsuranceDetailFinalizeSaveTask(InsuranceDetail dto, int process) {
        this.dto = dto;
        this.process = process;
    }

    @Override
    protected Object call() throws Exception {
        try {

            InsuranceMasterService service = EmcsAppContext.getContext().getBean(InsuranceMasterService.class);
            if (this.process == 0) {
                service.saveDetailsFinalize(dto, CommonUtils.setIdentityHeader());
            } else {
                service.updateDetailsFinalize(dto, CommonUtils.setIdentityHeader());
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
