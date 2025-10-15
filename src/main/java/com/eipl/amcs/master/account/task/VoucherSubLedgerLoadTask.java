package com.eipl.amcs.master.account.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.VoucherSubLedger;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class VoucherSubLedgerLoadTask extends Task<List<VoucherSubLedger>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoucherSubLedgerLoadTask.class);

    private final String code;

    public VoucherSubLedgerLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<VoucherSubLedger> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.VOUCHER + "/voucher-sub-ledger";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code);
            ResponseEntity<VoucherSubLedger[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, VoucherSubLedger[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("List<VoucherSubLedger> fetched: {}", response.getBody());
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("List<VoucherSubLedger> fetch", e);
        }
        return null;
    }
}
