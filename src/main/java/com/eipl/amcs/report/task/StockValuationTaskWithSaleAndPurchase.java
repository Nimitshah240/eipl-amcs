package com.eipl.amcs.report.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.report.dto.ProductStockValuation;
import com.eipl.amcs.report.dto.ProductStockValuationWithSaleAndPurchase;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StockValuationTaskWithSaleAndPurchase extends Task<List<ProductStockValuationWithSaleAndPurchase>> {
    private String societyCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String locale;
    private String productCode;


    public StockValuationTaskWithSaleAndPurchase(String societyCode, LocalDate fromDate, LocalDate toDate, String locale, String productCode) {
        this.societyCode = societyCode;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.locale = locale;
        this.productCode = productCode;

    }

    public StockValuationTaskWithSaleAndPurchase() {

    }

    @Override
    protected List<ProductStockValuationWithSaleAndPurchase> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STOCK_VALUATION_WITH_SALE_PURCHASE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("societyCode", societyCode)
                    .queryParam("fromDate", fromDate.toString())
                    .queryParam("toDate", toDate.toString())
                    .queryParam("locale", locale)
                    .queryParam("productCode",productCode);


            ResponseEntity<ProductStockValuationWithSaleAndPurchase[]> response = restTemplate.getForEntity(builder.toUriString(), ProductStockValuationWithSaleAndPurchase[].class);
            if (response.getStatusCode() != HttpStatus.OK)
                return null;
            return Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
