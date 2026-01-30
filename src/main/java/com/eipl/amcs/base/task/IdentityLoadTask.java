package com.eipl.amcs.base.task;

import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.base.service.IdentityDetailsService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IdentityLoadTask extends Task<Identity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityLoadTask.class);

    public IdentityLoadTask() {
    }

    @Override
    protected Identity call() throws Exception {
        try {
            IdentityDetailsService service = EmcsAppContext.getContext().getBean(IdentityDetailsService.class);
            List<Identity> identities = service.findAll();
            if (identities != null && !identities.isEmpty()) {
                LOGGER.info("Identity Fetched Successful");
                return identities.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.warn("Could not fetch identity");
        return null;
    }
}