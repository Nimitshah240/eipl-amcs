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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BalanceSheetTask extends Task<List<LedgerBalance>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;


    public BalanceSheetTask(String societyCode, LocalDate fromDate, LocalDate toDate, String locale) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
    }

    public BalanceSheetTask() {

    }

    @Override
    protected List<LedgerBalance> call() throws Exception {
        try {
            List<LedgerBalance> list = new ArrayList<>();

            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);

            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BALANCESHEET;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString())
                    .queryParam("locale", locale)
                    .queryParam("liabilityAsset", 0); // Expense

            ResponseEntity<LedgerBalance[]> response = restTemplate.getForEntity(builder.toUriString(), LedgerBalance[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            if (response.getBody() != null) {
                List<LedgerBalance> listExpense = Arrays.asList(response.getBody());
                listExpense.forEach(item -> item.setIncomeExpense(0));
                list.addAll(listExpense);
            }

            UriComponentsBuilder builder2 = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString())
                    .queryParam("locale", locale)
                    .queryParam("liabilityAsset", 1); // Income
            ResponseEntity<LedgerBalance[]> response2 = restTemplate.getForEntity(builder2.toUriString(), LedgerBalance[].class);
            if (response2 == null || response2.getStatusCode() != HttpStatus.OK ||response2.getBody()==null)
                return null;
            List<LedgerBalance> listIncome = Arrays.asList(response2.getBody());
            listIncome.forEach(item -> item.setIncomeExpense(1));
            list.addAll(listIncome);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
