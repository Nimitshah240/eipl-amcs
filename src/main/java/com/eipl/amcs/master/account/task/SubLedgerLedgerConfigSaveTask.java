package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SubLedgerLedgerConfig;
import com.eipl.amcs.master.account.service.SubLedgerLedgerConfigService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class SubLedgerLedgerConfigSaveTask extends Task<Object> {

    private final List<SubLedgerLedgerConfig> dto;
    private final String code;

    public SubLedgerLedgerConfigSaveTask(List<SubLedgerLedgerConfig> dto, String code) {
        this.dto = dto;
        this.code = code;
    }


    @Override
    protected Object call() throws Exception {
        try {
            SubLedgerLedgerConfigService service = EmcsAppContext.getContext().getBean(SubLedgerLedgerConfigService.class);
            service.save(dto, code, CommonUtils.setIdentityHeader());
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
