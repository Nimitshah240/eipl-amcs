package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.account.service.StaffMemberService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StaffMembersLoadTask extends Task<List<StaffMember>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffMembersLoadTask.class);

    @Override
    protected List<StaffMember> call() throws Exception {
        try {
            StaffMemberService service = EmcsAppContext.getContext().getBean(StaffMemberService.class);
            List<StaffMember> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("CommitteeMembers fetch", e);
        }
        return null;
    }
}
