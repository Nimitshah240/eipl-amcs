package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.CasteCategory;
import com.eipl.amcs.master.operation.repository.CasteCategoryRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CasteCategoryLoadTask extends Task<List<CasteCategory>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasteCategoryLoadTask.class);


    public CasteCategoryLoadTask() {
    }

    @Override
    protected List<CasteCategory> call() throws Exception {
        try {
            CasteCategoryRepository service = EmcsAppContext.getContext().getBean(CasteCategoryRepository.class);
            List<CasteCategory> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("CasteCategorys fetch", e);
        }
        return null;
    }
}
