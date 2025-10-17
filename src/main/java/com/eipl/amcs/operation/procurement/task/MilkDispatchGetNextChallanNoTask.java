package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.service.NextCodeService;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.inventory.task.ProductReceiptGetNextCodeTask;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class MilkDispatchGetNextChallanNoTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReceiptGetNextCodeTask.class);

    public MilkDispatchGetNextChallanNoTask(){

    }

    @Override
    protected String call() throws Exception {
        try {

            NextCodeService nextCodeService=EmcsAppContext.getContext().getBean(NextCodeService.class);;
//            TODO - what is nextCode?
            String code = nextCodeService.getNextCode("MilkDispatch", "challanNo", MainApp.identityDto.getSociety().getCode(), 3);
            if(code==null || code.isEmpty())
                return null;
            return code;

//            String code = MainApp.identityDto.getSociety().getCode()+"/"+MainApp.getFinancialYear().getCode()+"/";
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_DISPATCH + "/next-code";
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam(MainApp.identityDto.getSociety().getCode())
//                    .queryParam("nextCode",code);
//            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("MilkDispatch fetched: {}", response.getBody());
//            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("MilkDispatch fetch", e);
        }
        return null;
    }
}
