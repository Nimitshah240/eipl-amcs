package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.LedgerBalance;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SubLedgerOpeningTask extends Task<List<Object[]>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;


    public SubLedgerOpeningTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public SubLedgerOpeningTask() {

    }

    @Override
    protected List<Object[]> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.SUB_LEDGER_BALANCES;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString()).queryParam("locale", locale);


            ResponseEntity<Object[][]> response = restTemplate.getForEntity(builder.toUriString(), Object[][].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return  Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
