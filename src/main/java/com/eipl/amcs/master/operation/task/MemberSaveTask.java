package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.MemberDto;
import com.eipl.amcs.master.operation.service.MemberService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

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
            MemberDto dtoNew = null;
            if (this.update == 0) {
                dtoNew = service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                dtoNew = service.update(dto, CommonUtils.setIdentityHeader());
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
            return true;
        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
