package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerOpeningBalance;
import com.eipl.amcs.master.account.service.LedgerOpeningBalanceService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class LedgerOpeningBalanceSaveTask extends Task<Object> {

    private final LedgerOpeningBalance dto;
    private final short update;

    public LedgerOpeningBalanceSaveTask(LedgerOpeningBalance dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerOpeningBalanceService service = EmcsAppContext.getContext().getBean(LedgerOpeningBalanceService.class);
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
