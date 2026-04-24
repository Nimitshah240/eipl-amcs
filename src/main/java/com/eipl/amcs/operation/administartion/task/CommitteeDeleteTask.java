package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.CommitteeService;
import javafx.concurrent.Task;

public class CommitteeDeleteTask extends Task<Boolean> {
    private final String code;

    public CommitteeDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            CommitteeService service = EmcsAppContext.getContext().getBean(CommitteeService.class);
            service.deleteById(code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
