package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.StaffMemberService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class StaffMembersDeleteTask extends Task<Boolean> {
    private final String code;

    public StaffMembersDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            StaffMemberService service = EmcsAppContext.getContext().getBean(StaffMemberService.class);
            service.delete(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
