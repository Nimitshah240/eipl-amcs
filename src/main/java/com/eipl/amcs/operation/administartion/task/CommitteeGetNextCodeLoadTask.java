package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.CommitteeService;
import com.eipl.amcs.master.org.model.Society;
import javafx.concurrent.Task;

public class CommitteeGetNextCodeLoadTask extends Task<String> {

    private Society society;

    public CommitteeGetNextCodeLoadTask(Society society) {
        this.society = society;
    }

    @Override
    protected String call() throws Exception {
        CommitteeService service = EmcsAppContext.getContext().getBean(CommitteeService.class);
        return service.nextCode(society.getCode());
    }
}