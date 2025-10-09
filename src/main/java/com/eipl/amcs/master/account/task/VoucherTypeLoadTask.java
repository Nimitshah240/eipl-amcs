package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.LedgerGroup;
import com.eipl.amcs.master.account.model.VoucherType;
import com.eipl.amcs.master.account.service.VoucherTypeService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class VoucherTypeLoadTask extends Task<List<VoucherType>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherTypeLoadTask.class);

    @Override
    protected List<VoucherType> call() throws Exception {
        try {




//            private VoucherTypeService service;
//            List<VoucherType> list = service.findAll();




            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.VOUCHER_TYPE;
            ResponseEntity<VoucherType[]> response = restTemplate.getForEntity(url, VoucherType[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("VoucherType fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("VoucherType fetch", e);
        }
        return null;
    }
}
