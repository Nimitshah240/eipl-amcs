package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.dto.MilkDispatchDto;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class MilkDispatchSaveTask extends Task<Object> {

    private final MilkDispatchDto dto;
    private final short update;

    public MilkDispatchSaveTask(MilkDispatchDto dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {

            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            MilkDispatch dtoResult;

            if (this.update == 0) {
                dtoResult = service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                dtoResult = service.update(dto, CommonUtils.setIdentityHeader());
            }

            return dtoResult;

        } catch (HttpStatusCodeException e) {
            return EmcsAppContext.getContext().getBean(ApiJsonUtil.class).parseJsonString(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
