package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerMappingBillHead;
import com.eipl.amcs.master.account.service.LedgerMappingBillHeadService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class LedgerMappingBillHeadSaveTask extends Task<Object> {

    private final List<LedgerMappingBillHead> dto;

    public LedgerMappingBillHeadSaveTask(List<LedgerMappingBillHead> dto) {
        this.dto = dto;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerMappingBillHeadService service = EmcsAppContext.getContext().getBean(LedgerMappingBillHeadService.class);
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
