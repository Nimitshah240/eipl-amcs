package com.eipl.amcs.operation.administartion.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffSalaryHead;
import com.eipl.amcs.master.account.service.StaffSalaryHeadService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class StaffSalaryHeadSaveTask extends Task<Object> {

    private final StaffSalaryHead dto;
    private final short update;

    public StaffSalaryHeadSaveTask(StaffSalaryHead dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            StaffSalaryHeadService service = EmcsAppContext.getContext().getBean(StaffSalaryHeadService.class);
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
