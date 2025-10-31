package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberEkyc;
import com.eipl.amcs.master.operation.repository.MemberEkycRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberEkycLoadTask extends Task<List<MemberEkyc>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberEkycLoadTask.class);

    public MemberEkycLoadTask() {
    }

    @Override
    protected List<MemberEkyc> call() throws Exception {
        try {
            MemberEkycRepository memberEkycRepository = EmcsAppContext.getContext().getBean(MemberEkycRepository.class);
            return memberEkycRepository.findAllWithMembers();
        } catch (Exception e) {
            LOGGER.error("Error fetching MemberEkyc list", e);
        }
        return null;
    }
}
