package com.eipl.amcs.master.org.task;

import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DockNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockLoadTask.class);
    private final String society;

    public DockNumberLoadTask(String society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        try {
            NextCodeService nextCodeService = EmcsAppContext.getContext().getBean(NextCodeService.class);
            String code = nextCodeService.getNextCode("Dock", "dockNo", society, 2);
            if (code == null || code.isEmpty())
                return null;
            return code;
        } catch (Exception e) {
            LOGGER.error("Dock Number fetch", e);
        }
        return null;
    }
}
