package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.master.operation.service.MemberService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AllMemberDetailsLoadTask extends Task<List<MemberDetail>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllMemberDetailsLoadTask.class);


    public AllMemberDetailsLoadTask() {
    }

    private MemberService service;

    @Override
    protected List<MemberDetail> call() throws Exception {
        try {
            List<MemberDetail> list = service.findAllMemberDetails();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MemberDetails fetch", e);
        }
        return null;
    }
}
