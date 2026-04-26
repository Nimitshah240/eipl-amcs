package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.DcsChillerInfo;
import com.eipl.amcs.master.org.repository.DcsChillerInfoRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DcsChillerInfoLoadTask extends Task<List<DcsChillerInfo>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DcsChillerInfoLoadTask.class);


    public DcsChillerInfoLoadTask() {
    }

    @Override
    protected List<DcsChillerInfo> call() throws Exception {
        try {
            DcsChillerInfoRepository service = EmcsAppContext.getContext().getBean(DcsChillerInfoRepository.class);
            List<DcsChillerInfo> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("BmcChillerInfo fetch", e);
        }
        return null;
    }
}
