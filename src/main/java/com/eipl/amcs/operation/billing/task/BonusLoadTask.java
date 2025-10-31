package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.billing.model.Bonus;
import com.eipl.amcs.operation.billing.service.BonusService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class BonusLoadTask extends Task<List<Bonus>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusLoadTask.class);
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
    private final Integer milkType;

    public BonusLoadTask(LocalDateTime fromDate, LocalDateTime toDate, Integer milkType) {
        this.fromDate = fromDate;
        this.milkType = milkType;
        this.toDate = toDate;
    }

    @Override
    protected List<Bonus> call() throws Exception {
        try {
            BonusService service = EmcsAppContext.getContext().getBean(BonusService.class);
            List<Bonus> listBonus = service.loadData(fromDate, toDate, milkType);
            if (listBonus == null || listBonus.isEmpty()) return null;
            return listBonus;
        } catch (Exception e) {
            LOGGER.error("Bonus fetch", e);
        }
        return null;
    }
}
