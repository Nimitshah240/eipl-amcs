package com.eipl.amcs.master.org.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SocietySaveTask extends Task<Object> {

    private final Society society;

    public SocietySaveTask(Society society) {
        this.society = society;
    }

    @Override
    protected Object call() throws Exception {
        RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
        String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY;
        ResponseEntity<Society> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(society), Society.class);
        if (response == null || response.getStatusCode() != HttpStatus.OK)
            return response.getBody();
        return null;
    }
}
