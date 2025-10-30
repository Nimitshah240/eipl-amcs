package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.service.ShareDividendService;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class ShareDividendAllDeleteTask extends Task<Boolean> {
    private final LocalDate fromDt;
    private final LocalDate toDt;

    public ShareDividendAllDeleteTask(LocalDate fromDt, LocalDate toDt) {
        this.fromDt = fromDt;
        this.toDt = toDt;
    }


    @Override
    protected Boolean call() throws Exception {
        try {
            ShareDividendService service = EmcsAppContext.getContext().getBean(ShareDividendService.class);
            service.deleteAll(fromDt, toDt);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}