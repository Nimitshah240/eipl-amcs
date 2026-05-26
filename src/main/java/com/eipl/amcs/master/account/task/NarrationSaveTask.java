package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Narration;
import com.eipl.amcs.master.account.repository.NarrationRepository;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class NarrationSaveTask extends Task<Object> {

    private final Narration dto;
    private final short update;

    public NarrationSaveTask(Narration dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            NarrationRepository service = EmcsAppContext.getContext().getBean(NarrationRepository.class);
            if (update == 0) {
                service.save(dto);
            } else {
                service.save(dto);
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
