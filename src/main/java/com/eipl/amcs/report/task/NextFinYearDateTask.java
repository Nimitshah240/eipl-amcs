package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.FinancialYear;
import com.eipl.amcs.report.dto.MilkDispatchChallan;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class NextFinYearDateTask extends Task<List<FinancialYear>> {

    private LocalDate currentDate;

    public NextFinYearDateTask(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public NextFinYearDateTask() {
    }

    @Override
    protected List<FinancialYear> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);

            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.NEXT_FY_DATE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("date", currentDate.toString());
            ResponseEntity<FinancialYear[]> response = restTemplate.getForEntity(builder.toUriString(), FinancialYear[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
