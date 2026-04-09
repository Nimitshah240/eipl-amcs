package com.eipl.amcs.operation.procurement.task;

import com.eipl.amcs.config.EmcsAppContext;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.LocalMilkSale;
import com.eipl.amcs.operation.procurement.service.CouponBalanceService;
import com.eipl.amcs.operation.procurement.service.LocalMilkSaleService;
import com.eipl.amcs.utils.CommonUtils;
import javafx.concurrent.Task;

import java.util.UUID;
public class LocalMilkSaleDeleteTask extends Task<Boolean> {
    private final LocalMilkSale localMilkSale;

    public LocalMilkSaleDeleteTask(LocalMilkSale localMilkSale) {
        this.localMilkSale = localMilkSale;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            if (localMilkSale != null && localMilkSale.getPaymentMode() == 2) {
                CouponBalanceService couponBalanceService = EmcsAppContext.getContext().getBean(CouponBalanceService.class);
                CouponBalance couponBalance = couponBalanceService.fetchBalanceForConsumer(localMilkSale.getConsumerType(), localMilkSale.getConsumerCode(), localMilkSale.getMilkType());
                if (couponBalance != null) {
                    couponBalance.setBalance(couponBalance.getBalance() + new Double(String.valueOf(localMilkSale.getAmount())));
                    couponBalanceService.update(couponBalance);
                } else {
                    couponBalance = new CouponBalance();
                    couponBalance.setValuesInObject(localMilkSale.getConsumerCode(), localMilkSale.getConsumerType(),
                            Double.parseDouble(localMilkSale.getAmount().toString()), localMilkSale.getMilkType());
                    couponBalance.setxCol1(UUID.randomUUID().toString());
                    couponBalanceService.insert(couponBalance);
                }
            }
            LocalMilkSaleService service = EmcsAppContext.getContext().getBean(LocalMilkSaleService.class);
            service.delete(localMilkSale.getCode(), CommonUtils.setIdentityHeader());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}