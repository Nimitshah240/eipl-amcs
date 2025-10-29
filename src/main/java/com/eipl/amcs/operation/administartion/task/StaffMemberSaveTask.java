package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.StaffMember;
import com.eipl.amcs.master.account.service.StaffMemberService;
import com.eipl.amcs.utils.CommonUtils;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;


public class StaffMemberSaveTask extends Task<Object> {
    private final StaffMember dto;
    private final short update;

    public StaffMemberSaveTask(StaffMember dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            StaffMemberService service = EmcsAppContext.getContext().getBean(StaffMemberService.class);
            if (this.update == 0) {
                service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                service.update(dto, CommonUtils.setIdentityHeader());
            }

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url= MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.STAFF_MEMBERS;
//            ResponseEntity<StaffMember> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), StaffMember.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), StaffMember.class);
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
