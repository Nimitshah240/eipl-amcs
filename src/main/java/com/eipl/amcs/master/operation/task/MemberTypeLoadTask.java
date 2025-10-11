package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.global.model.MemberType;
import com.eipl.amcs.master.global.service.MemberTypeService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberTypeLoadTask extends Task<List<MemberType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberTypeLoadTask.class);

    @Override
    protected List<MemberType> call() throws Exception {
        try {
            MemberTypeService service = EmcsAppContext.getContext().getBean(MemberTypeService.class);
            List<MemberType> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("MemberType fetch", e);
        }
        return null;
    }
}
