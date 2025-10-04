package com.eipl.amcs.master.org.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.org.model.Bank;
import com.eipl.amcs.master.org.model.Branch;
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

public class BranchLoadTask extends Task<List<Branch>> {
    private Bank bank;
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchLoadTask.class);

    public BranchLoadTask() {

    }

    public BranchLoadTask(Bank bank) {
        this.bank = bank;
    }

    @Override
    protected List<Branch> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.BRANCH;
            ResponseEntity<Branch[]> response;
            if (bank != null) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                        .queryParam("bankCode", bank.getCode());
                response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, Branch[].class);

            } else {
                response = restTemplate.getForEntity(url, Branch[].class);
            }
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Branch fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Branch fetch", e);
        }
        return null;
    }
}
