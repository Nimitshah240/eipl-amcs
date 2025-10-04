package com.eipl.amcs.operation.share.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.share.model.ShareDividend;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareDividendListSaveTask extends Task<String> {
    private List<ShareDividend> shareList;
    private short update;

    public ShareDividendListSaveTask(List<ShareDividend> shareList, short update) {
        this.shareList = shareList;
        this.update = update;
    }


    @Override
    protected String call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SHARE_DIVIDEND;

            ResponseEntity<String> response = null;
            if (this.update == 0) {
                response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(shareList), String.class);
                if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                    return null;
                return response.getBody().toString();
            } else {
                url = url + "/{code}";
                Map<String, String> uriVariables = new HashMap<>();
                uriVariables.put("code", shareList.get(0).getCode());
                response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(shareList.get(0)), String.class);
                if (response == null || response.getStatusCode() != HttpStatus.OK)
                    return null;
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
