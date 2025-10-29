package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.master.account.service.CommitteeMembersService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommitteeMembersLoadTask extends Task<List<CommitteeMembers>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommitteeMembersLoadTask.class);

    @Override
    protected List<CommitteeMembers> call() throws Exception {
        try {
            CommitteeMembersService service = EmcsAppContext.getContext().getBean(CommitteeMembersService.class);

            List<CommitteeMembers> list = service.findAll();

            if (list == null || list.isEmpty())
                return null;
            return list;
//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.COMMITTEE_MEMBERS;
////            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
////                    .queryParam("society", MainApp.identityDto.getSociety().getCode());
//
//            ResponseEntity<CommitteeMembers[]> response = restTemplate.getForEntity(url, CommitteeMembers[].class);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("CommitteeMembers fetched: {}", response.getBody().length);
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("CommitteeMembers fetch", e);
        }
        return null;
    }
}
