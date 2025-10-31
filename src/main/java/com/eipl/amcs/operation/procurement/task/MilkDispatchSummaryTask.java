package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchSummaryDto;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.List;

public class MilkDispatchSummaryTask extends Task<List<MilkDispatchSummaryDto>> {
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    public MilkDispatchSummaryTask(LocalDateTime fromDate, LocalDateTime toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<MilkDispatchSummaryDto> call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            List<MilkDispatchSummaryDto> milkDispatchSummaryDtos = service.fetchMilkDispatchSummary(fromDate, toDate);
            if (milkDispatchSummaryDtos == null || milkDispatchSummaryDtos.isEmpty())
                return null;
            return milkDispatchSummaryDtos;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
