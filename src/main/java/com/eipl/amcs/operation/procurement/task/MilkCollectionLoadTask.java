package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.List;

public class MilkCollectionLoadTask extends Task<List<MilkCollection>> {
    private final LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String code;
    private String dockNo;
    private int sync;

    public MilkCollectionLoadTask(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public MilkCollectionLoadTask(LocalDateTime fromDate, LocalDateTime toDate, int sync) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sync = sync;
    }

    public MilkCollectionLoadTask(LocalDateTime fromDate, LocalDateTime toDate, String dockNo, int sync) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.dockNo = dockNo;
        this.sync = sync;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        try {

            MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
            if (sync == 1) {
                service.findAllCollectionByDate(fromDate, toDate, CommonUtils.setIdentityHeader());
                return null;
            }

            if (dockNo != null)
                return service.findAllCollectionByDockNo(fromDate, toDate, dockNo);

            if (toDate == null)
                return service.findAllCollection(fromDate);

            return service.findAllBetween(fromDate, toDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
