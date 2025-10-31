package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.DpuIncentiveRequest;
import com.eipl.amcs.operation.procurement.repository.DpuIncentiveRequestRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DpuIncentiveRequestLoadTask extends Task<DpuIncentiveRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DpuIncentiveRequestLoadTask.class);
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public DpuIncentiveRequestLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected DpuIncentiveRequest call() throws Exception {
        try {
            DpuIncentiveRequestRepository dpuIncentiveRequestRepository = EmcsAppContext.getContext().getBean(DpuIncentiveRequestRepository.class);

            if (dpuIncentiveRequestRepository.findTop1ByOrderByCreatedAtDesc().isPresent()) {
                return dpuIncentiveRequestRepository.findTop1ByOrderByCreatedAtDesc().get();
            } else
                return null;
        } catch (Exception e) {
            LOGGER.error("ManualRequest fetch", e);
        }
        return null;
    }
}
