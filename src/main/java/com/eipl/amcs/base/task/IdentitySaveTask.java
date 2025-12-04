package com.eipl.amcs.base.task;

import com.eipl.amcs.base.model.Identity;
import com.eipl.amcs.base.service.IdentityDetailsService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;

public class IdentitySaveTask extends Task<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentitySaveTask.class);

    private final Identity dto;

    public IdentitySaveTask(Identity dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            IdentityDetailsService service = EmcsAppContext.getContext().getBean(IdentityDetailsService.class);
            service.save(dto, CommonUtils.setIdentityHeader());
            LOGGER.info("Identity Saved Successful");
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("Identity Saved Failed");
        return null;
    }
}
