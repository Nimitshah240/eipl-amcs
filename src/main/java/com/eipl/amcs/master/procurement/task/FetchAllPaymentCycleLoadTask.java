package com.eipl.amcs.master.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.task.ProductPurchaseRateByProductTask;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.eipl.amcs.master.procurement.service.SocietyPaymentCycleService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class FetchAllPaymentCycleLoadTask extends Task<List<SocietyPaymentCycle>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductPurchaseRateByProductTask.class);

    private final LocalDate date;
    private final Integer limit;

    public FetchAllPaymentCycleLoadTask(LocalDate date, Integer limit) {
        this.date = date;
        this.limit = limit;
    }

    @Override
    protected List<SocietyPaymentCycle> call() throws Exception {
        try {
            SocietyPaymentCycleService service = EmcsAppContext.getContext().getBean(SocietyPaymentCycleService.class);
            LocalDateTime dt = LocalDateTime.of(date, LocalTime.of(12, 0));
            List<SocietyPaymentCycle> list = service.findByToDateGreaterThanEqualOrderByToDate(dt, limit);
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Payment Cycle List Fetch ", e);
        }
        return null;
    }
}
