package com.eipl.amcs.base.model;

import com.eipl.amcs.base.Identity;
import com.eipl.amcs.base.service.IdentityDetailsService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class IdentitySaveTask extends Task<Object> {

    private final Identity dto;

    public IdentitySaveTask(Identity dto) {
        this.dto = dto;
    }

    @Override
    protected Object call() throws Exception {
        try {
            IdentityDetailsService service = EmcsAppContext.getContext().getBean(IdentityDetailsService.class);
            service.save(dto, CommonUtil.setIdentityHeader());
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
