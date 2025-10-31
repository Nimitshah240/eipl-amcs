package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class MomSaveTask extends Task<Object> {

    private final Mom dto;
    private final short update;

    public MomSaveTask(Mom dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MeetingAgendaService service = EmcsAppContext.getContext().getBean(MeetingAgendaService.class);

            if (this.update == 0) {
                service.saveMom(dto, CommonUtils.setIdentityHeader());
            } else {
                service.updateMom(dto, CommonUtils.setIdentityHeader());
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
