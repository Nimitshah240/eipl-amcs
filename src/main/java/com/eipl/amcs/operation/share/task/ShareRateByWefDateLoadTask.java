package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.ShareRate;
import com.eipl.amcs.operation.share.service.ShareRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class ShareRateByWefDateLoadTask extends Task<ShareRate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShareRateByWefDateLoadTask.class);

    private final LocalDate date;

    public ShareRateByWefDateLoadTask(LocalDate date) {
        this.date = date;
    }

    @Override
    protected ShareRate call() throws Exception {
        try {
            ShareRateService service = EmcsAppContext.getContext().getBean(ShareRateService.class);
            return service.fetchRate(date);
        } catch (Exception e) {
            LOGGER.error("ShareRate fetch", e);
        }
        return null;
    }
}
