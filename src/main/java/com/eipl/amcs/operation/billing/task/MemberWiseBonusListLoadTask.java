package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.service.BonusService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class MemberWiseBonusListLoadTask extends Task<List<Map<String, Object>>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberWiseBonusListLoadTask.class);
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public MemberWiseBonusListLoadTask(LocalDate fromDate, LocalDate toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    protected List<Map<String, Object>> call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            List<Map<String, Object>> loadDataBonusSummaryResult = service.loadDataBonusSummary(fromDate, toDate);

            if (loadDataBonusSummaryResult == null || loadDataBonusSummaryResult.isEmpty()) return null;
            return loadDataBonusSummaryResult;
        } catch (Exception e) {
            LOGGER.error("Bonus fetch", e);
        }
        return null;
    }
}
