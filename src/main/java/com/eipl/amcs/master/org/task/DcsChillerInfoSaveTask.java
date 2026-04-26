package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.DcsChillerInfo;
import com.eipl.amcs.master.org.repository.DcsChillerInfoRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DcsChillerInfoSaveTask extends Task<List<DcsChillerInfo>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DcsChillerInfoSaveTask.class);

    private List<DcsChillerInfo> dcsChillerInfoList;

    public DcsChillerInfoSaveTask(List<DcsChillerInfo> dcsChillerInfoList) {
        this.dcsChillerInfoList = dcsChillerInfoList;
    }

    @Override
    protected List<DcsChillerInfo> call() throws Exception {
        try {
            DcsChillerInfoRepository service = EmcsAppContext.getContext().getBean(DcsChillerInfoRepository.class);
            return service.saveAll(dcsChillerInfoList);
        } catch (Exception e) {
            LOGGER.error("BmcChillerInfo fetch", e);
        }
        return null;
    }
}
