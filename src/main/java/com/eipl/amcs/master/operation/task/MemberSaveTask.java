package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.util.CommonUtil;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import javafx.concurrent.Task;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class MemberSaveTask extends Task<Object> {
    private final MemberDto dto;
    private final short update;

    public MemberSaveTask(MemberDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            MemberService service = EmcsAppContext.getContext().getBean(MemberService.class);
            MemberDto dtoNew=null;
            if (this.update == 0) {
                dtoNew = service.save(dto, CommonUtil.setIdentityHeader());
            } else {
                dtoNew = service.update(dto, CommonUtil.setIdentityHeader());
            }
            if (dtoNew == null) {
                dtoNew.getMember().setSociety(dto.getMember().getSociety());
                dtoNew.getMember().setMilkType(dto.getMember().getMilkType());
                dtoNew.getMember().setMemberType(dto.getMember().getMemberType());
                dtoNew.getMemberDetail().setGender(dto.getMemberDetail().getGender());
                dtoNew.getMemberDetail().setBank(dto.getMemberDetail().getBank());
                dtoNew.getMemberDetail().setBranch(dto.getMemberDetail().getBranch());
                dtoNew.getMemberDetail().setState(dto.getMemberDetail().getState());
                dtoNew.getMemberDetail().setDistrict(dto.getMemberDetail().getDistrict());
                dtoNew.getMemberDetail().setSubDistrict(dto.getMemberDetail().getSubDistrict());
                dtoNew.getMemberDetail().setVillage(dto.getMemberDetail().getVillage());
                dtoNew.getMemberDetail().setHamlet(dto.getMemberDetail().getHamlet());
                dtoNew.getMemberDetail().setMember(dto.getMemberDetail().getMember());
            }
            return dtoNew;


//            RestTemplate restTemplate = EmcsAppContext.getContext().getBean(RestTemplate.class);
//            String url= MainApp.getProperty(AppConstant.Props.BASE_URL, null) + AppConstant.UrlPath.MEMBER;
//            ResponseEntity<MemberDto> response = this.update == 0 ?
//                    restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dto), MemberDto.class) :
//                    restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(dto), MemberDto.class);
//
//            if (response == null || response.getStatusCode() != HttpStatus.CREATED)
//                return null;
//            return response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
