package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.service.BonusService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;

public class MemberWiseBonusLoadTask extends Task<Map<String, Object>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberWiseBonusLoadTask.class);
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final String memberCode;

    public MemberWiseBonusLoadTask(LocalDate fromDate, LocalDate toDate, String memberCode) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.memberCode = memberCode;
    }

    @Override
    protected Map<String, Object> call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            Map<String, Object> loadDataBonusResult = service.loadDataBonus(fromDate, toDate, memberCode);

            if (loadDataBonusResult == null || loadDataBonusResult.isEmpty()) return null;
            return loadDataBonusResult;
        } catch (Exception e) {
            LOGGER.error("Bonus fetch", e);
        }
        return null;
    }
}
