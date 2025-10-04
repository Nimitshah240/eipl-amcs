package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.dto.MemberDetail;
import com.eipl.amcs.operation.procurement.model.MilkDispatchTransaction;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MilkDispatchTransactionLoadTask extends Task<List<MilkDispatchTransaction>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MilkDispatchTransactionLoadTask.class);

    private final String challanNo;

    public MilkDispatchTransactionLoadTask(String challanNo) {
        this.challanNo = challanNo;
    }

    @Override
    protected List<MilkDispatchTransaction> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_DISPATCH + "/transaction";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("challanNo",challanNo);
            ResponseEntity<MilkDispatchTransaction[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MilkDispatchTransaction[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Milk dispatch transaction fetched: {}", response.getBody());
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Milk dispatch transaction fetch", e);
        }
        return null;
    }
}
