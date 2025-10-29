package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.Mom;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MomLoadTask extends Task<List<Mom>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MomLoadTask.class);
    private final String code;


    public MomLoadTask(String code) {
        this.code = code;
    }

    @Override
    protected List<Mom> call() throws Exception {
        try {
            MeetingAgendaService service = EmcsAppContext.getContext().getBean(MeetingAgendaService.class);
            List<Mom> list = service.findMom(code);
            if (list == null || list.isEmpty())
                return null;
            return list;

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MOM+"/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);
//            ResponseEntity<Mom[]> response = restTemplate.exchange(url, HttpMethod.GET, null, Mom[].class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
//            LOGGER.info("Mom fetched: {}", response.getBody());
//            return Arrays.asList(response.getBody());
        } catch (Exception e) {
            LOGGER.error("Mom fetch", e);
        }
        return null;
    }
}
