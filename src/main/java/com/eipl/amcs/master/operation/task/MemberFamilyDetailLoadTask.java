package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberFamilyDetail;
import com.eipl.amcs.master.operation.repository.MemberFamilyDetailRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberFamilyDetailLoadTask extends Task<List<MemberFamilyDetail>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberFamilyDetailLoadTask.class);

    private final String code;

    public MemberFamilyDetailLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<MemberFamilyDetail> call() throws Exception {
        try {
            MemberFamilyDetailRepository repository = EmcsAppContext.getContext().getBean(MemberFamilyDetailRepository.class);
            List<MemberFamilyDetail> list = repository.findByMember_Code(code);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MemberFamilyDetail fetch", e);
        }
        return null;
    }
}
