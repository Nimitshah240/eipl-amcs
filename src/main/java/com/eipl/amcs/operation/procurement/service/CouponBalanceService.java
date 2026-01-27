package com.eipl.amcs.operation.procurement.service;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.master.global.model.MilkClass;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.operation.procurement.model.CouponBalance;
import com.eipl.amcs.operation.procurement.model.CouponBalanceTransaction;

public interface CouponBalanceService {


    CouponBalance fetchBalanceForConsumer(int consumerType, String consumerCode,
                                              MilkType animalType, MilkClass milkClass) throws Exception;


    public boolean insert(CouponBalance couponBalance, int intType, String strSourceOrgType, String strOperationType);

    public boolean update(CouponBalance couponBalance, int intType, String strSourceOrgType, String strOperationType);

    public boolean insert(CouponBalanceTransaction couponBalanceTxn, int intType, String strSourceOrgType,
                          String strOperationType);

    CouponBalanceTransaction fetchPrevTxn(int consumerType, String consumerCode,
                                          MilkType animalType, MilkClass milkClass) throws Exception;


}
