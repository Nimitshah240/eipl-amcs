package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.org.service.UnionService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UnionLoadTask extends Task<List<Union>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnionLoadTask.class);

    @Override
    protected List<Union> call() throws Exception {
        try {
            UnionService service = EmcsAppContext.getContext().getBean(UnionService.class);
            List<Union> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Union fetch", e);
        }
        return null;
    }
}
