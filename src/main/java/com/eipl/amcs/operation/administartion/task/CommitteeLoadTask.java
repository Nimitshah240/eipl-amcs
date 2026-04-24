package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Committee;
import com.eipl.amcs.master.account.repository.CommitteeRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class CommitteeLoadTask extends Task<List<Committee>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommitteeLoadTask.class);

    @Override
    protected List<Committee> call() throws Exception {
        try {
            CommitteeRepository service = EmcsAppContext.getContext().getBean(CommitteeRepository.class);
            List<Committee> list = service.findAll(Sort.by("formationDate").descending());

            if (list == null || list.isEmpty())
                return new ArrayList<>();
            return list;
        } catch (Exception e) {
            LOGGER.error("Committee fetch", e);
        }
        return null;
    }
}
