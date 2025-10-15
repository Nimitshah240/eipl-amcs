package com.eipl.amcs.base.task;


import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.sync.model.Subscribed;
import com.eipl.amcs.sync.repository.SubscribedRepository;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;

public class SentboxSaveTask extends Task<Object> {
    private final List<Subscribed> dto;
    private final short update;

    public SentboxSaveTask(List<Subscribed> dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            SubscribedRepository subscribedRepository = EmcsAppContext.getContext().getBean(SubscribedRepository.class);
            if (this.update == 0) {
                for (Subscribed subscribed : dto) {
                    subscribedRepository.save(subscribed);
                }
            }
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
