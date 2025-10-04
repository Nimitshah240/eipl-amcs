package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.dto.MemberDetail;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllMemberDetailsLoadTask extends Task<List<MemberDetail>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllMemberDetailsLoadTask.class);


    public AllMemberDetailsLoadTask() {
    }
    @Override
    protected List<MemberDetail> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER+ "/member-details";
            Map<String, Object> uriVariables = new HashMap<>();
            ResponseEntity<MemberDetail[]> response = restTemplate.exchange(url, HttpMethod.GET, null, MemberDetail[].class, uriVariables);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("MemberDetails fetched: {}", response.getBody());
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("MemberDetails fetch", e);
        }
        return null;
    }
}
