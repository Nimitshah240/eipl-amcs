package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.dto.BonusDto;
import com.eipl.amcs.operation.billing.service.BonusService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class BonusEditTask extends Task<Object> {
    private final BonusDto dto;

    public BonusEditTask(BonusDto dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            service.editDto(CommonUtils.setIdentityHeader(), dto);
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
