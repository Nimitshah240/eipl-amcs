package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerMappingEvent;
import com.eipl.amcs.master.account.service.LedgerMappingEventService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class LedgerMappingEventSaveTask extends Task<Object> {

    private final List<LedgerMappingEvent> dto;

    public LedgerMappingEventSaveTask(List<LedgerMappingEvent> dto) {
        this.dto = dto;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerMappingEventService service = EmcsAppContext.getContext().getBean(LedgerMappingEventService.class);
            service.save(dto, CommonUtils.setIdentityHeader());
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
