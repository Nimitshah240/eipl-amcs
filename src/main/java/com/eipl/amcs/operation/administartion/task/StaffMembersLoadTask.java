package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.administartion.dto.StaffMember;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class StaffMembersLoadTask extends Task<List<StaffMember>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffMembersLoadTask.class);

    @Override
    protected List<StaffMember> call() throws Exception {
        try {
            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_MEMBERS;
//            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
//                    .queryParam("society", MainApp.identityDto.getSociety().getCode());

            ResponseEntity<StaffMember[]> response = restTemplate.getForEntity(url, StaffMember[].class);
            if (response == null || response.getStatusCode() != HttpStatus.OK)
                return null;
            LOGGER.info("CommitteeMembers fetched: {}", response.getBody().length);
            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("CommitteeMembers fetch", e);
        }
        return null;
    }
}
