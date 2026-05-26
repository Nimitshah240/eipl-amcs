package com.eipl.amcs.master.account.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.repository.NarrationRepository;
import javafx.concurrent.Task;

public class NarrationDeleteTask extends Task<Boolean> {
    private final String code;

    public NarrationDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            NarrationRepository service = EmcsAppContext.getContext().getBean(NarrationRepository.class);
            service.deleteById(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
