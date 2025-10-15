package com.eipl.amcs.base.model;

import com.eipl.amcs.base.Identity;
import com.eipl.amcs.base.service.IdentityDetailsService;
import com.eipl.amcs.config.EmcsAppContext;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class IdentityLoadTask extends Task<List<Identity>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityLoadTask.class);

    @Override
    protected List<Identity> call() throws Exception {
        try {
            IdentityDetailsService service = EmcsAppContext.getContext().getBean(IdentityDetailsService.class);
            List<Identity> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Identity fetch", e);
        }
        return null;
    }
}
