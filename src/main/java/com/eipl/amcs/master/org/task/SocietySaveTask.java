package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.service.SocietyService;
import javafx.concurrent.Task;

public class SocietySaveTask extends Task<Object> {

    private final Society society;

    public SocietySaveTask(Society society) {
        this.society = society;
    }

    @Override
    protected Object call() throws Exception {
        try{
            SocietyService service = EmcsAppContext.getContext().getBean(SocietyService.class);
            if (society == null)
                return null;

            return service.save(society);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
