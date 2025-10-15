package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class LocalMilkSaleRateTask extends Task<LocalMilkSaleRate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleRateTask.class);

    private final LocalDate date;
    private final Integer milkType, milkClass;

    public LocalMilkSaleRateTask(LocalDate date, Integer milkType, Integer milkClass) {
        this.date = date;
        this.milkType = milkType;
        this.milkClass = milkClass;
    }

    @Override
    protected LocalMilkSaleRate call() throws Exception {
        try {
            LocalMilkSaleRateService service = EmcsAppContext.getContext().getBean(LocalMilkSaleRateService.class);
            return service.fetchRate(date, milkType, milkClass);
        } catch (Exception e) {
            LOGGER.error("LocalMilkSaleRate fetch", e);
        }
        return null;
    }
}
