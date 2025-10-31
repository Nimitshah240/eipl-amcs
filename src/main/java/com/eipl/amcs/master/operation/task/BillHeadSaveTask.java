package com.eipl.amcs.master.operation.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.operation.service.BillHeadService;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;
import org.springframework.web.client.HttpStatusCodeException;

public class BillHeadSaveTask extends Task<Object> {

    private final BillHead billHead;
    private final short update;

    public BillHeadSaveTask(BillHead billHead, short update) {
        this.billHead = billHead;
        this.update = update;
    }


    @Override
    protected Object call() throws Exception {
        try {
            BillHeadService service = EmcsAppContext.getContext().getBean(BillHeadService.class);
            if (billHead == null)
                return null;
            if (this.update == 0) {
                service.saveBillHead(billHead, CommonUtils.setIdentityHeader());
            } else {
                service.updateBillHead(billHead, CommonUtils.setIdentityHeader());
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
