package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Relationship;
import com.eipl.amcs.master.operation.repository.RelationshipRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RelationLoadTask extends Task<List<Relationship>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationLoadTask.class);


    public RelationLoadTask() {
    }

    @Override
    protected List<Relationship> call() throws Exception {
        try {
            RelationshipRepository service = EmcsAppContext.getContext().getBean(RelationshipRepository.class);
            List<Relationship> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Relationships fetch", e);
        }
        return null;
    }
}
