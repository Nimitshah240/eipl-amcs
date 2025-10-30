package com.eipl.amcs.master.insurance.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsuranceDetailCodeTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceDetailCodeTask.class);

    public InsuranceDetailCodeTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getIdentity().getSocietyRefCode();
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String codes = nextCodeService.getNextCode("InsuranceDetail", "insuranceDetailCode", "VLC" + "-" + code + "-", 1);
            if (codes == null || codes.isEmpty())
                return null;
            return codes;
        } catch (Exception e) {
            LOGGER.error("Insurance Detail fetched: {}", e);
        }
        return null;
    }

}
