package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.repository.MemberRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MappedFarmerLoadTask extends Task<List<Member>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MappedFarmerLoadTask.class);

    private final String code;

    public MappedFarmerLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<Member> call() throws Exception {
        try {
            MemberRepository service = EmcsAppContext.getContext().getBean(MemberRepository.class);
            return service.findByxCol1(code);
        } catch (Exception e) {
            LOGGER.error("MemberById fetch", e);
        }
        return null;
    }
}
