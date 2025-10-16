package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MilkReceiptLoadTask extends Task<List<MilkReceipt>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MilkReceiptLoadTask.class);

    @Override
    protected List<MilkReceipt> call() throws Exception {
        try {
            MilkReceiptService service=EmcsAppContext.getContext().getBean(MilkReceiptService.class);
            List<MilkReceipt> list = service.findAll();

            if (list==null||list.isEmpty())return  null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_RECEIPT;
//            ResponseEntity<MilkReceipt[]> response = restTemplate.getForEntity(url,MilkReceipt[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("MilkDispatches fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("MilkDispatches fetch", e);
        }
        return null;
    }
}