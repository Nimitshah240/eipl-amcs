package com.eipl.amcs.base.model;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
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

public class NotificationSaveTask extends Task<List<Notification>> {

    private final List<Notification> dto;

    public NotificationSaveTask(List<Notification> dto) {
        this.dto = dto;
    }


    @Override
    protected List<Notification> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.NOTIFICATION;

            ResponseEntity<Notification[]> response =  restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), Notification[].class);

            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
