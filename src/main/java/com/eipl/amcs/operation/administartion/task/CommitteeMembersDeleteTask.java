package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.CommitteeMembersService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class CommitteeMembersDeleteTask extends Task<Boolean> {
    private final String code;

    public CommitteeMembersDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            CommitteeMembersService service = EmcsAppContext.getContext().getBean(CommitteeMembersService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
