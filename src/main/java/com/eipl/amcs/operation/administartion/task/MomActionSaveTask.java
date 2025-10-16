package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.MomAction;
import com.eipl.amcs.master.account.service.MeetingAgendaService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class MomActionSaveTask extends Task<Object> {

    private final MomAction dto;
    private final short update;

    public MomActionSaveTask(MomAction dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {

            MeetingAgendaService service = EmcsAppContext.getContext().getBean(MeetingAgendaService.class);
            if (this.update == 0) {
                service.saveMomAction(dto, CommonUtil.setIdentityHeader());
            } else {
                service.updateMomAction(dto, CommonUtil.setIdentityHeader());
            }

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MOM_ACTION;
//
//            ResponseEntity<Mom> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), Mom.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), Mom.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null;
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
