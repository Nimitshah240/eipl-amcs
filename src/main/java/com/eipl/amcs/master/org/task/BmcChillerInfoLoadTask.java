package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.BmcChillerInfo;
import com.eipl.amcs.master.org.repository.BmcChillerInfoRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BmcChillerInfoLoadTask extends Task<List<BmcChillerInfo>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmcChillerInfoLoadTask.class);


    public BmcChillerInfoLoadTask() {
    }

    @Override
    protected List<BmcChillerInfo> call() throws Exception {
        try {
            BmcChillerInfoRepository service = EmcsAppContext.getContext().getBean(BmcChillerInfoRepository.class);
            List<BmcChillerInfo> list = service.findAll();
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("BmcChillerInfo fetch", e);
        }
        return null;
    }
}
