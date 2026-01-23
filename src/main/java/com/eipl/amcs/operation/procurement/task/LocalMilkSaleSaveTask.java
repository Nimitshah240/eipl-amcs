package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class LocalMilkSaleSaveTask extends Task<Object> {
    private final LocalMilkSale dto;
    private final short update;

    public LocalMilkSaleSaveTask(LocalMilkSale dto, short update) {
        this.dto = dto;
        this.update = update;
    }

    @Override
    protected Object call() throws Exception {
        try {
            LocalMilkSaleService service = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
            boolean isCoupon = (dto.getPaymentMode() == 2);

//            if (this.update == 0) {
//                service.insertBalance(dto, MainApp.OPERATION_SOURCE, MainApp.SOURCE_RECORD_ORG_TYPE,
//                        MainApp.OPERATION_CREATE);
//                service.save(dto, CommonUtils.setIdentityHeader());
//            } else {
//                service.updateBalance(dto, MainApp.OPERATION_SOURCE, MainApp.SOURCE_RECORD_ORG_TYPE,
//                        MainApp.OPERATION_CREATE);
//                service.update(dto, CommonUtils.setIdentityHeader());
//            }

            if (this.update == 0) {
                if(isCoupon){
                    service.insertBalance(dto, MainApp.OPERATION_SOURCE, MainApp.SOURCE_RECORD_ORG_TYPE,
                            MainApp.OPERATION_CREATE);
                    service.save(dto, CommonUtils.setIdentityHeader());
                }else {
                    service.save(dto, CommonUtils.setIdentityHeader());
                }
            }else{
                if (isCoupon) {
                    service.updateBalance(dto, MainApp.OPERATION_SOURCE, MainApp.SOURCE_RECORD_ORG_TYPE,
                            MainApp.OPERATION_CREATE);
                    service.update(dto, CommonUtils.setIdentityHeader());
                }else {
                    service.update(dto, CommonUtils.setIdentityHeader());
                }

            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
