package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.procurement.model.LocalMilkSaleRate;
import com.eipl.amcs.master.procurement.service.LocalMilkSaleRateService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

public class LocalMilkSaleRateTask extends Task<LocalMilkSaleRate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalMilkSaleRateTask.class);

    private final LocalDate date;
    private final Integer milkType, milkClass;
    private LocalMilkSaleRateService service;

    public LocalMilkSaleRateTask(LocalDate date, Integer milkType, Integer milkClass) {
        this.date = date;
        this.milkType = milkType;
        this.milkClass = milkClass;
    }

    @Override
    protected LocalMilkSaleRate call() throws Exception {
        try {

            return service.fetchRate(date, milkType, milkClass);
        } catch (Exception e) {
            LOGGER.error("LocalMilkSaleRate fetch", e);
        }
        return null;
    }
}
