package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.CouponBalanceService;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

public class LocalMilkSaleSaveTask extends Task<Object> {
    private final LocalMilkSale dto;
    private final short update;
    private final CouponBalance couponBalance;

    public LocalMilkSaleSaveTask(LocalMilkSale dto, short update) {
        this.dto = dto;
        this.update = update;
        this.couponBalance = null;
    }

    public LocalMilkSaleSaveTask(LocalMilkSale dto, short update, CouponBalance couponBalance) {
        this.dto = dto;
        this.update = update;
        this.couponBalance = couponBalance;
    }

    @Override
    protected Object call() throws Exception {
        try {
            if (this.couponBalance != null && dto.getPaymentMode() == 2) {
                CouponBalanceService couponBalanceService = EmcsAppContext.getContext().getBean(CouponBalanceService.class);
                couponBalanceService.update(couponBalance);
            }
            LocalMilkSaleService service = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
            if (this.update == 0) {
                service.save(dto, CommonUtils.setIdentityHeader());
            } else {
                service.update(dto, CommonUtils.setIdentityHeader());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
