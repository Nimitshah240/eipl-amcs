package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.service.BonusService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class BonusSaveTask extends Task<Object> {
    private final BonusDto dto;
    private final short update;

    public BonusSaveTask(BonusDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            if (this.update == 0) {
                service.saveDto(CommonUtils.setIdentityHeader(), dto, (short) 0);
            } else {
                service.updateDto(CommonUtils.setIdentityHeader(), dto);
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
