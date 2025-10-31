package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class MomActionDeleteTask extends Task<Boolean> {
    private final String code;

    public MomActionDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MeetingAgendaService service = EmcsAppContext.getContext().getBean(MeetingAgendaService.class);
            service.deleteMomAction(code, CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
