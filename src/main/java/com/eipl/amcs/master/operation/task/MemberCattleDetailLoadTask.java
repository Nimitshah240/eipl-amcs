package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberCattleDetail;
import com.eipl.amcs.master.operation.repository.MemberCattleDetailRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberCattleDetailLoadTask extends Task<List<MemberCattleDetail>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberCattleDetailLoadTask.class);

    private final String code;

    public MemberCattleDetailLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<MemberCattleDetail> call() throws Exception {
        try {
            MemberCattleDetailRepository repository = EmcsAppContext.getContext().getBean(MemberCattleDetailRepository.class);
            List<MemberCattleDetail> list = repository.findByMember_Code(code);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MemberCattleDetail fetch", e);
        }
        return null;
    }
}
