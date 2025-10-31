package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.CustomerDto;
import com.eipl.amcs.master.operation.service.CustomerService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class CustomerSaveTask extends Task<Object> {
    private final CustomerDto dto;
    private final short update;

    public CustomerSaveTask(CustomerDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            CustomerService service = EmcsAppContext.getContext().getBean(CustomerService.class);

            CustomerDto dtoNew = null;

            if (this.update == 0) {
                dtoNew = service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                dtoNew = service.update(dto, CommonUtils.setIdentityHeader());
            }
            if (dtoNew == null) {
                dtoNew.getCustomer().setSociety(dto.getCustomer().getSociety());
                dtoNew.getCustomer().setUnion(dto.getCustomer().getUnion());
                dtoNew.getCustomerDetail().setBank(dto.getCustomerDetail().getBank());
                dtoNew.getCustomerDetail().setBranch(dto.getCustomerDetail().getBranch());
                dtoNew.getCustomerDetail().setState(dto.getCustomerDetail().getState());
                dtoNew.getCustomerDetail().setDistrict(dto.getCustomerDetail().getDistrict());
                dtoNew.getCustomerDetail().setSubDistrict(dto.getCustomerDetail().getSubDistrict());
                dtoNew.getCustomerDetail().setVillage(dto.getCustomerDetail().getVillage());
                dtoNew.getCustomerDetail().setCustomer(dto.getCustomerDetail().getCustomer());
            }
            return dtoNew;

        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
