package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class FinancialYearsCodeTask extends Task<Boolean> {

    private String code;

    public FinancialYearsCodeTask(String code) {
        this.code = code;
    }

    public FinancialYearsCodeTask() {
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);

            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.FY_FETCH_BY_CODE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code);
            ResponseEntity<Boolean> response = restTemplate.getForEntity(builder.toUriString(), Boolean.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return  response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
