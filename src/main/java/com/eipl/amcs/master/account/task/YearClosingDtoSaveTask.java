package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.dto.YearClosingDto;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class YearClosingDtoSaveTask extends Task<Object> {

    private final YearClosingDto dto;


    public YearClosingDtoSaveTask(YearClosingDto dto) {
        this.dto = dto;

    }


    @Override
    protected YearClosingDto call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.FINANCIAL_YEAR;

            ResponseEntity<YearClosingDto> response =
                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), YearClosingDto.class);

            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
                return null;
            return  response.getBody() != null?response.getBody():null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
