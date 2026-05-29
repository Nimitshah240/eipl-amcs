package com.eipl.amcs.setting.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkCollection;
import com.eipl.amcs.operation.procurement.service.MilkCollectionService;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.List;

public class MilkCollectionLoadTaskByAnalyzeAndShift extends Task<List<MilkCollection>> {
    private final String analyserCode;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
    private final String dockCode;

    public MilkCollectionLoadTaskByAnalyzeAndShift(String analyserCode, LocalDateTime fromDate, LocalDateTime toDate, String dockCode) {
        this.analyserCode = analyserCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.dockCode = dockCode;
    }

    @Override
    protected List<MilkCollection> call() throws Exception {
        MilkCollectionService service = EmcsAppContext.getContext().getBean(MilkCollectionService.class);
        return service.findByAnalyserCodeAndCollectionDateBetweenAndDockCode(analyserCode, fromDate, toDate, dockCode);
    }
}
