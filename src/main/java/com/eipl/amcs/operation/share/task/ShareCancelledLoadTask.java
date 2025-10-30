package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.Share;
import com.eipl.amcs.operation.share.service.ShareService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ShareCancelledLoadTask extends Task<List<Share>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareCancelledLoadTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ShareCancelledLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;

    }

    @Override
    protected List<Share> call() throws Exception {
        try {
            ShareService service = EmcsAppContext.getContext().getBean(ShareService.class);
            LocalDate fromDt = fromDate;
            LocalDate toDt = toDate;
            if (fromDate == null || toDate == null) {
                return service.findAll();
            }
            return service.findAllData(fromDt, toDt);
        } catch (Exception e) {
            LOGGER.error("Cancelled Share fetch", e);
        }
        return null;
    }
}
