package com.eipl.amcs.operation.billing.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.billing.model.MemberBillTransaction;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class MemberBillTransactionSaveTask extends Task<List<MemberBillTransaction>> {

    private final List<MemberBillTransaction> dto;

    public MemberBillTransactionSaveTask(List<MemberBillTransaction> dto) {
        this.dto = dto;
    }


    @Override
    protected List<MemberBillTransaction> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_BILLING+"/savetrans";
            ResponseEntity<MemberBillTransaction[]> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), MemberBillTransaction[].class);
            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
