package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceiptTransaction;
import com.eipl.amcs.operation.procurement.service.MilkReceiptService;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MilkReceiptTransactionLoadTask extends Task<List<MilkReceiptTransaction>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkReceiptTransactionLoadTask.class);

    private final String challanNo;

    public MilkReceiptTransactionLoadTask(String challanNo) {
        this.challanNo = challanNo;
    }

    @Override
    protected List<MilkReceiptTransaction> call() throws Exception {
        try {
            MilkReceiptService service=EmcsAppContext.getContext().getBean(MilkReceiptService.class);
            List<MilkReceiptTransaction>list=(service.findDetailByChallanNo(challanNo));
            if (list==null||list.isEmpty())return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_RECEIPT + "/transaction";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("challanNo",challanNo);
//            ResponseEntity<MilkReceiptTransaction[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkReceiptTransaction[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Milk dispatch transaction fetched: {}", response.getBody());
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Milk dispatch transaction fetch", e);
        }
        return null;
    }
}
