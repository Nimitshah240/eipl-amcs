package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.inventory.model.Product;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ShareDividendAllDeleteTask extends Task<Boolean> {
    private LocalDate fromDt, toDt;
    public ShareDividendAllDeleteTask(LocalDate fromDt,LocalDate toDt) {
        this.fromDt=fromDt;
        this.toDt=toDt;
    }


    @Override
    protected Boolean call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE_DIVIDEND + "/all";

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("fromDate",fromDt.toString()).queryParam("toDate",toDt.toString());

            ResponseEntity<ShareDividend[]> response = restTemplate.getForEntity(builder.toUriString(), ShareDividend[].class);

//            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}