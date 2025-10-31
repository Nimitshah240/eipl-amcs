package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.operation.share.service.ShareDividendService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class ShareDividendLoadTask extends Task<List<ShareDividend>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShareDividendLoadTask.class);

    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ShareDividendLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<ShareDividend> call() throws Exception {
        try {

            ShareDividendService service = EmcsAppContext.getContext().getBean(ShareDividendService.class);
            List<ShareDividend> list = service.findAllData(fromDate, toDate);
            if (list == null || list.isEmpty()) return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Cancelled Share fetch", e);
        }
        return null;
    }
}
