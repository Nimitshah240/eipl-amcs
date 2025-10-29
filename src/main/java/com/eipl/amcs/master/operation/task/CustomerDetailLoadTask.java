package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Customer;
import com.eipl.amcs.master.operation.model.CustomerDetails;
import com.eipl.amcs.master.operation.service.CustomerService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerDetailLoadTask extends Task<CustomerDetails> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDetailLoadTask.class);

    private final String code;

    public CustomerDetailLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected CustomerDetails call() throws Exception {
        try {
            CustomerService service = EmcsAppContext.getContext().getBean(CustomerService.class);
            Customer customer = service.findByCustomerCode(code);
            return service.findDetailByCustomer(customer);

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.CUSTOMER_DETAIL + "/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);
//            ResponseEntity<CustomerDetails> response = restTemplate.exchange(url, HttpMethod.GET, null, CustomerDetails.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Customer fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Customer fetch", e);
        }
        return null;
    }
}