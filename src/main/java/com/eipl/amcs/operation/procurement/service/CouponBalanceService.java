package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponBalanceTransaction;

public interface CouponBalanceService {


    CouponBalance fetchBalanceForConsumer(int consumerType, String consumerCode,
                                          MilkType animalType) throws Exception;


    boolean insert(CouponBalance couponBalance);

    boolean update(CouponBalance couponBalance);

}
