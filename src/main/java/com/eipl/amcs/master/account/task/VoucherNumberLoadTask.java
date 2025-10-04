package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.utils.AppConstant;

import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class VoucherNumberLoadTask extends Task<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherNumberLoadTask.class);

    public VoucherNumberLoadTask() {
    }

    @Override
    protected String call() throws Exception {
        try {
            String code = MainApp.identityDto.getSociety().getCode()+"/"+MainApp.getFinancialYear().getCode()+"/";
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.VOUCHER_NEXT_CODE;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParam("code",code);
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("ProductSale No fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("ProductSale No fetch", e);
        }
        return null;
    }

}
