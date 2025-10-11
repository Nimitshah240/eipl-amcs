package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.service.CustomerService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CustomerLoadTask extends Task<List<Customer>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerLoadTask.class);
    private CustomerService service;

    @Override
    protected List<Customer> call() throws Exception {
        try {

            List<Customer> list = service.findAllBySociety(MainApp.identityDto.getSociety().getCode());
            if (list == null || list.isEmpty())
                return null;
            return list;
        } catch (Exception e) {
            LOGGER.error("Customer fetch", e);
        }
        return null;
    }
}