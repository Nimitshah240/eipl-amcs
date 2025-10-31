package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.BonusSummary;
import com.eipl.amcs.operation.billing.service.BonusService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BonusSummaryLoadTask extends Task<List<BonusSummary>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusSummaryLoadTask.class);

    @Override
    protected List<BonusSummary> call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            List<BonusSummary> summaryList = service.findBonusSummaryBetWeen();
            if (summaryList == null || summaryList.isEmpty()) return null;
            return summaryList;
        } catch (Exception e) {
            LOGGER.error("Memberbill summary fetch", e);
        }
        return null;
    }
}
