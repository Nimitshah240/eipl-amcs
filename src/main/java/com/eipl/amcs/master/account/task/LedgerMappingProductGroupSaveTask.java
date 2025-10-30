package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerMappingProductGroup;
import com.eipl.amcs.master.account.service.LedgerMappingProductGroupService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class LedgerMappingProductGroupSaveTask extends Task<Object> {

    private final List<LedgerMappingProductGroup> dto;

    public LedgerMappingProductGroupSaveTask(List<LedgerMappingProductGroup> dto) {
        this.dto = dto;
    }


    @Override
    protected Object call() throws Exception {
        try {
            LedgerMappingProductGroupService service = EmcsAppContext.getContext().getBean(LedgerMappingProductGroupService.class);
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
