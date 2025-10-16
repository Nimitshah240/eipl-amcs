package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberEkyc;
import com.eipl.amcs.master.operation.repository.MemberEkycRepository;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberEkycLoadTask extends Task<List<MemberEkyc>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberEkycLoadTask.class);

    public MemberEkycLoadTask() {
    }

    @Override
    protected List<MemberEkyc> call() throws Exception {
        try {
            MemberEkycRepository memberEkycRepository = EmcsAppContext.getContext().getBean(MemberEkycRepository.class);
            return memberEkycRepository.findAllWithMembers();

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER_EKYC + "/all";
//            Map<String, Object> uriVariables = new HashMap<>();
//            ResponseEntity<MemberEkyc[]> response = restTemplate.exchange(url, HttpMethod.GET, null, MemberEkyc[].class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK) {
//                LOGGER.warn("Failed to fetch MemberEkyc list, status: {}", response != null ? response.getStatusCode() : "null response");
//                return null;
//            }
//            LOGGER.info("MemberEkyc fetched: {}", response.getBody());
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Error fetching MemberEkyc list", e);
        }
        return null;
    }
}
