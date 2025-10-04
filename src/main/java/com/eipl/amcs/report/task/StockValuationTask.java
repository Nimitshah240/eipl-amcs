package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.dto.SocietyPurchase;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class StockValuationTask extends Task<List<ProductStockValuation>> {
    private String societyCode;
    private LocalDate asOnDate;
    private String locale;


    public StockValuationTask(String societyCode, LocalDate asOnDate, String locale) {
        this.societyCode = societyCode;
        this.asOnDate = asOnDate;
        this.locale = locale;

    }

    public StockValuationTask() {

    }

    @Override
    protected List<ProductStockValuation> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STOCK_VALUATION;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", asOnDate.toString())
                    .queryParam("locale", locale);


            ResponseEntity<ProductStockValuation[]> response = restTemplate.getForEntity(builder.toUriString(), ProductStockValuation[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
