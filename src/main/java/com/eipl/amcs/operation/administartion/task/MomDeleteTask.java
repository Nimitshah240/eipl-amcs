package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
import com.eipl.amcs.util.CommonUtil;
import javafx.concurrent.Task;

public class MomDeleteTask extends Task<Boolean> {
    private final String code;

    public MomDeleteTask(String code) {
        this.code = code;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MeetingAgendaService service = EmcsAppContext.getContext().getBean(MeetingAgendaService.class);
            service.deleteMom(code, CommonUtil.setIdentityHeader());

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MOM + "/{code}";
//            Map<String, Object> uriVariables = new HashMap<>();
//            uriVariables.put("code", code);

//            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class, uriVariables);
//            if (response == null || response.getStatusCode() != HttpStatus.OK)
//                return null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
