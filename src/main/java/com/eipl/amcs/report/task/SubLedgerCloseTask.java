package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.LedgerClose;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SubLedgerCloseTask extends Task<List<LedgerClose>> {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;
    private String ledgerCode;


    public SubLedgerCloseTask(String ledgerCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.ledgerCode = ledgerCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public SubLedgerCloseTask() {

    }

    @Override
    protected List<LedgerClose> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.FY_SUB_LEDGER_OPENING_BALANCE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("ledgerCode", ledgerCode)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString())
                    .queryParam("locale", MainApp.locale);
            ResponseEntity<LedgerClose[]> response = restTemplate.getForEntity(builder.toUriString(), LedgerClose[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
