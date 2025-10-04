package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkReceipt;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class MilkReceiptDeleteTask extends Task<Boolean> {

    private final MilkReceipt receipt;


    public MilkReceiptDeleteTask(MilkReceipt receipt) {
        this.receipt = receipt;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MILK_RECEIPT + "/delete";
//            Map<String, Object> uriVariables = new HashMap<>();
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("code",receipt.getCode());

            ResponseEntity<Void> response = restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, null, Void.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
