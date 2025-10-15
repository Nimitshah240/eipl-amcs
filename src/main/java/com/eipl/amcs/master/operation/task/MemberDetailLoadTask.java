package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberDetail;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class MemberDetailLoadTask extends Task<MemberDetail> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberDetailLoadTask.class);

    private final String code;

    public MemberDetailLoadTask(String code) {
        this.code = code;
    }
    @Override
    protected MemberDetail call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER+ "/member-detail" + "/{code}";
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("code", code);
            ResponseEntity<MemberDetail> response = restTemplate.exchange(url, HttpMethod.GET, null, MemberDetail.class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("Member fetched: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error("Member fetch", e);
        }
        return null;
    }
}
