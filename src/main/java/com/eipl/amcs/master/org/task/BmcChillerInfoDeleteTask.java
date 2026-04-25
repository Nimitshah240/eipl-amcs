package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.repository.BmcChillerInfoRepository;
import javafx.concurrent.Task;

public class BmcChillerInfoDeleteTask extends Task<Boolean> {
    private final Integer code;

    public BmcChillerInfoDeleteTask(Integer code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            BmcChillerInfoRepository service = EmcsAppContext.getContext().getBean(BmcChillerInfoRepository.class);
            service.deleteById(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
