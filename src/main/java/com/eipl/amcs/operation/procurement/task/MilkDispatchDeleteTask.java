package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.MilkDispatch;
import com.eipl.amcs.operation.procurement.service.MilkDispatchService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class MilkDispatchDeleteTask extends Task<Boolean> {

    private final MilkDispatch dispatch;


    public MilkDispatchDeleteTask(MilkDispatch dispatch) {
        this.dispatch = dispatch;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MilkDispatchService service = EmcsAppContext.getContext().getBean(MilkDispatchService.class);
            service.delete(dispatch.getChallanNo(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
