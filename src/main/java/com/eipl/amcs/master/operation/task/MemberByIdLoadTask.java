package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.service.MemberService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberByIdLoadTask extends Task<Member> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberByIdLoadTask.class);

    private final String code;

    public MemberByIdLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected Member call() throws Exception {
        try {
            MemberService service = EmcsAppContext.getContext().getBean(MemberService.class);
            return service.findByMemberCode(code);
        } catch (Exception e) {
            LOGGER.error("MemberById fetch", e);
        }
        return null;
    }
}
