package com.eipl.amcs.operation.administartion.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.account.model.CommitteeMembers;
import com.eipl.amcs.master.account.service.CommitteeMembersService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class CommitteeMembersSaveTask extends Task<Object> {

    private final CommitteeMembers dto;
    private final short update;

    public CommitteeMembersSaveTask(CommitteeMembers dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            CommitteeMembersService service = EmcsAppContext.getContext().getBean(CommitteeMembersService.class);

            if (this.update == 0) {
                service.save(dto, CommonUtil.setIdentityHeader());
            } else {
                service.update(dto, CommonUtil.setIdentityHeader());
            }

//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url = MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.COMMITTEE_MEMBERS;
//
//            ResponseEntity<CommitteeMembers> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), CommitteeMembers.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), CommitteeMembers.class);
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
