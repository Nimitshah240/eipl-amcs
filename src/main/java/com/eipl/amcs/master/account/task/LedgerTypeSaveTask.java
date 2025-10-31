package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerType;
import com.eipl.amcs.master.account.service.LedgerTypeService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class LedgerTypeSaveTask extends Task<Object> {

    private final LedgerType dto;
    private final short update;

    public LedgerTypeSaveTask(LedgerType dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerTypeService service = EmcsAppContext.getContext().getBean(LedgerTypeService.class);
            if (update == 0) {
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
