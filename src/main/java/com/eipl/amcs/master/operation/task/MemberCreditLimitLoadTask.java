package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.operation.dto.MemberCreditLimit;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public class MemberCreditLimitLoadTask extends Task<MemberCreditLimit> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberCreditLimitLoadTask.class);

    private final String code;
    private final Short type;

    public MemberCreditLimitLoadTask(String code,Short type) {
        this.code = code;
        this.type = type;
    }

    @Override
    protected MemberCreditLimit call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + "membercreditlimit"+"/codeAndType";
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                    .queryParam("code", code)
                    .queryParam("type", type);
            ResponseEntity<MemberCreditLimit> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, MemberCreditLimit.class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("MemberCreditLimit fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("MemberCreditLimit fetch", e);
        }
        return null;
    }
}
