package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.repository.DcsChillerInfoRepository;
import javafx.concurrent.Task;

public class DcsChillerInfoDeleteTask extends Task<Boolean> {
    private final Integer code;

    public DcsChillerInfoDeleteTask(Integer code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            DcsChillerInfoRepository service = EmcsAppContext.getContext().getBean(DcsChillerInfoRepository.class);
            service.deleteById(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
