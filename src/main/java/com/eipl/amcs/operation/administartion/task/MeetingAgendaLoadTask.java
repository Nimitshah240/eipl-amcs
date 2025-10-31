package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.MeetingAgenda;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MeetingAgendaLoadTask extends Task<List<MeetingAgenda>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeetingAgendaLoadTask.class);

    @Override
    protected List<MeetingAgenda> call() throws Exception {
        try {
            MeetingAgendaService service = EmcsAppContext.getContext().getBean(MeetingAgendaService.class);
            List<MeetingAgenda> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Meeting fetch", e);
        }
        return null;
    }
}
