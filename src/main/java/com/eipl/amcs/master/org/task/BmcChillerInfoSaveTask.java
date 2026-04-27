package com.eipl.amcs.master.org.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.BmcChillerInfo;
import com.eipl.amcs.master.org.repository.BmcChillerInfoRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BmcChillerInfoSaveTask extends Task<List<BmcChillerInfo>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BmcChillerInfoSaveTask.class);

    private List<BmcChillerInfo> bmcChillerInfoList;

    public BmcChillerInfoSaveTask(List<BmcChillerInfo> bmcChillerInfoList) {
        this.bmcChillerInfoList = bmcChillerInfoList;
    }

    @Override
    protected List<BmcChillerInfo> call() throws Exception {
        try {
            BmcChillerInfoRepository service = EmcsAppContext.getContext().getBean(BmcChillerInfoRepository.class);
            return service.saveAll(bmcChillerInfoList);
        } catch (Exception e) {
            LOGGER.error("BmcChillerInfo fetch", e);
        }
        return null;
    }
}
