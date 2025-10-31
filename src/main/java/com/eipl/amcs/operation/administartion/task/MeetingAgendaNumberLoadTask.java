package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.task.ProductLoadTask;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeetingAgendaNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLoadTask.class);
    private final String society;

    public MeetingAgendaNumberLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("MeetingAgenda", "code", society, 2);
            if (code == null || code.isBlank())
                return null;
            return code;
        } catch (Exception e) {
            LOGGER.error("product Number fetch", e);
        }
        return null;
    }
}
