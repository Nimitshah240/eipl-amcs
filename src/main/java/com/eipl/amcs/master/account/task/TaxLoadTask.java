package com.eipl.amcs.master.account.task;

import com.eipl.amcs.master.account.dto.TaxDto;
import com.eipl.amcs.master.account.service.TaxService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TaxLoadTask extends Task<List<TaxDto>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaxLoadTask.class);
    private TaxService service;

    @Override
    protected List<TaxDto> call() throws Exception {
        try {
            List<TaxDto> list = service.findAll();
            if (list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Tax fetch", e);
        }
        return null;
    }
}

