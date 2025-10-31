package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.BmcRunningHours;
import com.eipl.amcs.operation.procurement.service.BmcRunningHoursService;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class BmcRunningHrsSaveTask extends Task<Object> {
    private final BmcRunningHours dto;
    private final short update;

    public BmcRunningHrsSaveTask(BmcRunningHours dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            if (dto == null) {
                return false;
            }

            BmcRunningHoursService service = EmcsAppContext.getContext().getBean(BmcRunningHoursService.class);

            if (this.update == 0) {
                service.save(dto);
            } else {
                service.update(dto);
            }
            return true;
        } catch (HttpStatusCodeException e) {
            // Handle exceptions from the API call and parse the error response
            String errorResponse = e.getResponseBodyAsString();
            EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}