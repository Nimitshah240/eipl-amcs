package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.service.CustomerService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerByIdLoadTask extends Task<Customer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerByIdLoadTask.class);

    private final String code;

    public CustomerByIdLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected Customer call() throws Exception {
        try {
            CustomerService service = EmcsAppContext.getContext().getBean(CustomerService.class);
            return service.findByCustomerCode(code);
        } catch (Exception e) {
            LOGGER.error("MemberById fetch", e);
        }
        return null;
    }
}
