package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.MomAction;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MomActionLoadTask extends Task<List<MomAction>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MomActionLoadTask.class);
    private final String code;

    public MomActionLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<MomAction> call() throws Exception {
        try {
            MeetingAgendaService service = EmcsAppContext.getContext().getBean(MeetingAgendaService.class);
            List<MomAction> list = service.findMomAction(code);

            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Mom fetch", e);
        }
        return null;
    }
}
