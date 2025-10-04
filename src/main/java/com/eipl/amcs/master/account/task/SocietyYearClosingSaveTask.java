package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.SocietyYearClosing;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class SocietyYearClosingSaveTask extends Task<SocietyYearClosing> {

    private final SocietyYearClosing dto;
    private final short update;

    public SocietyYearClosingSaveTask(SocietyYearClosing dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected SocietyYearClosing call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SOCIETY_YEAR_CLOSING;

            ResponseEntity<SocietyYearClosing> response = this.update == 0 ?
                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), SocietyYearClosing.class) :
                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), SocietyYearClosing.class);

            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                return null;
            return  response.getBody()  ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
