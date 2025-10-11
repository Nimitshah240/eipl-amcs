package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.service.MemberService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberLoadTask extends Task<List<Member>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberLoadTask.class);

    @Override
    protected List<Member> call() throws Exception {
        try {
            MemberService service = EmcsAppContext.getContext().getBean(MemberService.class);
            List<Member> list = service.findAllBySociety(MainApp.identityDto.getSociety().getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Member fetch", e);
        }
        return null;
    }
}
